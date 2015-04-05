package com.androidboys.spellarena.screens;

import org.json.JSONObject;

import appwarp.WarpController;
import appwarp.WarpListener;

import com.androidboys.spellarena.game.ButtonExample;
import com.androidboys.spellarena.gameworld.GameRenderer;
import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.InputHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class GameScreen implements Screen, WarpListener{

	private Game game;
	
	private GameWorld world;
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

    private ButtonExample[] commandList = new ButtonExample[3];
    int commandCount = 0;
	
	//Constructor for debugging w/o multiplayer
	public GameScreen(Game game) {
		this.game = game;
		
		this.cam = new OrthographicCamera(480,320);
		cam.position.set(480/2, 320/2, 0);
		
		batcher = new SpriteBatch();
		
		world = new GameWorld();
		renderer = new GameRenderer(batcher, world);
        stage = new Stage(new StretchViewport(960, 640), batcher);

        Texture textureUp   = new Texture(Gdx.files.internal("images/exort.png"));
        ButtonExample myButton   = new ButtonExample(textureUp);

        myButton.setPosition(790, 70);
        myButton.setSize(130, 130);
        myButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(0);
            }

        });

        Texture textureUp2   = new Texture(Gdx.files.internal("images/quas.png"));

        final ButtonExample myButton2   = new ButtonExample(textureUp2);

        myButton2.setPosition(790, 235);
        myButton2.setSize(130, 130);
        myButton2.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(1);
            }

        });

        Texture textureUp3   = new Texture(Gdx.files.internal("images/wex.png"));

        ButtonExample myButton3   = new ButtonExample(textureUp3);

        myButton3.setPosition(790, 400);
        myButton3.setSize(130, 130);
        myButton3.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                displayButtonCommand(2);
            }

        });

        stage.addActor(myButton);
        stage.addActor(myButton2);
        stage.addActor(myButton3);

        createTouchpad();

		inputHandler = new InputHandler(world,this);
		
		WarpController.getInstance().setListener(this);
		
	}

    public void displayButtonCommand(int indexOfButtonPressed) {
        if (commandCount > 2) {
            return;
        }
        Texture texture;
        switch (indexOfButtonPressed) {
            case 0:
                texture = new Texture(Gdx.files.internal("images/exort.png"));
                break;
            case 1:
                texture = new Texture(Gdx.files.internal("images/quas.png"));
                break;
            case 2:
                texture = new Texture(Gdx.files.internal("images/wex.png"));
                break;
            default:
                texture = new Texture(Gdx.files.internal("images/exort.png"));
                break;
        }

        ButtonExample newButton = new ButtonExample(texture, indexOfButtonPressed);
        newButton.setPosition(10, 550);
        newButton.setSize(60, 60);
        commandList[commandCount] = newButton;
        switch (commandCount) {
            case 0:
                break;
            case 1:
                commandList[0].setPosition(60, 550);
                break;
            case 2:
                commandList[0].setPosition(110, 550);
                commandList[1].setPosition(60, 550);
                break;
        }
        commandCount += 1;
        stage.addActor(newButton);
        if (commandCount == 3) {
            for (int i = 0; i < commandList.length; i++) {
                int commandIndex = commandList[i].getCommandIndex(); //0 for red 1 for purple 2 for blue
                //TODO use index make combos and clear buttons from stage - BY HUY

            }
            commandCount = 0; // reset state

        }
    }

	public GameScreen(Game game, StartMultiplayerScreen prevScreen) {
		
		this(game);
		this.prevScreen = prevScreen;
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
		
		background.dispose();
		knob.dispose();



		stage.addActor(touchpad);
		world.getBob().setTouchpad(touchpad);
	}


	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		renderer.render(runTime);
		stage.draw();
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
	
	public Stage getStage(){
		return stage;
	}
	
	public Touchpad getTouchpad(){
		return touchpad;
	}
}
