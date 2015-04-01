package com.androidboys.spellarena.game;

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
	
	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainMenuScreen(this,480,320));
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
}
