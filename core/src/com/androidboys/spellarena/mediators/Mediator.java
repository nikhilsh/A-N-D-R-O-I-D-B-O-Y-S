package com.androidboys.spellarena.mediators;

import com.androidboys.spellarena.game.SpellArena;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class Mediator {

	protected SpellArena game;
	protected Screen screen;
	
	public Mediator(SpellArena game){
		this.game = game;
	}
	
	public abstract Screen createScreen();
	
	public final void onScreenShowInternal(){
		
		onScreenShow();
		
	}

	protected void onScreenShow(){};
}
