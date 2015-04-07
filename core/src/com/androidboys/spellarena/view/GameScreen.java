package com.androidboys.spellarena.view;

import org.json.JSONObject;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.gameworld.GameFactory;
import com.androidboys.spellarena.gameworld.GameRenderer;
import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.InputHandler;
import com.androidboys.spellarena.helper.StyleLoader;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.mediators.Mediator;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.net.WarpListener;
import com.androidboys.spellarena.servers.GameServer;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.widgets.LoadingWidget;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class GameScreen implements Screen{

	protected static final String TAG = "GameScreen";
	private SpellArena game;
	private Mediator mediator;
	
	private GameWorld world = new GameWorld();
	private GameScreenMediator gameScreenMediator;
	private GameServer gameServer;
	
	private GameRenderer renderer;
	
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	
	private StartMultiplayerScreen prevScreen;

	private TouchpadStyle touchpadStyle;
	private Touchpad touchpad;
	
	private Stage stage;
	
	private InputHandler inputHandler;
	
	private float runTime;
	
	private TiledMap map;
	private LoadingWidget loadingWidget;
	private TiledMapRenderer mapRenderer;
	private Label roomOwner;
	private Label p2Label;
	private Label p3Label;
	private Label p4Label;
	private Label roomName;
	private Label beforeGamePanelTitle;
	private Group disconnectPopUp;
	private Group roomOwnerLeftPopUp;
	private Group beforeGamePanel;
	private Group winGamePopUp;
	private Group loseGamePopUp;
	private TextButton startGameButton;
	
	//Constructor for debugging w/o multiplayer
	public GameScreen(SpellArena game, Mediator mediator) {
		this.game = game;
		this.mediator = mediator;
	
		
		this.gameScreenMediator = (GameScreenMediator) mediator;
		this.gameScreenMediator.setRoom(UserSession.getInstance().getRoom());
		
		initializeDefaultGameBoard();
		
		this.cam = new OrthographicCamera(480,320);
		cam.position.set(480/2, 320/2, 0);
		
		this.batcher = new SpriteBatch();
		this.stage = new Stage(new StretchViewport(720, 480));
//		
//
		renderer = new GameRenderer(batcher, world);	
//		
//		createTouchpad();
//		inputHandler = new InputHandler(world,this);
//		
//		WarpController.getInstance().setListener(this);
//		
	}


	private void initializeDefaultGameBoard() {
		map = AssetLoader.map;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		world.initialize(GameFactory.getGameModel());
	}

	
	public GameScreen(Game game, StartMultiplayerScreen prevScreen) {
		
		//this(game);
		this.prevScreen = prevScreen;
	}

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		mediator.onScreenShowInternal();
		
		if(UserSession.getInstance().isServer()) {
			initializeGameOnServer();
			gameServer = new GameServer(world);
			gameServer.initialize(game.getClient(),gameScreenMediator);
		}
		
		createTouchpad();
		initializeBeforeGamePanel();
		prepareInputProcessor();
		prepareDisconnectPopUp();
		prepareRoomOwnerLeftPopUp();
		prepareWinGamePopUp();
		prepareLoseGamePopUp();
	}
	
	private void prepareLoseGamePopUp() {
		loseGamePopUp = new Group();
		loseGamePopUp.setVisible(false);
		loseGamePopUp.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 200)/2, 
				400, 200);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		loseGamePopUp.addActor(panelBackground);
		panelBackground.setBounds(0, 0, 400, 240);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AssetLoader.playText;
		labelStyle.fontColor = Color.WHITE;
		Label popUpLabel = new Label(" You lost! Try again next time!", labelStyle);
		popUpLabel.setAlignment(1);
		loseGamePopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*loseGamePopUp.getHeight(), 
				loseGamePopUp.getWidth(), 
				0.67f*loseGamePopUp.getHeight());
		TextButtonStyle popUpButtonStyle = new TextButtonStyle();
		popUpButtonStyle.font = AssetLoader.playTextSmall;
		popUpButtonStyle.pressedOffsetX = 5;
		popUpButtonStyle.pressedOffsetY = -5;
		TextButton reconnectButton = new TextButton("Back to lobby", popUpButtonStyle);
		loseGamePopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*loseGamePopUp.getHeight(), 
				loseGamePopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				backToLobby();
			}
		});
		stage.addActor(loseGamePopUp);
	}


	private void prepareWinGamePopUp() {
		winGamePopUp = new Group();
		winGamePopUp.setVisible(false);
		winGamePopUp.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 200)/2, 
				400, 200);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		winGamePopUp.addActor(panelBackground);
		panelBackground.setBounds(0, 0, 400, 240);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AssetLoader.playText;
		labelStyle.fontColor = Color.WHITE;
		Label popUpLabel = new Label("You win!", labelStyle);
		popUpLabel.setAlignment(1);
		winGamePopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*winGamePopUp.getHeight(), 
				winGamePopUp.getWidth(), 
				0.67f*winGamePopUp.getHeight());
		TextButtonStyle popUpButtonStyle = new TextButtonStyle();
		popUpButtonStyle.font = AssetLoader.playTextSmall;
		popUpButtonStyle.pressedOffsetX = 5;
		popUpButtonStyle.pressedOffsetY = -5;
		TextButton reconnectButton = new TextButton("Back to lobby", popUpButtonStyle);
		winGamePopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*winGamePopUp.getHeight(), 
				winGamePopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				backToLobby();
			}
		});
		stage.addActor(winGamePopUp);
	}


	private void prepareRoomOwnerLeftPopUp() {
		roomOwnerLeftPopUp = new Group();
		roomOwnerLeftPopUp.debugAll();
		roomOwnerLeftPopUp.setVisible(false);
		roomOwnerLeftPopUp.setBounds(160, 
				40, 
				400, 200);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		roomOwnerLeftPopUp.addActor(panelBackground);
		panelBackground.setBounds(160, 40, 400, 240);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AssetLoader.playText;
		labelStyle.fontColor = Color.WHITE;
		Label popUpLabel = new Label("Host has disconnected", labelStyle);
		popUpLabel.setAlignment(1);
		roomOwnerLeftPopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*roomOwnerLeftPopUp.getHeight(), 
				roomOwnerLeftPopUp.getWidth(), 
				0.67f*roomOwnerLeftPopUp.getHeight());
		TextButtonStyle popUpButtonStyle = new TextButtonStyle();
		popUpButtonStyle.font = AssetLoader.playTextSmall;
		popUpButtonStyle.pressedOffsetX = 5;
		popUpButtonStyle.pressedOffsetY = -5;
		TextButton reconnectButton = new TextButton("Back to lobby", popUpButtonStyle);
		roomOwnerLeftPopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*roomOwnerLeftPopUp.getHeight(), 
				roomOwnerLeftPopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				backToLobby();
			}
		});
		stage.addActor(roomOwnerLeftPopUp);
	}


	private void prepareDisconnectPopUp() {
		disconnectPopUp = new Group();
		disconnectPopUp.setVisible(false);
		disconnectPopUp.setBounds((stage.getWidth() - 400)/2, 
				(stage.getHeight() - 200)/2, 
				400, 200);
		Image popUpBackground = new Image(AssetLoader.loadingTexture);
		popUpBackground.setBounds(0, 0, 400, 200);
		disconnectPopUp.addActor(popUpBackground);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AssetLoader.playText;
		labelStyle.fontColor = Color.WHITE;
		Label popUpLabel = new Label("Connection Error", labelStyle);
		popUpLabel.setAlignment(1);
		disconnectPopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*disconnectPopUp.getHeight(), 
				disconnectPopUp.getWidth(), 
				0.67f*disconnectPopUp.getHeight());
		TextButtonStyle popUpButtonStyle = new TextButtonStyle();
		popUpButtonStyle.font = AssetLoader.playTextSmall;
		popUpButtonStyle.pressedOffsetX = 5;
		popUpButtonStyle.pressedOffsetY = -5;
		TextButton reconnectButton = new TextButton("Back to lobby", popUpButtonStyle);
		disconnectPopUp.addActor(reconnectButton);
		reconnectButton.setBounds(0f, 
				0.33f*disconnectPopUp.getHeight(), 
				disconnectPopUp.getWidth(), 
				reconnectButton.getMaxHeight());
		reconnectButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y){
				backToLobby();
			}
		});
		stage.addActor(disconnectPopUp);
	}


	private void backToLobby() {
		game.backToPreviousScreen();
	}


	private void prepareInputProcessor() {
		// TODO Auto-generated method stub
		
	}

	private void initializeBeforeGamePanel() {
		beforeGamePanel = new Group();
		beforeGamePanel.debugAll();
		beforeGamePanel.setVisible(true);
		stage.addActor(beforeGamePanel);
		beforeGamePanel.setBounds((stage.getWidth()-600)/2,
				(stage.getHeight()-360)/2, 600, 360);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		beforeGamePanel.addActor(panelBackground);
		panelBackground.setFillParent(true);
		beforeGamePanelTitle = new Label("", StyleLoader.parchmentLabel);
		beforeGamePanel.addActor(beforeGamePanelTitle);
		beforeGamePanelTitle.setAlignment(Align.center);
		beforeGamePanelTitle.setBounds(100, 300, 400, 60);
		roomOwner = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(roomOwner);
		roomOwner.setBounds(0, 200, 300, 100);
		
		roomOwner.setVisible(false);
		p2Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p2Label);
		p2Label.setBounds(300, 200, 300, 100);
		p2Label.setVisible(false);
		p3Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p3Label);
		p3Label.setBounds(0, 100, 300, 100);
		p3Label.setVisible(false);
		p4Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p4Label);
		p4Label.setBounds(300, 100, 300, 100);
		p4Label.setVisible(false);
		roomName = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(roomName);
		roomName.setBounds(0, 0, 600, 40);
		roomName.setText(UserSession.getInstance().getRoom().getName());
		startGameButton = new TextButton("Start Game",StyleLoader.smallParchmentButtonStyle);
		startGameButton.setVisible(false);
		startGameButton.setBounds(100,40,400,60);
		startGameButton.align(Align.center);
		if(UserSession.getInstance().isServer()){
			setBeforeGamePanelTitle("Waiting for players...");
			roomOwner.setText("Player 1 (Room Owner) :" + UserSession.getInstance().getUserName());
			roomOwner.setVisible(true);
		} else {
			setBeforeGamePanelTitle("Waiting for game to start");
		}
	}

	private void setBeforeGamePanelTitle(String title) {
		beforeGamePanelTitle.setText(title);
	}


	private void createTouchpad(){
		Pixmap.setBlending(Blending.None);
		Pixmap background = new Pixmap(152,152,Format.RGBA8888);
		background.setColor(1,1,1,0.75f);
		background.drawCircle(76, 76, 74);
		background.setColor(1,1,1,0.75f);
		background.drawCircle(76, 76, 76);
		background.setColor(81/255f,112/255f,130/255f,1f);
		background.drawCircle(76, 76, 75);
		
		Pixmap knob = new Pixmap(22,22,Format.RGBA8888);
		knob.setColor(1,1,1,0.6f);
		knob.fillCircle(11, 11, 11);
		
		touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(background))); 
		touchpadStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(knob)));
		touchpad = new Touchpad(10,touchpadStyle);
		touchpad.setBounds(45, 45, 152, 152);
		touchpad.setVisible(false);
		
		background.dispose();
		knob.dispose();
		
		stage.addActor(touchpad);
		touchpad.setVisible(false);
		
	}
	

	private void initializeGameOnServer() {
		//GameFactory.GameModel gameModel = GameFactory.getGameModel(gameScreenMediator.getLevel());
		
//		Bob playerBob = addBobToWorld(UserSession.getInstance().getUserName(),1);
		
		//world.addObstacles
	}


	/**
	 * Called when the screen should render itself.
	 * @param delta	The time in seconds since the last render.
	 */
	@Override
	public void render(float delta) {
		runTime+=delta;
		update(delta);
		draw();
	}

	private void update(float delta) {		
		world.update(delta);
		stage.act(delta);
	}

	private void draw() {
		drawMap();
		renderer.render(runTime);
		stage.draw();
	}

	private void drawMap() {
		mapRenderer.setView(cam);
		mapRenderer.render();
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

	/*
	
	@Override
	public void onWaitingStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		if(isRemote) {
			prevScreen.onGameFinished(code, true);
		} else {

		}
		WarpController.getInstance().handleLeave();
	}

	@Override
	public void onGameUpdateReceived(String message) {
		try{
			JSONObject data = new JSONObject(message);
			float x = (float)data.getDouble("x");	
			float y = (float)data.getDouble("y");
			float vx = (float)data.getDouble("vx");
			float vy = (float)data.getDouble("vy");
			int state = (int)data.getDouble("state");
			world.updateEnemy(x, y, vx, vy, state);
		} catch (Exception e){
		}
	}
	*/
	
	public Stage getStage(){
		return stage;
	}
	
	public Touchpad getTouchpad(){
		return touchpad;
	}
	
	public void displayLoadingWidget(){
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


	public void onDisconnected() {
		disconnectPopUp.setVisible(true);
	}


	public void onPlayerJoinedRoom(String playerName) {
		if(!existPlayerOnWorld(playerName)) {
			int gameIndex = world.getNextGameIndex();
			addPlayerModelToWorld(playerName, gameIndex);
			final Group panel = beforeGamePanel;
			switch(gameIndex){
				case 1:
					roomOwner.setText(playerName);
					roomOwner.setVisible(true);
					break;
				case 2:
					p2Label.setText(playerName);
					p2Label.setVisible(true);
					break;
				case 3:
					p3Label.setText(playerName);
					p3Label.setVisible(true);
					break;
				case 4:
					p4Label.setText(playerName);
					p4Label.setVisible(true);
					break;	
			}
			if(UserSession.getInstance().isServer()){
				startGameButton.setVisible(true);
				startGameButton.addListener(new ClickListener(){
					
					@Override
					public void clicked(InputEvent event, float x, float y) {
						Gdx.app.log(TAG,"Start game button clicked");
						beforeGamePanel.remove();
						gameServer.createGame();
					}
				});
			}
		} else {
			Gdx.app.log(TAG,"Player already in room");
		}
	}


	private Bob addPlayerModelToWorld(String playerName, int gameIndex) {
		Bob bob = new Bob();
		bob.setPlayerName(playerName);
		bob.setPosition(loadStartPosition(gameIndex));
		bob.setGameIndex(gameIndex);
		world.addPlayerModel(bob);
		return bob;
	}


	private Vector2 loadStartPosition(int gameIndex) {
		MapLayer layer = (MapLayer) map.getLayers().get("StartPosition");
		for(MapObject mapObject: layer.getObjects()){
			System.out.println("map "+mapObject.getName());
			System.out.println("id "+"player"+gameIndex);
			if(mapObject.getName().equals("player"+gameIndex)){
				float x = (Float) mapObject.getProperties().get("x");
				float y = (Float) mapObject.getProperties().get("y");
				Vector2 startPosition = new Vector2(x,y);
				Gdx.app.log(TAG, "Loading starting position: "+startPosition);
				return startPosition;
			}
		}
		return null;
	}


	private boolean existPlayerOnWorld(String playerName) {
		return world.getPlayerModel(playerName) != null;
	}


	public void removePlayer(String playerName) {
		//world.removePlayer(playerName);
	}


	public void startGame() {
		touchpad.setVisible(true);
		//Set other buttons to be visible

	}


	
}
