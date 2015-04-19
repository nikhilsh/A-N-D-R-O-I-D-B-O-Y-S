package com.androidboys.spellarena.game;

import java.util.Stack;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.AudioManager;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.appwarp.AppWarpClient;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.androidboys.spellarena.view.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

public class SpellArena extends Game {
	
	private static final String TAG = "SpellArena";
	
	FPSLogger fps;
	final private Stack<Screen> screens = new Stack<Screen>();	
	//Three types of screens
    public static enum ScreenType {
        SPLASH, PLAY, LOBBY
    }
	
	private NetworkInterface client = new AppWarpClient(UserSession.getInstance().getUserName());

	private Screen topScreen;
	
	/**
	 * Called when the Application is first created.
	 * Inherited from ApplicationListener
	 */
	@Override
	public void create () {
		setScreen(new SplashScreen(this));
		Gdx.input.setCatchBackKey(true);
		fps = new FPSLogger();
	}

	/**
	 * Called when the Application should render itself.
	 * Inherited from ApplicationListener
	 */
	@Override
	public void render () {
		super.render();
	}
	
	/**
	 * Called when the Application is destroyed.
	 * Inherited from ApplicationListener
	 */
	@Override
	public void dispose () {
		super.dispose();
		
	}
	
	/**
	 * Change screen
	 * @param screenType: new screen
	 */
	public void switchScreen(final ScreenType screenType) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                displayLoadingWidget();
                switch (screenType) {
                    case PLAY:
                    	AssetLoader.setGameResources();
                        GameScreenMediator mediator = new GameScreenMediator(SpellArena.this, client);
                        addScreen(mediator.createScreen());
                        break;

                    default:
                        break;
                }
            }
        });
    }
	
	/**
	 * Add a new screen
	 * @param screen
	 */
	public void addScreen(Screen screen) {
		if(screen == null){
			Gdx.app.log(TAG,"No screen found");
		}
		if(getTopScreen() != null){
			getTopScreen().hide();
		}
		screens.push(screen);
		this.topScreen = getTopScreen();
		displayTopScreen();
	}
	
	/**
	 * Display the screen on the top
	 */
	private void displayTopScreen() {
		setScreen(this.topScreen);
	}

	/**
	 * Get the topmost screen of the screen stack
	 * @return
	 */
	private Screen getTopScreen() {
		try{
			return screens.peek();
		} catch (Exception e){
			return null;
		}
	}

	private void displayLoadingWidget() {
		Screen screen = getScreen();
		if (screen instanceof GameScreen) {
			((GameScreen) screen).displayLoadingWidget();
		}
		
	}

	public NetworkInterface getClient(){
		return client;
	}

	public void backToPreviousScreen() {
		try{
			Screen top = screens.pop();
			top.hide();
			top.dispose(); //remove the topmost screen
			this.topScreen = getTopScreen();
			displayTopScreen();
		} catch (Exception e){
		}

	}

	public int getNumberScreens() {
		return screens.size();
	}
}
