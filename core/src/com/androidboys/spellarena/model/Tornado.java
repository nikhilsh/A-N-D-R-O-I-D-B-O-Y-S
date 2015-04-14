package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tornado extends GameObject {

	private static final float MAX_SPEED = 300f;
	
	public Tornado(float x, float y, Direction direction, String playerName) {
		super(x, y, playerName);
		switch(direction){
			case EAST:
				this.getPosition().add(25, 0);
				this.setVelocity(new Vector2(MAX_SPEED,0));
				break;
			case NORTH:
				this.getPosition().add(0, 25);
				this.setVelocity(new Vector2(0, MAX_SPEED));
				break;
			case NORTHEAST:
				this.getPosition().add(18, 18);
				this.setVelocity(new Vector2(MAX_SPEED, MAX_SPEED));
				break;
			case NORTHWEST:
				this.getPosition().add(-18, 18);
				this.setVelocity(new Vector2(-MAX_SPEED, MAX_SPEED));
				break;
			case SOUTH:
				this.getPosition().add(0, -25);
				this.setVelocity(new Vector2(0, -MAX_SPEED));
				break;
			case SOUTHEAST:
				this.getPosition().add(18, -18);
				this.setVelocity(new Vector2(MAX_SPEED, -MAX_SPEED));
				break;
			case SOUTHWEST:
				this.getPosition().add(-18, -18);
				this.setVelocity(new Vector2(-MAX_SPEED, -MAX_SPEED));
				break;
			case WEST:
				this.getPosition().add(-18, 0);
				this.setVelocity(new Vector2(-MAX_SPEED,0));
				break;
			default:
				break;
		}

	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-50,getPosition().y-50,150f,150f);
	}

	
}
