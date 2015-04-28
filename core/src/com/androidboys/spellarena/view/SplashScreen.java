package com.androidboys.spellarena.view;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.AudioManager;
import com.androidboys.spellarena.helper.StyleLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashScreen.
 */
public class SplashScreen implements Screen{

	/** The game. */
	private SpellArena game;
	
	/** The splash texture. */
	private Texture splashTexture = new Texture(Gdx.files.internal("images/androidboys.png"));
	
	/** The splash image. */
	private Image splashImage = new Image(splashTexture);
	
	/** The stage. */
	private Stage stage = new Stage(new ScreenViewport(new OrthographicCamera()));
	
	/** The animation done. */
	private boolean animationDone = false;
	
	/**
	 * Instantiates a new splash screen.
	 *
	 * @param game the game
	 */
	public SplashScreen(SpellArena game) {
		this.game = game;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		AssetLoader.queueLoading();
		AudioManager.initialize();
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

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		if(AssetLoader.update()&&AudioManager.update()){
			if(animationDone){
				AssetLoader.setMainMenuResources();
				StyleLoader.prepareStyles();
				game.addScreen(new MainMenuScreen(game));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		dispose();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		splashTexture.dispose();
		stage.dispose();
	}
	
}
