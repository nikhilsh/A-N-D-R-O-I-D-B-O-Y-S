package com.androidboys.spellarena.gameworld;

import com.androidboys.spellarena.model.Bob;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {

	public static final float WORLD_WIDTH = 48;
	public static final float WORLD_HEIGHT = 27;

	private World physicsWorld;
	
	private final Bob local_bob;
	private final Bob enemy_bob;
	
	public GameWorld(){
		this.local_bob = new Bob(810, 540, false);
		this.enemy_bob = new Bob(810, 540, true);
	}
	
	public Bob getBob(){
		return local_bob;
	}

	public void update(float delta) {
		local_bob.update(delta);
		enemy_bob.update(delta);
		local_bob.sendLocation(0);
	}

	public void updateEnemy(float x, float y, float vx, float vy, int state) {
		enemy_bob.setVelocity(vx, vy);
		enemy_bob.setPosition(x, y);
	}

	public Bob getEnemy() {
		return enemy_bob;
	}
}
