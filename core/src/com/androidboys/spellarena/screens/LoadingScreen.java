package com.androidboys.spellarena.screens;

import com.androidboys.spellarena.helper.AssetLoader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingScreen implements Screen {

	private Game game;
	
	private int width;
	private int height;
	
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	
	private TextureRegion loadingScreen;
	
	public LoadingScreen(Game game, int i, int j) {
		this.game = game;
		
		this.width = i;
		this.height = j;
		
		cam = new OrthographicCamera(width,height);
		cam.position.set(width/2, height/2 , 0);
		
		initLoadingScreen();
		batcher = new SpriteBatch();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		
		update();
		draw();
		
	}
	
	private void draw(){
		Gdx.gl20.glClearColor(26/255f, 33/255f, 43/255f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		batcher.setProjectionMatrix(cam.combined);
		
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(loadingScreen, 
				width/2 - loadingScreen.getRegionWidth()*.25f, 
				height/2 - loadingScreen.getRegionHeight()*.25f ,
				loadingScreen.getRegionWidth()*.5f, 
				loadingScreen.getRegionHeight()*.5f);
		batcher.end();
	}
	
	private void update(){
		if(Gdx.input.justTouched()){
			game.setScreen(new MainMenuScreen(game,480,320));
			return;
		}
	}

	private void initLoadingScreen(){
		loadingScreen = new TextureRegion(new Texture(Gdx.files.internal("images/splash.jpeg")));
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

}
