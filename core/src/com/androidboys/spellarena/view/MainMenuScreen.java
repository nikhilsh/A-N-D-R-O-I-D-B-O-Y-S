package com.androidboys.spellarena.view;

import java.util.List;
import java.util.Random;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.game.SpellArena.ScreenType;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.StyleLoader;
import com.androidboys.spellarena.mediators.MainMenuMediator;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.widgets.LoadingWidget;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainMenuScreen implements Screen {

	protected static final String TAG = "MainMenuScreen";
	private static final float PERIODIC_REQUEST_INTERVAL = 5;
	
	
	private SpellArena game;
	private MainMenuMediator mediator;
	
	float time;
	
	final float width = 480;
	final float height = 320;
	private boolean processingJoinRoom;
	private Group joinedRoomFailedPopUp;
	
	OrthographicCamera cam;
	SpriteBatch batcher;
	
	private Stage stage;
	
	private Image background;
	
	private Table gameTable;
	private TextButton createButton;
	private Group[] joinGameList;

	private LoadingWidget loadingWidget;
	
	private ShapeRenderer debugRenderer;
	private NetworkListenerAdapter networkListenerAdapter;
	private TextField userNameLabel;
	private Group connectionErrorPopUp;
	private float updateCounter;
	
	public MainMenuScreen(SpellArena game, MainMenuMediator mediator) {
		super();
		this.game = game;
		this.mediator = mediator;

		this.stage = new Stage(new StretchViewport(720,480));
		this.time = 0;

		cam = new OrthographicCamera(width,height);
		cam.position.set(width/2, height/2 , 0);
		
		debugRenderer = new ShapeRenderer();
		batcher = new SpriteBatch();
		
		initialize();
	}
	
	public void initialize(){
		
		gameTable = new Table();
		gameTable.setTouchable(Touchable.enabled);
		
		Image background = new Image(AssetLoader.backgroundTexture);
		stage.addActor(background);
		background.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		
		LabelStyle headerLabelStyle = new LabelStyle();
		headerLabelStyle.font = AssetLoader.header;
		headerLabelStyle.fontColor = Color.WHITE;
		Label spellText = new Label("Spell", headerLabelStyle);
		Label arenaText = new Label("Arena", headerLabelStyle);
		
		stage.addActor(spellText);
		spellText.setBounds(
				(stage.getWidth())/2 - 215 , 
				stage.getHeight()/2+130,
				stage.getWidth(), 
				spellText.getHeight());
		
		stage.addActor(arenaText);
		arenaText.setBounds(
				(stage.getWidth())/2 - 65 , 
				stage.getHeight()/2+80,
				stage.getWidth(), 
				spellText.getHeight());
		
		userNameLabel = new TextField("",StyleLoader.textFieldStyle);
		stage.addActor(userNameLabel);
		userNameLabel.setBounds(20, stage.getHeight() - 40, userNameLabel.getWidth(), 40);
				
		createButton = new TextButton("Create Game",StyleLoader.buttonStyle);
		stage.addActor(createButton);
		createButton.setBounds(
				stage.getWidth()/2-createButton.getWidth()/2, 
				50, 
				createButton.getWidth(), 
				createButton.getHeight());
		createButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.log(TAG, "Create button clicked");
				createNewGame();
			}
			
		});
		createButton.debugActor();
		initializeGameList();
		networkListenerAdapter = new NetworkListenerAdapter(){

			@Override
			public void onConnected(){
				updateUserInfo();
				game.getClient().listRooms();
			}

			@Override
			public void onDisconnected() {
				removeLoadingWidget();
				onConnectionError();
			}
			
			@Override
			public void onRoomListReceived(List<RoomModel> rooms) {
				reloadGameListPanels(rooms);
				removeLoadingWidget();
			}
			
			@Override
			public void onRoomListRequestFailed() {
				Gdx.app.log(TAG, "Room list request failed");
				removeLoadingWidget();
				if(!game.getClient().isConnected()){
					onDisconnected();
				}
			}
			
			@Override
			public void onJoinRoomSuccess(String roomId) {
				Gdx.app.log(TAG, "Joined room successfully");
				removeLoadingWidget();
				if (processingJoinRoom) {
					game.switchScreen(ScreenType.PLAY);
					processingJoinRoom = false;
					game.getClient().removeNetworkListener(networkListenerAdapter);
				}
			}
			
			@Override
			public void onJoinRoomFailed() {
				Gdx.app.log(TAG, "Joined room failed");
				processingJoinRoom = false;
				UserSession.getInstance().setRoom(null);
				removeLoadingWidget();
				MainMenuScreen.this.onJoinedRoomFailed();
			}
			
			@Override
			public void onRoomCreated(RoomModel room) {
				Gdx.app.log(TAG, "Created room: "+room.getName());
				if(processingJoinRoom) {
					UserSession.getInstance().setRoom(room);
					game.getClient().joinRoom(room.getId());
				}
			}
			
			@Override
			public void onCreateRoomFailed() {
				Gdx.app.log(TAG, "Create room failed");
				removeLoadingWidget();
				processingJoinRoom = false;
			}
			
			@Override
			public void onRoomInfoReceived(String[] players, String data) {
				boolean exists = false;
				if(players != null){
					for(String player:players){
						if(player.equals(UserSession.getInstance().getUserName())){
							exists = true;
							break;
						}
					}
				}
				if(!exists){
					game.getClient().joinRoom(UserSession.getInstance().getRoom().getId());
				} else {
					Gdx.app.log(TAG, "Player exists in room, switching to game screen");
					onJoinRoomSuccess(UserSession.getInstance().getRoom().getId());
				}
			}
		};
		
		displayLoadingWidget();
		game.getClient().connect();

		Gdx.input.setInputProcessor(stage);
	}

	private void initializeGameList() {
		Table gameList = new Table();
		stage.addActor(gameList);
		gameList.align(Align.bottom);
//		gameList.debug();
		
		gameTable.align(Align.bottom);
		gameTable.setFillParent(true);
		//gameTable.setScale(100,100);
//		gameTable.debug();
		
		final ScrollPane scrollPane = new ScrollPane(gameTable);
//		scrollPane.debug();
		scrollPane.setClamp(true);
		scrollPane.setOverscroll(false, false);
		scrollPane.setFillParent(true);
		scrollPane.setTouchable(Touchable.enabled);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollbarsOnTop(false);
		gameList.setSize(460, 200);
		gameList.setPosition(stage.getWidth()/2 - 230, 110);
		gameList.addActor(scrollPane);

	}
	
	private void reloadGameListPanels(List<RoomModel> rooms) {
		gameTable.clearChildren();
		gameTable.clearListeners();
		if(rooms != null){
			for(final RoomModel roomModel: rooms){
				try{
					Group group = createGameListItem(roomModel);
                    gameTable.row();
                    gameTable.add(group)
                    .width(460)
                    .height(40);
				} catch (Exception e){
					
				}
			}
			if(rooms.size() < 6){
				for(int i = rooms.size(); i<6 ; i++){
					Group group = createGameListItem(null);
					gameTable.row();
					gameTable.add(group)
                    .width(460)
                    .height(40);
				}
			}
		}

	}

	private Group createGameListItem(final RoomModel roomModel) {
		final Group group = new Group();
		final Label label;
		if(roomModel == null){
			label = new Label("", StyleLoader.tableLabelStyle);
		} else {
			label = new Label(roomModel.getName(), StyleLoader.tableLabelStyle);
		}
		label.setAlignment(Align.left);
		TextButtonStyle smallButtonStyle = new TextButtonStyle();
		smallButtonStyle.font = AssetLoader.playTextSmall;
		smallButtonStyle.pressedOffsetX = 5;
		smallButtonStyle.pressedOffsetY = -5;
		final TextButton joinGameButton;
		if(roomModel == null){
			joinGameButton = new TextButton("", smallButtonStyle);
		} else {
			joinGameButton = new TextButton("Join Game", smallButtonStyle);
		}
		joinGameButton.debugActor();
		group.addActor(label);
		group.addActor(joinGameButton);
		joinGameButton.right();
		label.setPosition(0, 0);
		label.setSize(300, 25);
		joinGameButton.setBounds(300,0,160,25);
		joinGameButton.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log(TAG, "Join game clicked");
				joinGame(roomModel);
				return super.touchDown(event, x, y, pointer, button);
			}
			
		});
		return group;
	}

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		game.getClient().addNetworkListener(networkListenerAdapter);
	}
	
	/**
	 * Called when the screen should render itself.
	 * @param delta	The time in seconds since the last render.
	 */
	@Override
	public void render(float delta) {
		time+=delta;
		update(delta);
		stage.act(delta);
		stage.draw();
	}

	
	private void update(float delta) {
		updateCounter += delta;
		if(!processingJoinRoom && updateCounter >= PERIODIC_REQUEST_INTERVAL) {
            game.getClient().listRooms();
            updateCounter = 0;
        }
		
//		if(Gdx.input.justTouched()){
////			WarpController.getInstance().startApp(getRandomHexString(10));
////			game.setScreen(new StartMultiplayerScreen(game,width,height));
//			game.setScreen(new GameScreen(game));
//			return;
//		}
	}

	@Deprecated
	private void draw() {
		Gdx.gl20.glClearColor(1, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		batcher.setProjectionMatrix(cam.combined);
		
//		batcher.disableBlending();
//		batcher.begin();
//		batcher.draw(AssetLoader.backgroundRegion, 0, 0 ,width, height);
//		batcher.end();
		
		batcher.enableBlending();
		batcher.begin();
		
		AssetLoader.header.draw(batcher, "Spell", width/2-165, 300);
		AssetLoader.header.draw(batcher, "Arena", width/2-165+50, 300-40);
//		if(!((int)time%3 == 0)){ //Blink
//			AssetLoader.playText.draw(batcher, "Tap screen to play", width/2-165+25, 150);
//		}
//		
		batcher.end();
	}

	/**
	 * Called when the Application is resized.
	 * @param width		the new width in pixels
	 * @param height	the new height in pixels
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when the Application is paused, usually when it's not active or visible on screen.
	 * An Application is also paused before it is destroyed.
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when the Application is resumed from a paused state, usually when it regains focus.
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	protected void createNewGame() {
		Gdx.app.log(TAG, "New game created.");
		//UserSession.getInstance().setUserName(userNameLabel.getMessageText());
		displayLoadingWidget();
		Random random = new Random(System.currentTimeMillis());
		game.getClient().createRoom(UserSession.getInstance().getUserName()
				+ " Room " 
				+ random.nextInt(1000));
	}
	
	private void joinGame(RoomModel roomModel) {
		//UserSession.getInstance().setUserName(userNameLabel.getText());
		processingJoinRoom = true;
		displayLoadingWidget();
		UserSession.getInstance().setRoom(roomModel);
		game.getClient().getRoomInfo(roomModel.getId());
	}
	
	public void removeLoadingWidget() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (loadingWidget != null) {
                	Gdx.app.log(TAG, "Removing loading widget.");
                    loadingWidget.setVisible(false);
                    loadingWidget.remove();
                }
            }
        });

    }
	
	private void displayLoadingWidget(){
		if(loadingWidget != null){
			loadingWidget.setVisible(false);
			loadingWidget.remove();
		}
		loadingWidget = LoadingWidget.getInstance();
		loadingWidget.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 100)/2, 
				400, 100);
		loadingWidget.setVisible(true);
		stage.addActor(loadingWidget);
		
	}
	
	private void onConnectionError() {
		if(connectionErrorPopUp == null){
			prepareErrorPopUp();
		}
		connectionErrorPopUp.setVisible(true);
	}
	
	private void prepareErrorPopUp() {
		connectionErrorPopUp = new Group();
		connectionErrorPopUp.setVisible(false);
		connectionErrorPopUp.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 200)/2, 
				400, 200);
		Image popUpBackground = new Image(AssetLoader.loadingTexture);
		popUpBackground.setBounds(0, 0, 400, 200);
		connectionErrorPopUp.addActor(popUpBackground);
		Label popUpLabel = new Label("Connection Error", StyleLoader.parchmentLabel);
		popUpLabel.setAlignment(1);
		connectionErrorPopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*connectionErrorPopUp.getHeight(), 
				connectionErrorPopUp.getWidth(), 
				0.67f*connectionErrorPopUp.getHeight());
		TextButton reconnectButton = new TextButton("Reconnect", StyleLoader.smallParchmentButtonStyle);
		connectionErrorPopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*connectionErrorPopUp.getHeight(), 
				connectionErrorPopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				displayLoadingWidget();
				game.getClient().connect();
				connectionErrorPopUp.setVisible(false);
			}
		});
		stage.addActor(connectionErrorPopUp);
	}

	private void onJoinedRoomFailed() {
		if(joinedRoomFailedPopUp == null){
			prepareJoinedRoomFailedPopUp();
		}
		joinedRoomFailedPopUp.setVisible(true);
	}
	
	private void prepareJoinedRoomFailedPopUp() {
		joinedRoomFailedPopUp = new Group();
		joinedRoomFailedPopUp.setVisible(true);
		joinedRoomFailedPopUp.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 200)/2, 
				400, 200);
		Image popUpBackground = new Image(AssetLoader.loadingTexture);
		popUpBackground.setBounds(0, 0, 400, 200);
		joinedRoomFailedPopUp.addActor(popUpBackground);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AssetLoader.playText;
		labelStyle.fontColor = Color.BLACK;
		Label popUpLabel = new Label("join room failed", labelStyle);
		popUpLabel.setAlignment(1);
		joinedRoomFailedPopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*joinedRoomFailedPopUp.getHeight(), 
				joinedRoomFailedPopUp.getWidth(), 
				0.67f*joinedRoomFailedPopUp.getHeight());
		TextButtonStyle popUpButtonStyle = new TextButtonStyle();
		popUpButtonStyle.font = AssetLoader.playTextSmall;
		popUpButtonStyle.pressedOffsetX = 5;
		popUpButtonStyle.pressedOffsetY = -5;
		TextButton reconnectButton = new TextButton("ok", popUpButtonStyle);
		joinedRoomFailedPopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*joinedRoomFailedPopUp.getHeight(), 
				joinedRoomFailedPopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				displayLoadingWidget();
				joinedRoomFailedPopUp.setVisible(false);
			}
		});
		stage.addActor(connectionErrorPopUp);
	}
	
	private void updateUserInfo(){
		userNameLabel.setMessageText(UserSession.getInstance().getUserName());
	}
	
	
}
