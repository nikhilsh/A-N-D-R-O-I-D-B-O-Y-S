package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;

//Laser object
public class Laser extends GameObject {
	
	private Bob bob;
	private Rectangle rectangle;

	public Laser(float x, float y, String playerName, Bob bob) {
		super(x, y, playerName);
		this.bob = bob;
		this.rectangle = new Rectangle(bob.getPosition().x+12.5f,bob.getPosition().y,200f,50f);
	}

	@Override
	public void update(float delta) {
		switch(bob.getDirection()){
		case EAST:
			rectangle.set(bob.getPosition().x+12.5f,bob.getPosition().y-5f,210f,35f);
			break;
		case NORTH:
			rectangle.set(bob.getPosition().x-5f,bob.getPosition().y+28f,35f,210f);
			break;
		case NORTHEAST:
			rectangle.set(bob.getPosition().x+15f,bob.getPosition().y,200f,50f);
			break;
		case NORTHWEST:
			rectangle.set(bob.getPosition().x+7.25f,bob.getPosition().y+22,200f,50f);
			break;
		case SOUTH:
			rectangle.set(bob.getPosition().x-5f,bob.getPosition().y-190f,35f,200f);
			break;
		case SOUTHEAST:
			rectangle.set(bob.getPosition().x+12.5f,bob.getPosition().y+8f,200f,50f);
			break;
		case SOUTHWEST:
			rectangle.set(bob.getPosition().x+12.5f,bob.getPosition().y+28f,200f,50f);
			break;
		case WEST:
			rectangle.set(bob.getPosition().x-200f,bob.getPosition().y-7f,190f,35f);
			break;
		default:
			rectangle.set(bob.getPosition().x+12.5f,bob.getPosition().y,200f,50f);
			break;
		}
	}
	
	@Override
	public Rectangle getRectangle() {
		return rectangle;
	}

}
