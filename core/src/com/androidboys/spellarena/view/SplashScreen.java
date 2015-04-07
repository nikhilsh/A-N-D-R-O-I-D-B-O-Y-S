package com.androidboys.spellarena.view;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.StyleLoader;
import com.androidboys.spellarena.mediators.MainMenuMediator;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SplashScreen implements Screen{

	private SpellArena game;
	
	private Texture splashTexture = new Texture(Gdx.files.internal("images/androidboys.png"));
	private Image splashImage = new Image(splashTexture);
	private Stage stage = new Stage(new ScreenViewport(new OrthographicCamera()));
	
	private boolean animationDone = false;
	
	public SplashScreen(SpellArena game) {
		this.game = game;
	}

	@Override
	public void show() {
		AssetLoader.queueLoading();
		splashImage.setSize(stage.getWidth(), stage.getHeight());
		stage.addActor(splashImage);
		splashImage.addAction(Actions.sequence(Actions.alpha(0),
				Actions.fadeIn(0.75f),
				Actions.delay(1.5f),
				Actions.run(new Runnable() {
					
					@Override
					public void run() {
						animationDone = true;
					}
				})));

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		
		if(AssetLoader.update()){
			if(animationDone){
				AssetLoader.setMainMenuResources();
				StyleLoader.prepareStyles();
				MainMenuMediator mediator = new MainMenuMediator(game);
				game.addScreen(mediator.createScreen());
			}
		}
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
		dispose();
	}

	@Override
	public void dispose() {
		splashTexture.dispose();
		stage.dispose();
	}
	
}
