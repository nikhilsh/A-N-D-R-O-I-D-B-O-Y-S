package com.androidboys.spellarena.game;


import com.androidboys.spellarena.ggps.ActionResolver;
import com.androidboys.spellarena.ggps.IGoogleServices;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.screens.MainMenuScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpellArena extends Game {
	
	FPSLogger fps;
	
	public static IGoogleServices googleServices;
	
	private boolean isSignedIn;
	
	public ActionResolver actionResolver;	
	
	public SpellArena(ActionResolver actionResolver) {
		super();
		this.actionResolver = actionResolver;
	}

	@Override
	public void create () {
		//setScreen(new LoadingScreen(this ,480, 320));
		AssetLoader.load();
		setScreen(new MainMenuScreen(this, 480, 320));
		fps = new FPSLogger();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	public void onSignedIn(boolean isSignedIn) {
		this.isSignedIn = isSignedIn;
	}

	public boolean isSignedIn() {
		return isSignedIn;
	}

}
