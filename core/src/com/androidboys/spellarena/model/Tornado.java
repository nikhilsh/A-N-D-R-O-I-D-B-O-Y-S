package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Vector2;

public class Tornado extends GameObject {

	private static final float MAX_SPEED = 300f;
	
	public Tornado(float x, float y, Direction direction) {
		super(x, y);
		switch(direction){
			case EAST:
				this.setVelocity(new Vector2(MAX_SPEED,0));
				break;
			case NORTH:
				this.setVelocity(new Vector2(0, MAX_SPEED));
				break;
			case NORTHEAST:
				this.setVelocity(new Vector2(MAX_SPEED, MAX_SPEED));
				break;
			case NORTHWEST:
				this.setVelocity(new Vector2(-MAX_SPEED, MAX_SPEED));
				break;
			case SOUTH:
				this.setVelocity(new Vector2(0, -MAX_SPEED));
				break;
			case SOUTHEAST:
				this.setVelocity(new Vector2(MAX_SPEED, -MAX_SPEED));
				break;
			case SOUTHWEST:
				this.setVelocity(new Vector2(-MAX_SPEED, -MAX_SPEED));
				break;
			case WEST:
				this.setVelocity(new Vector2(-MAX_SPEED,0));
				break;
			default:
				break;
		}

	}

	
}
