package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;

public class Laser extends GameObject {
	
	private Direction direction;

	public Laser(float x, float y, Direction direction,String playerName) {
		super(x, y, playerName);
		this.direction = direction;
	}

	@Override
	public Rectangle getRectangle() {
		switch(direction){
		case EAST:
			return new Rectangle(getPosition().x+25f,getPosition().y,200f,50f);
		case NORTH:
			return new Rectangle(getPosition().x+25f,getPosition().y+42,200f,50f);
		case NORTHEAST:
			return new Rectangle(getPosition().x+30f,getPosition().y,200f,50f);
		case NORTHWEST:
			return new Rectangle(getPosition().x+12.5f,getPosition().y+22,200f,50f);
		case SOUTH:
			return new Rectangle(getPosition().x,getPosition().y+12f,200f,50f);
		case SOUTHEAST:
			return new Rectangle(getPosition().x+5f,getPosition().y+8f,200f,50f);
		case SOUTHWEST:
			return new Rectangle(getPosition().x+5f,getPosition().y+28f,200f,50f);
		case WEST:
			return new Rectangle(getPosition().x,getPosition().y+22f,200f,50f);
		default:
			return new Rectangle(getPosition().x+25f,getPosition().y,200f,50f);
		}

	}

}
