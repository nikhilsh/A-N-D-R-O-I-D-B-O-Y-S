package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//Laser object
public class Laser extends GameObject {

	public Laser(float x, float y, Direction direction, float width, float height, String playerName) {
		super(x, y, playerName);
		switch(direction){
		case EAST:
			break;
		case NORTH:
			break;
		case NORTHEAST:
			break;
		case NORTHWEST:
			break;
		case SOUTH:
			break;
		case SOUTHEAST:
			break;
		case SOUTHWEST:
			break;
		case WEST:
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
