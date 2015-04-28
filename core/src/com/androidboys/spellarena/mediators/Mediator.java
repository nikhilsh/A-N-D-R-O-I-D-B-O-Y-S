package com.androidboys.spellarena.mediators;

import com.androidboys.spellarena.game.SpellArena;
import com.badlogic.gdx.Screen;

public abstract class Mediator {

	protected SpellArena game;
	protected Screen screen;
	
	/**
	 * Instantiates a new mediator.
	 *
	 * @param game the game
	 */
	public Mediator(SpellArena game){
		this.game = game;
	}
	
	/**
	 * Creates the screen.
	 *
	 * @return the screen
	 */
	public abstract Screen createScreen();
	
	/**
	 * Show game screen.
	 */
	public final void onScreenShowInternal(){
		onScreenShow();
	}

	/**
	 * On screen show.
	 */
	protected void onScreenShow(){};
}
