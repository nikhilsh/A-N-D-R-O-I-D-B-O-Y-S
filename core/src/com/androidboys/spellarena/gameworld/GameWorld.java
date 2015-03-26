package com.androidboys.spellarena.gameworld;

import com.androidboys.spellarena.model.Bob;

public class GameWorld {

	public static final float WORLD_WIDTH = 48;
	public static final float WORLD_HEIGHT = 27;

	
	private final Bob local_bob;
	
	public GameWorld(){
		this.local_bob = new Bob(810, 540);
	}
	
	public Bob getBob(){
		return local_bob;
	}

	public void update(float delta) {
		local_bob.update(delta);
	}
}
