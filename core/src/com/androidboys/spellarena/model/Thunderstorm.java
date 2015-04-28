package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;

public class Thunderstorm extends GameObject{

	/**
	 * Instantiates a new thunderstorm.
	 *
	 * @param x the x
	 * @param y the y
	 * @param direction the direction
	 * @param playerName the player name
	 */
	public Thunderstorm(float x, float y, Direction direction, String playerName) {
		super(x, y, playerName);
		switch(direction){
			case EAST:
				this.getPosition().add(300, 25);
				break;
			case NORTH:
				this.getPosition().add(15, 300);
				break;
			case NORTHEAST:
				this.getPosition().add(212, 212);
				break;
			case NORTHWEST:
				this.getPosition().add(-187, 212);
				break;
			case SOUTH:
				this.getPosition().add(15, -275);
				break;
			case SOUTHEAST:
				this.getPosition().add(212, -187);
				break;
			case SOUTHWEST:
				this.getPosition().add(-187, -187);
				break;
			case WEST:
				this.getPosition().add(-275, 25);
				break;
			default:
				break;
		}
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-100,
				getPosition().y-75,
				200,112);
	}

}
