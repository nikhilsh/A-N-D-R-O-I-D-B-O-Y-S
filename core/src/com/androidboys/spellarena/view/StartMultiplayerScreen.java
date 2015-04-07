package com.androidboys.spellarena.view;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.net.WarpListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class StartMultiplayerScreen implements Screen, WarpListener {

	Game game;
	
	private float width;
	private float height;
	
	OrthographicCamera cam;
	SpriteBatch batcher;
	Rectangle backBounds;
	Vector3 touchPoints;
	
	private float xOffset;
	
	private final String[] tryingToConnect = {"Connecting","to AppWarp"};
	private final String[] waitForOtherUser = {"Waiting for","other user"};
	private final String[] errorInConnection = {"Error in","Connection", "Go Back"};
	
	private final String[] game_win = {"Congrats You Win!", "Enemy Defeated"};
	private final String[] game_lose = {"Oops You Lose!","Target Achieved","By Enemy"};
	private final String[] enemy_left = {"Congrats You Win!", "Enemy Left the Game"};
	
	private String[] msg = tryingToConnect;

	public StartMultiplayerScreen(Game game, float width, float height) {
		this.game = game;
		
		this.width = width;
		this.height = height;
		
		cam = new OrthographicCamera(width, height);
		cam.position.set(width/2, height/2,0);
		
		
		backBounds = new Rectangle(10,10,
				AssetLoader.swordText.getBounds("E").width,
				AssetLoader.swordText.getBounds("E").height);
		touchPoints = new Vector3();
		
		batcher = new SpriteBatch();

		
		xOffset = 80;
		
		WarpController.getInstance().setListener(this);
	}

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {

	}

	/**
	 * Called when the screen should render itself.
	 * @param delta	The time in seconds since the last render.
	 */
	@Override
	public void render(float delta) {
		update();
		draw();
	}

	private void update() {
		if(Gdx.input.justTouched()){
			cam.unproject(touchPoints.set(Gdx.input.getX(),Gdx.input.getY(),0));
			if(backBounds.contains(touchPoints.x,touchPoints.y)){
//				game.setScreen(new MainMenuScreen(game, width, height));
				WarpController.getInstance().handleLeave();
				return;
			}
		}
	}


	private void draw() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batcher.setProjectionMatrix(cam.combined);
		
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(AssetLoader.backgroundRegion, 0, 0 ,width, height);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		float y = 230;
		for (int i = msg.length-1; i >= 0; i--) {
			float width = AssetLoader.parchmentText.getBounds(msg[i]).width;
			AssetLoader.parchmentText.draw(batcher, msg[i], 240-width/2, y);
			y += AssetLoader.parchmentText.getLineHeight();
		}

		AssetLoader.swordText.draw(batcher, "E", 10, backBounds.height+10);
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

	@Override
	public void onWaitingStarted(String message) {
		this.msg = waitForOtherUser;
		update();
	}

	@Override
	public void onError(String message) {
		this.msg = errorInConnection;
		update();
	}

	@Override
	public void onGameStarted(String message) {
		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				game.setScreen(new GameScreen(game, StartMultiplayerScreen.this));
			}
			
		});
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		if(code == WarpController.GAME_WIN){
			this.msg = game_win;
		} else if(code == WarpController.GAME_LOSE){
			this.msg = game_lose;
		} else if(code == WarpController.ENEMY_LEFT){
			this.msg = enemy_left;
		}
		update();
		game.setScreen(this);
	}

	@Override
	public void onGameUpdateReceived(String message) {
		// TODO Auto-generated method stub
		
	}

}
