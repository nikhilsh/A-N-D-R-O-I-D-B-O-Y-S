package com.androidboys.spellarena.view;

import java.util.HashMap;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.gameworld.GameFactory;
import com.androidboys.spellarena.gameworld.GameRenderer;
import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.AudioManager;
import com.androidboys.spellarena.helper.StyleLoader;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.mediators.Mediator;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Spell;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.servers.GameClient;
import com.androidboys.spellarena.servers.GameServer;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.widgets.ButtonWidget;
import com.androidboys.spellarena.view.widgets.LoadingWidget;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class GameScreen implements Screen{

	public interface MovementChangeListener{
		
		public void onMovementChanged(int movement);
		
	}
	
	public static final int MOVEMENT_NONE = 10000;
	public static final int MOVEMENT_NORTH = 10001;
	public static final int MOVEMENT_NORTHEAST = 10002;
	public static final int MOVEMENT_EAST = 10003;
	public static final int MOVEMENT_SOUTHEAST = 10004;
	public static final int MOVEMENT_SOUTH = 10005;
	public static final int MOVEMENT_SOUTHWEST = 10006;
	public static final int MOVEMENT_WEST = 10007;
	public static final int MOVEMENT_NORTHWEST = 10008;
	

	private static float LASER_COOLDOWN = 0;
	private static float BLINK_COOLDOWN = 0;
	private static float SHADOWBLAST_COOLDOWN = 0;
	private static float DIVINESHIELD_COOLDOWN = 0;
	private static float FIREWALL_COOLDOWN = 0;
	private static float EXPLOSION_COOLDOWN = 0;
	private static float SPECTRALTHROW_COOLDOWN = 0;
	private static float THUNDERSTORM_COOLDOWN = 0;
	private static float BLADESTORM_COOLDOWN = 0;
	private static float HASTE_COOLDOWN = 0;
	
	protected static final String TAG = "GameScreen";
	private SpellArena game;
	private Mediator mediator;
	
	private GameWorld world = new GameWorld();
	private GameScreenMediator gameScreenMediator;
	private GameServer gameServer;
	private GameClient gameClient;
	private Bob bob = world.getPlayerModel(UserSession.getInstance().getUserName());
	
	private GameRenderer renderer;
	
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	private TouchpadStyle touchpadStyle;
	private Touchpad touchpad;
	private int movement = MOVEMENT_NONE;
	
	private Stage stage;
		
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
	private Group helpPopUp;
	private TextButton startGameButton;
	
	private ButtonWidget[] commandList = new ButtonWidget[3];
    private int[] spellList = new int[3];
    int commandCount = 0;
	private MovementChangeListener movementChangeListener;
	
	private boolean gameStarted;
	private boolean serverReady;
	private boolean connectedToServer;
	private boolean prevMovementChanged;
	
	private float[] cooldown;
	
	private String serverIp;
	
	private HashMap<String,Integer> playersList = new HashMap<String, Integer>();
	private HashMap<String,Boolean> clientsReady = new HashMap<String, Boolean>();
	private InputMultiplexer multiplexer;
	
	public GameScreen(SpellArena game, Mediator mediator) {
		this.game = game;
		this.mediator = mediator;
		world.setMediator((GameScreenMediator)mediator);

		this.serverReady = false;
		this.connectedToServer = false;
		this.gameStarted = false;
		this.gameEnded = false;
		
		this.gameScreenMediator = (GameScreenMediator) mediator;
		this.gameScreenMediator.setRoom(UserSession.getInstance().getRoom());
		this.bob = world.getPlayerModel(UserSession.getInstance().getUserName());

		initializeDefaultGameBoard();
		
		this.cam = new OrthographicCamera(480,320);
		cam.position.set(480/2, 320/2, 0);
		
		this.batcher = new SpriteBatch();
		this.stage = new Stage(new StretchViewport(720, 480));
//
		renderer = new GameRenderer(batcher, world);	
		AudioManager.stopMainTheme();
		AudioManager.playStartGame();
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

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		mediator.onScreenShowInternal();
		
		createTouchpad();
		createSpellButtons();
		createSpellLabel();
		initializeBeforeGamePanel();
		setBackKeyListener();
		prepareInputProcessor();
		prepareDisconnectPopUp(); 
		prepareRoomOwnerLeftPopUp();
		prepareWinGamePopUp();
		prepareLoseGamePopUp();
		
		if(UserSession.getInstance().isServer()) {
			initializeGameOnServer();
			gameServer = GameServer.getInstance();
			gameServer.initialize(world, game.getClient(),gameScreenMediator);
			gameServer.startServer();
			gameScreenMediator.setGameServer(gameServer);
		} else {

		}
		
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
		Label popUpLabel = new Label("YOU LOSE", StyleLoader.parchmentLabel);
		popUpLabel.setAlignment(1);
		loseGamePopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*loseGamePopUp.getHeight(), 
				loseGamePopUp.getWidth(), 
				0.67f*loseGamePopUp.getHeight());
		TextButton reconnectButton = new TextButton("Back to lobby", StyleLoader.smallParchmentButtonStyle);
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
		Label popUpLabel = new Label("YOU WIN", StyleLoader.parchmentLabel);
		popUpLabel.setAlignment(1);
		winGamePopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*winGamePopUp.getHeight(), 
				winGamePopUp.getWidth(), 
				0.67f*winGamePopUp.getHeight());
		TextButton reconnectButton = new TextButton("Back to lobby", StyleLoader.smallParchmentButtonStyle);
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
				100, 
				400, 200);
		Image panelBackground = new Image(AssetLoader.loadingTexture);
		roomOwnerLeftPopUp.addActor(panelBackground);
		panelBackground.setBounds(0, 0, 400, 240);
		Label popUpLabel = new Label("Host has disconnected", StyleLoader.parchmentLabel);
		popUpLabel.setAlignment(1);
		roomOwnerLeftPopUp.addActor(popUpLabel);
		popUpLabel.setBounds(0f, 
				0.33f*roomOwnerLeftPopUp.getHeight(), 
				roomOwnerLeftPopUp.getWidth(), 
				0.67f*roomOwnerLeftPopUp.getHeight());
		TextButton reconnectButton = new TextButton("Back to lobby", StyleLoader.smallParchmentButtonStyle);
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
		gameScreenMediator.disconnect(gameStarted);
		game.backToPreviousScreen();
		AudioManager.playMainTheme();
	}
	
	private void prepareInputProcessor() {
		this.movementChangeListener = new MovementChangeListener() {

			@Override
			public void onMovementChanged(int movement) {
				if(world.getPlayerModel(UserSession.getInstance().getUserName())
						.getState() != Bob.STATE_DEAD){
					gameScreenMediator.move(movement);
					world.movePlayer(UserSession.getInstance().getUserName(), movement);
				}
			}
		};
		
		touchpad.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				int movement;
				float touchX = touchpad.getKnobPercentX();
				float touchY = touchpad.getKnobPercentY();
				if(touchX > 0.5){
					if(touchY > 0.5){
						movement = MOVEMENT_NORTHEAST;

					} else if (touchY < -0.5){
						movement = MOVEMENT_SOUTHEAST;
					} else {
						movement = MOVEMENT_EAST;
					}
				}else if (touchX < -0.5){
					if(touchY > 0.5){
						movement = MOVEMENT_NORTHWEST;
					} else if (touchY < -0.5){
						movement = MOVEMENT_SOUTHWEST;
					} else {
						movement = MOVEMENT_WEST;
					}
				} else {
					if(touchY > 0.5){
						movement = MOVEMENT_NORTH;
					} else if(touchY < -0.5) {
						movement = MOVEMENT_SOUTH;
					} else {
						movement = MOVEMENT_NONE;
					}
				}
				if(movement!=GameScreen.this.movement&&!prevMovementChanged){
					GameScreen.this.movement = movement;
					movementChangeListener.onMovementChanged(movement);
					prevMovementChanged = true;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				int movement;
				float touchX = touchpad.getKnobPercentX();
				float touchY = touchpad.getKnobPercentY();
				if(touchX > 0.5){
					if(touchY > 0.5){
						movement = MOVEMENT_NORTHEAST;

					} else if (touchY < -0.5){
						movement = MOVEMENT_SOUTHEAST;
					} else {
						movement = MOVEMENT_EAST;
					}
				}else if (touchX < -0.5){
					if(touchY > 0.5){
						movement = MOVEMENT_NORTHWEST;
					} else if (touchY < -0.5){
						movement = MOVEMENT_SOUTHWEST;
					} else {
						movement = MOVEMENT_WEST;
					}
				} else {
					if(touchY > 0.5){
						movement = MOVEMENT_NORTH;
					} else if(touchY < -0.5) {
						movement = MOVEMENT_SOUTH;
					} else {
						movement = MOVEMENT_NONE;
					}
				}
				if(movement!=GameScreen.this.movement&&!prevMovementChanged){
					GameScreen.this.movement = movement;
					movementChangeListener.onMovementChanged(movement);
					prevMovementChanged = true;
				}
				super.touchDragged(event, x, y, pointer);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(GameScreen.this.movement!=MOVEMENT_NONE){
					GameScreen.this.movement = MOVEMENT_NONE ;
					movementChangeListener.onMovementChanged(movement);
					prevMovementChanged = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);

		multiplexer.addProcessor(new InputProcessor(){

			@Override
			public boolean keyDown(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				int direction = 0;
				Bob bob = world.getPlayerModel(UserSession.getInstance().getUserName());
				if(bob.getState() == Bob.STATE_DEAD){
					return false;
				}
				switch (bob.getDirection()) {
				case EAST:
					direction = 0;
					break;
				case NORTH:
					direction = 1;
					break;
				case NORTHEAST:
					direction = 2;
					break;
				case NORTHWEST:
					direction = 3;
					break;
				case SOUTH:
					direction = 4;
					break;
				case SOUTHEAST:
					direction = 5;
					break;
				case SOUTHWEST:
					direction = 6;
					break;
				case WEST:
					direction = 7;
					break;
				default:
					break;
				}
				Spell spell = new Spell(world, bob.getPosition().x, bob.getPosition().y, null, direction);
				int[] spellList = new int[3];
		        for (int i = 0; i < commandList.length; i++) {
		            try {
		            	spellList[i] = commandList[i].getCommandIndex(); //1 for red 2 for purple 4 for blue
		            } catch (Exception e) {
		            	return false;
		            }
		        }
				spell.spellSettler(spellList[0], spellList[1], spellList[2]);
				if (spell.getSpell() == 6){
					Vector3 touchPoint = new Vector3(screenX, screenY, 0);
					touchPoint = renderer.getCam().unproject(touchPoint);
				    System.out.println("VECTORX: " +touchPoint.x);
					System.out.println("VECTORY: " +touchPoint.y);
					if (castSpell(UserSession.getInstance().getUserName(),
							touchPoint.x, touchPoint.y, spell.getSpell(), direction)){
						gameScreenMediator.spellCommand(direction, spell.getSpell(), 
								touchPoint.x, touchPoint.y);
					}
				}
				else {
					if (castSpell(UserSession.getInstance().getUserName(),
							bob.getPosition().x, bob.getPosition().y, spell.getSpell(), direction)){
						gameScreenMediator.spellCommand(direction, spell.getSpell(), 
								bob.getPosition().x, bob.getPosition().y);
					}
				}


				return true;
			}	
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		Gdx.input.setInputProcessor(stage);
	}

	private void initializeBeforeGamePanel() {
		beforeGamePanel = new Group();
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
		roomOwner.setAlignment(Align.center);
		p2Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p2Label);
		p2Label.setBounds(300, 200, 300, 100);
		p2Label.setVisible(false);
		p2Label.setAlignment(Align.center);
		p3Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p3Label);
		p3Label.setBounds(0, 100, 300, 100);
		p3Label.setVisible(false);
		p3Label.setAlignment(Align.center);
		p4Label = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(p4Label);
		p4Label.setBounds(300, 100, 300, 100);
		p4Label.setVisible(false);
		p4Label.setAlignment(Align.center);
		roomName = new Label("", StyleLoader.smallParchmentLabel);
		beforeGamePanel.addActor(roomName);
		roomName.setBounds(0, 280, 600, 40);
		roomName.setAlignment(Align.center);
		roomName.setText(UserSession.getInstance().getRoom().getName());
		startGameButton = new TextButton("Waiting for server",StyleLoader.parchmentButtonStyle);
		startGameButton.setVisible(true);
		startGameButton.setBounds(100,20,400,60);
		startGameButton.align(Align.center);
		beforeGamePanel.addActor(startGameButton);
		if(UserSession.getInstance().isServer()){
			setBeforeGamePanelTitle("Waiting for players");
			startGameButton.setText("Setting up server");
			roomOwner.setText("Room Owner\n\n" + UserSession.getInstance().getUserName());
			roomOwner.setVisible(true);
		} else {
			setBeforeGamePanelTitle("Waiting for game to start");
		}
	}

	private void setBeforeGamePanelTitle(String title) {
		beforeGamePanelTitle.setText(title);
	}


	/**
	 * Creates the touchpad.
	 */
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
	

	/**
	 * Initialize game on server.
	 */
	private void initializeGameOnServer() {
		Gdx.app.log(TAG, "initializeGameOnServer");
		GameFactory.GameModel gameModel = GameFactory.getGameModel();
		addPlayerModelToWorld(UserSession.getInstance().getUserName(),1);
	}
	
	/**
	 * Initialize client.
	 */
	private void initializeClient() {
		Gdx.app.log(TAG, "Initializing game client");
		gameClient = new GameClient();
		gameClient.initialize(game.getClient(), gameScreenMediator);
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

	int n = 0;
	private ButtonWidget myButton;
	private ButtonWidget myButton2;
	private ButtonWidget myButton3;
	private boolean roomInfoProcessed;
	private Label spellLabel;
	private boolean gameEnded;
	
	/**
	 * Update.
	 *
	 * @param delta the delta
	 */
	private void update(float delta) {
		synchronized (world.getPlayerModels()) {
			n++;
			if(n%10 == 0){
				prevMovementChanged = false;
			}
			if(gameStarted&&!gameEnded){
				world.update(delta);
			}
			stage.act(delta);
			if (gameStarted&&n%30 == 0){
				gameScreenMediator.update(world.getPlayerModel(UserSession.getInstance().getUserName()));
			}
		}
		updateCooldown(delta);
		updateSpellLabel();
	}

	/**
	 * Draw.
	 */
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
		gameScreenMediator.leaveRoom();
	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose() {
		gameScreenMediator.disconnect(gameStarted);
	}
	
	/**
	 * Gets the stage.
	 *
	 * @return the stage
	 */
	public Stage getStage(){
		return stage;
	}
	
	/**
	 * Gets the touchpad.
	 *
	 * @return the touchpad
	 */
	public Touchpad getTouchpad(){
		return touchpad;
	}
	
	/**
	 * Display loading widget.
	 */
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


	/**
	 * On disconnected.
	 */
	public void onDisconnected() {
		disconnectPopUp.setVisible(true);
	}


	/**
	 * On player joined room.
	 *
	 * @param playerName the player name
	 */
	public void onPlayerJoinedRoom(String playerName)  {
		if(!existPlayerOnWorld(playerName)) {
			int gameIndex = world.getNextGameIndex();
			addPlayerModelToWorld(playerName, gameIndex);
			playersList.put(playerName,gameIndex);
			Gdx.app.log(TAG,"Player "+playerName+" joined with index "+gameIndex);
			switch(gameIndex){
				case 1:
					roomOwner.setText("Room Owner\n\n"+playerName);
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
			clientsReady.put(playerName, false);
			if(connectedToServer){
				gameScreenMediator.connectToServerSuccess(null);
			}
			if(UserSession.getInstance().isServer()){
				if(serverReady)gameScreenMediator.sendServerAddress(playerName);
			}
		} else {
			Gdx.app.log(TAG,"Player already in room");
		}
	}
	
	/**
	 * Creates the spell element buttons.
	 */
	public void createSpellButtons(){
        myButton   = new ButtonWidget(AssetLoader.wexTexture);

        myButton.setPosition(480, 40);
        myButton.setSize(80, 80);
        myButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(1);
                updateSpellLabel();
            }
        });

        myButton2   = new ButtonWidget(AssetLoader.quasTexture);

        myButton2.setPosition(600, 40);
        myButton2.setSize(80, 80);
        myButton2.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(10);
                updateSpellLabel();
            }

        });

        myButton3   = new ButtonWidget(AssetLoader.exortTexture);

        myButton3.setPosition(600, 150);
        myButton3.setSize(80, 80);
        myButton3.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(100);
                updateSpellLabel();
            }

        });

        stage.addActor(myButton);
        stage.addActor(myButton2);
        stage.addActor(myButton3);
        myButton.setVisible(false);
        myButton2.setVisible(false);
        myButton3.setVisible(false);
	}
	
	/**
	 * Update cooldown.
	 *
	 * @param delta the delta
	 */
	private void updateCooldown(float delta){
		 if (SHADOWBLAST_COOLDOWN>0){
			 SHADOWBLAST_COOLDOWN -= delta;
		 }
		 if (SPECTRALTHROW_COOLDOWN>0){
			 SPECTRALTHROW_COOLDOWN -= delta;
		 }
		 if (HASTE_COOLDOWN>0){
			 HASTE_COOLDOWN -= delta;
		 }
		 if (DIVINESHIELD_COOLDOWN>0){
			 DIVINESHIELD_COOLDOWN -= delta;
		 }
		 if (FIREWALL_COOLDOWN>0){
			 FIREWALL_COOLDOWN -= delta;
		 }
		 if (BLINK_COOLDOWN>0){
			 BLINK_COOLDOWN -= delta;
		 }
		 if (EXPLOSION_COOLDOWN>0){
			 EXPLOSION_COOLDOWN-= delta;
		 }
		 if (BLADESTORM_COOLDOWN>0){
			 BLADESTORM_COOLDOWN -= delta;
		 }
		 if (SHADOWBLAST_COOLDOWN>0){
			 SHADOWBLAST_COOLDOWN -= delta;
		 }
		 if (THUNDERSTORM_COOLDOWN>0){
			 THUNDERSTORM_COOLDOWN -= delta;
		 }
		 if (LASER_COOLDOWN>0){
			 LASER_COOLDOWN -= delta;
		 }
	}
	
	/**
	 * Update spell label.
	 */
	private void updateSpellLabel(){
		int spellId = 0;
		for(int i = 0; i< commandList.length ; i++){
			if (commandList[i] != null){
				spellId += commandList[i].getCommandIndex();
			}
		}
		String spellName;
		String spellCountdown;
		switch(spellId){
		case 3:
			spellCountdown = ((DIVINESHIELD_COOLDOWN <= 0) ? "Ready" : ""+((int)DIVINESHIELD_COOLDOWN+1));
			spellName = "Divine Shield " + spellCountdown;
			break;
		case 12:
			spellCountdown = ((HASTE_COOLDOWN <= 0) ? "Ready" : ""+((int)HASTE_COOLDOWN+1));
			spellName = "Haste " + spellCountdown;
			break;
		case 21:
			spellCountdown = ((SHADOWBLAST_COOLDOWN <= 0) ? "Ready" : ""+((int)SHADOWBLAST_COOLDOWN+1));
			spellName = "Shadow Blast " +  spellCountdown;
			break;
		case 30:
			spellCountdown = ((THUNDERSTORM_COOLDOWN <= 0) ? "Ready" : ""+((int)THUNDERSTORM_COOLDOWN+1));
			spellName = "Thunderstorm " + spellCountdown;
			break;
		case 102:
			spellCountdown = ((BLINK_COOLDOWN <= 0) ? "Ready" : ""+((int)BLINK_COOLDOWN+1));
			spellName = "Blink " + spellCountdown;
			break;
		case 111:
			spellCountdown = ((BLADESTORM_COOLDOWN <= 0) ? "Ready" : ""+((int)BLADESTORM_COOLDOWN+1));
			spellName = "Hurricane " +  spellCountdown;
			break;
		case 120:
			spellCountdown = ((SPECTRALTHROW_COOLDOWN <= 0) ? "Ready" : ""+((int)SPECTRALTHROW_COOLDOWN+1));
			spellName = "Spectral Throw " + spellCountdown;
			break;
		case 201:
			spellCountdown = ((EXPLOSION_COOLDOWN <= 0) ? "Ready" : ""+((int)EXPLOSION_COOLDOWN+1));
			spellName = "Explosion " +  spellCountdown;
			break;
		case 210:
			spellCountdown = ((LASER_COOLDOWN <= 0) ? "Ready" : ""+((int)LASER_COOLDOWN+1));
			spellName = "Laser " +  spellCountdown;
			break;
		case 300:
			spellCountdown = ((FIREWALL_COOLDOWN <= 0) ? "Ready" : ""+((int)FIREWALL_COOLDOWN+1));
			spellName = "Firewall " + spellCountdown;
			break;
		default:
			spellName = "";
		}
		spellLabel.setText(spellName);
	}
	
	/**
	 * Creates the spell label.
	 */
	private void createSpellLabel(){
		spellLabel = new Label("", StyleLoader.tableLabelStyle);
		stage.addActor(spellLabel);
		spellLabel.setBounds(0, 420, 640, 60);
		spellLabel.setVisible(false);
		spellLabel.setAlignment(Align.right);
	}
	
	 /**
 	 * Creates the images of the last three spells
 	 *
 	 * @param indexOfButtonPressed the index of button pressed
 	 */
 	public void displayButtonCommand(int indexOfButtonPressed) {
	        if (commandCount > 2) {
	            return;
	        }
	        Texture texture = null;
	        switch (indexOfButtonPressed) {
	            case 1:
	                texture = AssetLoader.wexTexture;
	                break;
	            case 10:
	                texture = AssetLoader.quasTexture;
	                break;
	            case 100:
	                texture = AssetLoader.exortTexture;
	                break;	
	        }

	        ButtonWidget newButton = new ButtonWidget(texture, indexOfButtonPressed);
	        newButton.setSize(40,40);
	        if (commandList[2] != null) {
	        	commandList[2].remove();
	        }
	        commandList[2] = commandList[1];
	        commandList[1] = commandList[0];
	        commandList[0] = newButton;
	        int y = 420;
	        int spacing = 50;
	        int x0 = 30;
	        for (int i = 0; i < 3; i++) {
	        	int x = x0 + spacing *i;
	        	try {
	        		commandList[i].setPosition(x, y);
	        	} catch (Exception e) {
	        		break;
	        	}
	        }
	        stage.addActor(commandList[0]);
	    }
 	
	/**
	 * Cast spell.
	 *
	 * @param playerName the player name
	 * @param x the x
	 * @param y the y
	 * @param spell the spell
	 * @param direction the direction
	 * @return the boolean
	 */
	public Boolean castSpell(String playerName, float x, float y, int spell, int direction){
		Gdx.app.log(TAG,"Casting spell: "+spell);
		Spells spellEnum = null;
		switch(spell){
		case 0:
			if (DIVINESHIELD_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.DIVINESHIELD;
			if (playerName == UserSession.getInstance().getUserName()){
				DIVINESHIELD_COOLDOWN = 5;
			}
			AudioManager.playShieldSound();
			break;		
		case 1:
			if (BLINK_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.BLINK;
			if (playerName == UserSession.getInstance().getUserName()){
				BLINK_COOLDOWN = 2;
			}
			AudioManager.playBlink();
			break;		
		case 2:
			if (HASTE_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.HASTE;
			if (playerName == UserSession.getInstance().getUserName()){
				HASTE_COOLDOWN = 5;
			}
			AudioManager.playHaste();
			break;		
		case 3:
			if (FIREWALL_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.FIREWALL;
			if (playerName == UserSession.getInstance().getUserName()){
				FIREWALL_COOLDOWN = 5;
			}
			AudioManager.playFirewall();
			break;		
		case 4:
			if (BLADESTORM_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.HURRICANE;
			if (playerName == UserSession.getInstance().getUserName()){
				BLADESTORM_COOLDOWN = 5;
			}
			AudioManager.playHurricane();
			break;		
		case 5:
			if (THUNDERSTORM_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.THUNDERSTORM;
			if (playerName == UserSession.getInstance().getUserName()){
				THUNDERSTORM_COOLDOWN = 5;
			}
			AudioManager.playThunderstorm();
			break;		
		case 6:
			if (EXPLOSION_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.EXPLOSION;
			if (playerName == UserSession.getInstance().getUserName()){
				EXPLOSION_COOLDOWN = 5;
			}
			AudioManager.playSunstrike();
			break;		
		case 7:
			if (LASER_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.LASER;
			if (playerName == UserSession.getInstance().getUserName()){
				LASER_COOLDOWN = 5;
			}
			AudioManager.playLaser();
			break;		
		case 8:
			if (SPECTRALTHROW_COOLDOWN > 0){
				return false;
			}
			spellEnum =  Spells.SPECTRALTHROW;
			if (playerName == UserSession.getInstance().getUserName()){
				SPECTRALTHROW_COOLDOWN = 5;
			}
			AudioManager.playBoomerang();

			break;		
		case 9:
			if (SHADOWBLAST_COOLDOWN > 0){
				return false;
			}
			spellEnum = Spells.SHADOWBLAST;
			if (playerName == UserSession.getInstance().getUserName()){
				SHADOWBLAST_COOLDOWN = 5;
			}
			AudioManager.playSpark();
			break;		
		default:
			break;
		}
		
		world.castSpell(playerName,x,y,spellEnum,direction);
		return true;
	}

		private Bob addPlayerModelToWorld(String playerName, int gameIndex) {
			Gdx.app.log(TAG, "addPlayerModelToWorld: "+playerName+","+gameIndex);
			Bob bob = new Bob();
			bob.setPlayerName(playerName);
			bob.setPosition(loadStartPosition(gameIndex));
			bob.setGameIndex(gameIndex);
			world.addPlayerModel(bob);
			renderer.initGameObjects();
			return bob;
		}


	private Vector2 loadStartPosition(int gameIndex) {
		MapLayer layer = (MapLayer) map.getLayers().get("StartPosition");
		for(MapObject mapObject: layer.getObjects()){

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
		Gdx.app.log(TAG,"existPlayerOnWorld: "+playerName);
		return world.getPlayerModel(playerName) != null;
	}

	 private void setBackKeyListener() {
	        stage.addListener(new InputListener() {
	            @Override
	            public boolean keyDown(InputEvent event, int keycode) {
	                if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                            	if(!gameStarted||gameEnded)
                            		{
		                        		gameScreenMediator.disconnect(gameStarted);
		                                if (game.getNumberScreens() > 1) {
		                                    game.backToPreviousScreen();
		                                }
                            		}
                            }
                        });
	                }
	                return true;
	            }
	        });
	    }
	

	public void removePlayer(String playerName) {
		renderer.removePlayer(playerName);
		world.removePlayer(playerName);
		if(world.isGameEnd()){
			displayWinGamePopup();
		}
	}

	public void onStartGame() {
		myButton.setVisible(true);
		myButton2.setVisible(true);
		myButton3.setVisible(true);
		touchpad.setVisible(true);
		spellLabel.setVisible(true);
		gameStarted = true;
		beforeGamePanel.remove();
		Gdx.input.setInputProcessor(multiplexer);
		world.initialize(GameFactory.getGameModel());
		if(gameScreenMediator.getNetworkListenerAdapter() != null){
			game.getClient().removeNetworkListener(gameScreenMediator.getNetworkListenerAdapter());
		}
		RoomModel roomModel = UserSession.getInstance().getRoom();
		if(roomModel != null){
			game.getClient().leaveRoom(roomModel.getId());
			if(UserSession.getInstance().isRoomOwner()){
				game.getClient().deleteRoom(roomModel.getId());
			}
		}
	}

	public ButtonWidget[] getCommandList() {
		return commandList;
	}


	public void onMove(long time, String fromUser, int movement, float x, float y) {
		world.movePlayer(time, fromUser, movement, x, y);
	}


	public void onUpdate(String fromUser, long timestamp, Vector2 position, Vector2 velocity, float health) {
		world.updatePlayer(fromUser, timestamp, position, velocity, health);
	}


	public void onServerStarted() {
		serverReady = true;
		for(String playerName: clientsReady.keySet()){
			if(!clientsReady.get(playerName)){
				gameScreenMediator.sendServerAddress(playerName);
			}
		}
		startGameButton.setVisible(false);
	}


	public void onServerStartFail() {
		startGameButton.setText("Retry start server");
		startGameButton.clearListeners();
		startGameButton.setVisible(true);
		startGameButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameServer.startServer();
			}
			
		});
	}


	public void connectToServer(String ip) {
		initializeClient();
		this.serverIp = ip;
		Gdx.app.log(TAG, "connectToServer");
		gameClient.connectToServer(ip);
	}


	public void connectToServerFail() {
		startGameButton.setText("Reconnect to host");
		startGameButton.clearListeners();
		startGameButton.setVisible(true);
		startGameButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				connectToServer(serverIp);
			}
			
		});
	}

	public void connectToServerSuccess() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		connectedToServer = true;
		if(roomInfoProcessed){
			onPlayerReady(UserSession.getInstance().getUserName());
		}
		startGameButton.setVisible(false);
	}


	public void onPlayerReady(String playerName) {
		Gdx.app.log(TAG, "onPlayerReady: "+playerName);
		if(!clientsReady.get(playerName)){
			int playerNo = playersList.get(playerName);
			clientsReady.put(playerName, true);
			Label playerLabel = null;
			switch(playerNo){
				case 2:
					playerLabel = p2Label;
					break;
				case 3:
					playerLabel = p3Label;
					break;
				case 4:
					playerLabel = p4Label;
					break;
			}
			playerLabel.setText(playerName+"\n\nready");
			if(UserSession.getInstance().isServer()){
				boolean readyToStart = true;
				for(String client: clientsReady.keySet()){
					if(!clientsReady.get(client)){
						readyToStart = false;
					}
				}
				if(readyToStart){
					startGameButton.setText("Start Game");
					startGameButton.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							Gdx.app.log(TAG,"Start game button clicked");
							beforeGamePanel.remove();
							gameScreenMediator.startGame();
						}
					});
					startGameButton.setVisible(true);
				}
			}
		}
	}


	public void setServerReady(boolean b) {
		this.serverReady = true;
	}

	public void roomInfoProcessed() {
		this.roomInfoProcessed = true;
		if(connectedToServer){
			onPlayerReady(UserSession.getInstance().getUserName());
		}
	}


	public void displayWinGamePopup() {
		if(!gameEnded){
			AudioManager.playEndGame();
		}
		winGamePopUp.setVisible(true);
		gameEnded = true;
	}

	public void displayLoseGamePopup(String winnerName) {
		if(!gameEnded){
			AudioManager.playEndGame();
		}
		loseGamePopUp.setVisible(true);
		gameEnded = true;
	}

	public void onOwnerLeft() {
		AudioManager.playEndGame();
		gameScreenMediator.disconnect(gameStarted);
		if(!world.isGameEnd()){
			roomOwnerLeftPopUp.setVisible(true);
		}
	}	
	
	
	
}
