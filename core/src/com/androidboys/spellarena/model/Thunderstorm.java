package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Thunderstorm extends GameObject{

	public Thunderstorm(float x, float y, Direction direction, String playerName) {
		super(x, y, playerName);
		switch(direction){
			case EAST:
				this.getPosition().add(100, 0);
				break;
			case NORTH:
				this.getPosition().add(0, 100);
				break;
			case NORTHEAST:
				this.getPosition().add(71, 71);
				break;
			case NORTHWEST:
				this.getPosition().add(-71, 71);
				break;
			case SOUTH:
				this.getPosition().add(0, -100);
				break;
			case SOUTHEAST:
				this.getPosition().add(71, -71);
				break;
			case SOUTHWEST:
				this.getPosition().add(-71, -71);
				break;
			case WEST:
				this.getPosition().add(-100, 0);
				break;
			default:
				break;
		}
	}

	
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x,
				getPosition().y,
				50,50);
	}

}
