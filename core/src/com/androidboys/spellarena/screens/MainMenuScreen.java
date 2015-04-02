package com.androidboys.spellarena.screens;

import java.util.Random;

import appwarp.WarpController;

import com.androidboys.spellarena.game.SpellArena;
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

	final static int START_SCREEN = 0;
	final static int FIND_GAME_SCREEN = 1;
	
	private final static String START_GAME_PROMPT = "Tap screen to sign in";
	
	SpellArena game;
	
	float time;
	
	float width;
	float height;
	
	OrthographicCamera cam;
	SpriteBatch batcher;
	
	int state;
	String prompt;
	
	Rectangle quickGameBound;
	Rectangle playWithFriendsBound;
	
	public MainMenuScreen(SpellArena game, float width, float height) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.time = 0;
		this.state = START_SCREEN;
		this.prompt = START_GAME_PROMPT;

		cam = new OrthographicCamera(width,height);
		cam.position.set(width/2, height/2 , 0);
		
		batcher = new SpriteBatch();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void render(float delta) {
		time+=delta;
		update();
		draw();
	}

	private void update() {
		checkSignedIn();
		switch(state){
			case START_SCREEN:
				if(Gdx.input.justTouched()){
					game.actionResolver.signIn();					
				}
		}
//		if(Gdx.input.justTouched()){
//			//WarpController.getInstance().startApp(getRandomHexString(10));
//			//game.setScreen(new StartMultiplayerScreen(game,width,height));
//			game.setScreen(new GameScreen(game));
//			return;
//		}
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

		AssetLoader.header.draw(batcher, "Spell", width/2-165, 300);
		AssetLoader.header.draw(batcher, "Arena", width/2-165+50, 300-40);
		switch(state){
			case START_SCREEN:
				if(!((int)time%3 == 0)){
					AssetLoader.playText.draw(batcher, prompt, width/2-165+25, 150);
				}
				break;
			case FIND_GAME_SCREEN:
				AssetLoader.playText.draw(batcher, "find quick game", width/2-165+25, 180);
				AssetLoader.playText.draw(batcher, "play with friends", width/2-165+25, 120);
				break;
		}
		
		batcher.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

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

	public void checkSignedIn() {
		if(game.isSignedIn()){
			state = FIND_GAME_SCREEN;
		} else {
			state = START_SCREEN;
		}
	}
	
}
