package com.androidboys.spellarena.screens;

import org.json.JSONObject;

import appwarp.WarpController;
import appwarp.WarpListener;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
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
	
	//Constructor for debugging w/o multiplayer
	public GameScreen(Game game) {
		this.game = game;
		
		this.cam = new OrthographicCamera(480,320);
		cam.position.set(480/2, 320/2, 0);
		
		batcher = new SpriteBatch();
		
		world = new GameWorld();
		renderer = new GameRenderer(batcher, world);	
		
		createTouchpad();
		inputHandler = new InputHandler(world,this);
		
		WarpController.getInstance().setListener(this);
		
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
		
		stage = new Stage(new StretchViewport(960, 640), batcher);
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
