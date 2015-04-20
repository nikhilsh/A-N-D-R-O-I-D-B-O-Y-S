package com.androidboys.spellarena.view;

import java.util.List;
import java.util.Random;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.game.SpellArena.ScreenType;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.AudioManager;
import com.androidboys.spellarena.helper.StyleLoader;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.widgets.LoadingWidget;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainMenuScreen implements Screen {

	private static final String TAG = "MainMenuScreen";
	private static final float PERIODIC_REQUEST_INTERVAL = 5;
	
	private SpellArena game;
	
	final float width = 480;
	final float height = 320;
	
	private boolean processingJoinRoom;
	
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	private Stage stage;
	
	private TextField userNameLabel;	
	private ImageButton helpButton;
	private ImageButton musicOnButton, musicOffButton;
	private Table gameTable;
	private TextButton createButton;

	private LoadingWidget loadingWidget;
	private Group joinedRoomFailedPopUp;
	private Group connectionErrorPopUp;
	private Group helpPopUp;
	
	private NetworkListenerAdapter networkListenerAdapter;

	private float updateCounter;


	
	public MainMenuScreen(SpellArena game) {
		super();
		this.game = game;
		this.stage = new Stage(new StretchViewport(720,480));
		this.cam = new OrthographicCamera(width,height);
		this.cam.position.set(width/2, height/2 , 0);
		this.batcher = new SpriteBatch();
		
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
		
		helpButton = new ImageButton(new SpriteDrawable(new Sprite(AssetLoader.questionTexture)));
		stage.addActor(helpButton);
		helpButton.setBounds(stage.getWidth() - 45, stage.getHeight() - 45, 
				40, 40);
		helpButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				displayHelpPopUp();
				super.clicked(event, x, y);
			}
			
		});
		
		musicOnButton = new ImageButton(new SpriteDrawable(new Sprite(AssetLoader.musicOnTexture)));
		stage.addActor(musicOnButton);
		musicOnButton.setBounds(stage.getWidth() - 90, stage.getHeight() - 45, 
				40, 40);
		musicOnButton.setVisible(true);
		musicOnButton.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				AudioManager.toggleMusic();		
				musicOffButton.setVisible(true);
				musicOnButton.setVisible(false);
			};
		});
		
		musicOffButton = new ImageButton(new SpriteDrawable(new Sprite(AssetLoader.musicOffTexture)));
		stage.addActor(musicOffButton);
		musicOffButton.setBounds(stage.getWidth() - 90, stage.getHeight() - 45, 
				40, 40);
		musicOffButton.setVisible(false);
		musicOffButton.addListener(new ClickListener(){
			
			public void clicked(InputEvent event, float x, float y) {
				AudioManager.toggleMusic();
				musicOffButton.setVisible(false);
				musicOnButton.setVisible(true);
			};
		});
		
		createButton = new TextButton("Create Game",StyleLoader.textButtonStyle);
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

		initializeGameList();
		prepareHelpPopUp();
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
		AudioManager.playMainTheme();
	}

	private void initializeGameList() {
		Table gameList = new Table();
		stage.addActor(gameList);
		gameList.align(Align.bottom);
		
		gameTable.align(Align.bottom);
		gameTable.setFillParent(true);
		
		final ScrollPane scrollPane = new ScrollPane(gameTable);
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
		synchronized(gameTable){
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
		group.addActor(label);
		group.addActor(joinGameButton);
		joinGameButton.right();
		label.setPosition(0, 0);
		label.setSize(300, 25);
		joinGameButton.setBounds(300,0,160,25);
		if(roomModel != null){
			joinGameButton.addListener(new InputListener(){
				
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, "Join game clicked");
					joinGame(roomModel);
					return super.touchDown(event, x, y, pointer, button);
				}
				
			});
		}
		return group;
	}

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		game.getClient().addNetworkListener(networkListenerAdapter);
		Gdx.input.setInputProcessor(stage);
	}
	
	/**
	 * Called when the screen should render itself.
	 * @param delta	The time in seconds since the last render.
	 */
	@Override
	public void render(float delta) {
		update(delta);
		stage.act(delta);
		synchronized (gameTable) {
			stage.draw();
		}
	}

	
	private void update(float delta) {
		updateCounter += delta;
		if(!processingJoinRoom && updateCounter >= PERIODIC_REQUEST_INTERVAL) {
            game.getClient().listRooms();
            updateCounter = 0;
        }
	}

	@SuppressWarnings("unused")
	@Deprecated
	/**
	 * Method to render the main menu, not obsolete due to using stage.
	 */
	private void draw() {
		Gdx.gl20.glClearColor(1, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		batcher.setProjectionMatrix(cam.combined);
		batcher.enableBlending();
		batcher.begin();
		
		AssetLoader.header.draw(batcher, "Spell", width/2-165, 300);
		AssetLoader.header.draw(batcher, "Arena", width/2-165+50, 300-40);
		batcher.end();
	}

	/**
	 * Called when the Application is resized.
	 * @param width		the new width in pixels
	 * @param height	the new height in pixels
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Called when the Application is paused, usually when it's not active or visible on screen.
	 * An Application is also paused before it is destroyed.
	 */
	@Override
	public void pause() {

	}

	/**
	 * Called when the Application is resumed from a paused state, usually when it regains focus.
	 */
	@Override
	public void resume() {

	}

	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	@Override
	public void hide() {
	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose() {
	}
	
	protected void createNewGame() {
		Gdx.app.log(TAG, "New game created.");
		processingJoinRoom = true;
		displayLoadingWidget();
		Random random = new Random(System.currentTimeMillis());
		synchronized (gameTable) {
			game.getClient().createRoom(UserSession.getInstance().getUserName()
					+ " Room " 
					+ random.nextInt(1000));
		}

	}
	
	private void joinGame(RoomModel roomModel) {
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
		stage.addActor(joinedRoomFailedPopUp);
	}
	
	private void updateUserInfo(){
		userNameLabel.setMessageText(UserSession.getInstance().getUserName());
	}
	
	private void prepareHelpPopUp(){
		helpPopUp = new Group();
		helpPopUp.setVisible(false);
		stage.addActor(helpPopUp);
		helpPopUp.setBounds((stage.getWidth()-600)/2,
				(stage.getHeight()-360)/2, 600, 360);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		helpPopUp.addActor(panelBackground);
		panelBackground.setFillParent(true);
		Label helpTitle = new Label("\nHow to Play\n", StyleLoader.parchmentLabel);
		helpPopUp.addActor(helpTitle);
		helpTitle.setAlignment(Align.center);
		helpTitle.setBounds(100, 300, 400, 60);
		
		Group scrollGroup = new Group();
//		scrollGroup.debugAll();
		helpPopUp.addActor(scrollGroup);
		scrollGroup.setBounds(0, 20, 600, 280);
		
		Table helpTable = new Table();
		helpTable.setFillParent(true);
		helpTable.align(Align.bottom);
		
		final ScrollPane scrollPane = new ScrollPane(helpTable);
		scrollPane.setClamp(true);
		scrollPane.setOverscroll(false, false);
		scrollPane.setFillParent(true);
		scrollPane.setTouchable(Touchable.enabled);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollbarsOnTop(false);
		scrollGroup.addActor(scrollPane);

		helpTable.align(Align.bottom);
		helpTable.add(new Label("Defeat all enemies to win\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		helpTable.add(new Label("Damage enemies by casting spells\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		helpTable.add(new Label("Move your character with the touchpad\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		helpTable.add(new Label("Change your active spell by tapping the orbs\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		Label label0 = new Label("Your last three selected orbs will \ndetermine your active spell\n", 
				StyleLoader.smallParchmentLabel);
		label0.setAlignment(Align.center);
		helpTable.add(label0).height(60);
		helpTable.row();
		Label label1 = new Label("The order of the orbs does not matter\n only the combination does\n", 
				StyleLoader.smallParchmentLabel);
		label1.setAlignment(Align.center);
		helpTable.add(label1).height(60);
		helpTable.row();
		helpTable.add(new Label("Tap anywhere on the screen to cast your active spell\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		
		helpTable.add(new Label("Spell List", 
				StyleLoader.parchmentLabel)).height(60);
		helpTable.row();
		helpTable.add(new Label("Defensive skills\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		
		Table spellTable0 = new Table();
		spellTable0.setSize(360, 40);
		spellTable0.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable0.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable0.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		Label spellLabel0 = new Label("Divine Shield", StyleLoader.smallParchmentLabel);
		spellLabel0.setAlignment(Align.right);
		spellTable0.add(spellLabel0).width(240).height(40);
		helpTable.add(spellTable0).width(360).height(40);
		helpTable.row();
		Label spellDesc0 = new Label("Renders your character invulnerable\n for a short duration\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc0.setAlignment(Align.center);
		helpTable.add(spellDesc0).height(60);
		helpTable.row();
		
		Table spellTable1 = new Table();
		spellTable1.setSize(360, 40);
		spellTable1.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable1.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable1.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		Label spellLabel1 = new Label("Haste", StyleLoader.smallParchmentLabel);
		spellLabel1.setAlignment(Align.right);
		spellTable1.add(spellLabel1).width(240).height(40);
		helpTable.add(spellTable1).width(360).height(40);
		helpTable.row();
		Label spellDesc1 = new Label("Speeds up your character for a short duration\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc1.setAlignment(Align.center);
		helpTable.add(spellDesc1).height(40);
		helpTable.row();
		
		Table spellTable2 = new Table();
		spellTable2.setSize(360, 40);
		spellTable2.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable2.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable2.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel2 = new Label("Blink", StyleLoader.smallParchmentLabel);
		spellLabel2.setAlignment(Align.right);
		spellTable2.add(spellLabel2).width(240).height(40);
		helpTable.add(spellTable2).width(360).height(40);
		helpTable.row();
		Label spellDesc2 = new Label("Teleports your character a short distance\n in front of you\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc1.setAlignment(Align.center);
		helpTable.add(spellDesc2).height(60);
		helpTable.row();
		
		helpTable.add(new Label("Offensive skills\n", 
				StyleLoader.smallParchmentLabel)).height(40);
		helpTable.row();
		
		Table spellTable3 = new Table();
		spellTable3.setSize(360, 40);
		spellTable3.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable3.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable3.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		Label spellLabel3 = new Label("Shadow Ball", StyleLoader.smallParchmentLabel);
		spellLabel3.setAlignment(Align.right);
		spellTable3.add(spellLabel3).width(240).height(40);
		helpTable.add(spellTable3).width(360).height(40);
		helpTable.row();
		Label spellDesc3 = new Label("Shoots out three damaging orbs in front of you\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc3.setAlignment(Align.center);
		helpTable.add(spellDesc3).height(40);
		helpTable.row();
		
		Table spellTable4 = new Table();
		spellTable4.setSize(360, 40);
		spellTable4.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable4.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable4.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel4 = new Label("Hurricane", StyleLoader.smallParchmentLabel);
		spellLabel4.setAlignment(Align.right);
		spellTable4.add(spellLabel4).width(240).height(40);
		helpTable.add(spellTable4).width(360).height(40);
		helpTable.row();
		Label spellDesc4 = new Label("Summons three damaging tornados that spirals out \n"
				+ "from your location\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc4.setAlignment(Align.center);
		helpTable.add(spellDesc4).height(60);
		helpTable.row();
		
		Table spellTable5 = new Table();
		spellTable5.setSize(360, 40);
		spellTable5.add(new Image(AssetLoader.wexTexture)).width(40).height(40);
		spellTable5.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		spellTable5.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel5 = new Label("Explosion", StyleLoader.smallParchmentLabel);
		spellLabel5.setAlignment(Align.right);
		spellTable5.add(spellLabel5).width(240).height(40);
		helpTable.add(spellTable5).width(360).height(40);
		helpTable.row();
		Label spellDesc5 = new Label("Creates an extremely damaging explosion at \n"
				+ "the point you tapped after a second delay\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc5.setAlignment(Align.center);
		helpTable.add(spellDesc5).height(60);
		helpTable.row();
		
		Table spellTable6 = new Table();
		spellTable6.setSize(360, 40);
		spellTable6.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable6.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable6.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		Label spellLabel6 = new Label("Thunderstorm", StyleLoader.smallParchmentLabel);
		spellLabel6.setAlignment(Align.right);
		spellTable6.add(spellLabel6).width(240).height(40);
		helpTable.add(spellTable6).width(360).height(40);
		helpTable.row();
		Label spellDesc6 = new Label("Summons an extremely powerful thunderstorm\n"
				+ " in front of you\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc6.setAlignment(Align.center);
		helpTable.add(spellDesc6).height(60);
		helpTable.row();
		
		Table spellTable7 = new Table();
		spellTable7.setSize(360, 40);
		spellTable7.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable7.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable7.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel7 = new Label("Spectral Throw", StyleLoader.smallParchmentLabel);
		spellLabel7.setAlignment(Align.right);
		spellTable7.add(spellLabel7).width(240).height(40);
		helpTable.add(spellTable7).width(360).height(40);
		helpTable.row();
		Label spellDesc7 = new Label("Throws out a ghostly sword that will return to you\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc7.setAlignment(Align.center);
		helpTable.add(spellDesc7).height(40);
		helpTable.row();
		
		Table spellTable8 = new Table();
		spellTable8.setSize(360, 40);
		spellTable8.add(new Image(AssetLoader.quasTexture)).width(40).height(40);
		spellTable8.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		spellTable8.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel8 = new Label("Laser", StyleLoader.smallParchmentLabel);
		spellLabel8.setAlignment(Align.right);
		spellTable8.add(spellLabel8).width(240).height(40);
		helpTable.add(spellTable8).width(360).height(40);
		helpTable.row();
		Label spellDesc8 = new Label("Burns your enemies with a laser in front of you \n"
				+ "Pew pew pew\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc8.setAlignment(Align.center);
		helpTable.add(spellDesc8).height(60);
		helpTable.row();
		
		Table spellTable9 = new Table();
		spellTable9.setSize(360, 40);
		spellTable9.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		spellTable9.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		spellTable9.add(new Image(AssetLoader.exortTexture)).width(40).height(40);
		Label spellLabel9 = new Label("Firewall", StyleLoader.smallParchmentLabel);
		spellLabel9.setAlignment(Align.right);
		spellTable9.add(spellLabel9).width(240).height(40);
		helpTable.add(spellTable9).width(360).height(40);
		helpTable.row();
		Label spellDesc9 = new Label("Creates an extremely damaging yet slow moving \n"
				+ "wall of fire in front of you\n", 
				StyleLoader.smallParchmentLabel);
		spellDesc9.setAlignment(Align.center);
		helpTable.add(spellDesc9).height(60);
		helpTable.row();
	}
	
	private void displayHelpPopUp() {
		helpPopUp.setVisible(!helpPopUp.isVisible());
	}
	
}
