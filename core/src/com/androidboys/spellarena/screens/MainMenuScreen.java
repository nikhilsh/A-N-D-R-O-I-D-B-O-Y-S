package com.androidboys.spellarena.screens;

import java.util.Random;

import appwarp.WarpController;

import com.androidboys.spellarena.helper.AssetLoader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sun.xml.internal.ws.policy.AssertionSet;

public class MainMenuScreen implements Screen {

	Game game;
	
	float time;
	
	float width;
	float height;
	
	OrthographicCamera cam;
	SpriteBatch batcher;
	
	public MainMenuScreen(Game game, float width, float height) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.time = 0;

		cam = new OrthographicCamera(width,height);
		cam.position.set(width/2, height/2 , 0);
		
		batcher = new SpriteBatch();
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
		time+=delta;
		update();
		draw();
	}

	private void update() {
		if(Gdx.input.justTouched()){
//			WarpController.getInstance().startApp(getRandomHexString(10));
//			game.setScreen(new StartMultiplayerScreen(game,width,height));
			game.setScreen(new GameScreen(game));
			return;
		}
	}

	long last = System.nanoTime();
	private void draw() {
		Gdx.gl20.glClearColor(1, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		batcher.setProjectionMatrix(cam.combined);
		
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(AssetLoader.backgroundRegion, 0, 0 ,width, height);
		batcher.end();
		
		batcher.enableBlending();
		batcher.begin();
		//batcher.draw(AssetLoader.playRegion, playBounds.getX(), playBounds.getY(),
		//		playBounds.getWidth(),playBounds.getHeight());

		//Titie
		AssetLoader.header.draw(batcher, "Spell", width/2-165, 300);
		AssetLoader.header.draw(batcher, "Arena", width/2-165+50, 300-40);
		if(!((int)time%3 == 0)){ //Blink
			AssetLoader.playText.draw(batcher, "Tap screen to play", width/2-165+25, 150);
		}
		
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

	private String getRandomHexString(int numchars){
	      Random r = new Random();
	      StringBuffer sb = new StringBuffer();
	      while(sb.length() < numchars){
	          sb.append(Integer.toHexString(r.nextInt()));
	      }
	      return sb.toString().substring(0, numchars);
	  }
	
}
