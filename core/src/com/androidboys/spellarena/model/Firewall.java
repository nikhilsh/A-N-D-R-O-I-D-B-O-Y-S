package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Firewall extends GameObject{

	/**
	 * Instantiates a new firewall.
	 *
	 * @param x the x
	 * @param y the y
	 * @param direction the direction
	 * @param playerName the player name
	 */
	public Firewall(float x, float y, Direction direction, String playerName) {
		super(x, y, playerName);
		float rotation = 0;
		switch(direction){
			case EAST:
				rotation = 0;
				break;
			case NORTH:
				rotation = 90;
				break;
			case NORTHEAST:
				rotation = 45;
				break;
			case NORTHWEST:
				rotation = 135;
				break;
			case SOUTH:
				rotation = 270;
				break;
			case SOUTHEAST:
				rotation = 315;
				break;
			case SOUTHWEST:
				rotation = 225;
				break;
			case WEST:
				rotation = 180;
				break;
			default:
				break;
		}
		Vector2 velocity = new Vector2(85,0).rotate(rotation);
		setVelocity(velocity);
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x, getPosition().y, 50, 50);
	}
}
