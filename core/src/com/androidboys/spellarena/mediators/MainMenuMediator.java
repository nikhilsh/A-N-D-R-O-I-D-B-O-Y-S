package com.androidboys.spellarena.mediators;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.view.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MainMenuMediator extends Mediator{

	private MainMenuScreen mainMenuScreen;
	
	public MainMenuMediator(SpellArena game){
		super(game);
	}

	@Override
	public Screen createScreen() {
		this.screen = new MainMenuScreen(game, this);
		this.mainMenuScreen = (MainMenuScreen) screen;
		return screen;
	}
	
	@Override
	protected void onScreenShow(){
		super.onScreenShow();
	}
	
}
