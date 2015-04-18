package com.androidboys.spellarena.model;

import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

//Laser object
public class Laser extends GameObject {
	
	private Bob bob;
	private Rectangle rectangle;
	private ArrayList<Rectangle> rectangleArray = new ArrayList<Rectangle>();

	public Laser(float x, float y, String playerName, Bob bob) {
		super(x, y, playerName);
		this.bob = bob;
		this.rectangle = new Rectangle();
	}

	@Override
	public void update(float delta) {
		switch(bob.getDirection()){
		case EAST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x+12.5f,bob.getPosition().y-5,210f,35f));
			break;
		case NORTH:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x-5,bob.getPosition().y+28f,35f,210f));
			break;
		case NORTHEAST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x+20,bob.getPosition().y+5,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+35,bob.getPosition().y+17.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+52,bob.getPosition().y+42.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+70,bob.getPosition().y+65.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+90,bob.getPosition().y+87.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+110,bob.getPosition().y+112.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+130,bob.getPosition().y+130f,20f,25f));
			break;
		case NORTHWEST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x-20,bob.getPosition().y+5,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-35,bob.getPosition().y+17.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-52,bob.getPosition().y+42.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-70,bob.getPosition().y+65.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-90,bob.getPosition().y+87.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-110,bob.getPosition().y+112.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-130,bob.getPosition().y+130f,20f,25f));
			break;
		case SOUTH:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x-5,bob.getPosition().y-190f,35f,200f));
			break;
		case SOUTHEAST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x+20,bob.getPosition().y-10,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+35,bob.getPosition().y-25f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+52,bob.getPosition().y-42.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+70,bob.getPosition().y-65.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+90,bob.getPosition().y-84f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+110,bob.getPosition().y-107.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x+130,bob.getPosition().y-120f,20f,25f));
			break;
		case SOUTHWEST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x-20,bob.getPosition().y-10,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-35,bob.getPosition().y-25f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-52,bob.getPosition().y-42.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-70,bob.getPosition().y-65.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-90,bob.getPosition().y-87.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-110,bob.getPosition().y-112.5f,20f,25f));
			rectangleArray.add(new Rectangle(bob.getPosition().x-130,bob.getPosition().y-130f,20f,25f));
			break;
		case WEST:
			rectangleArray.clear();
			rectangleArray.add(new Rectangle(bob.getPosition().x-200,bob.getPosition().y-7f,190f,35f));
			break;
		default:
			break;
		}
	}
	
	@Override
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public ArrayList<Rectangle> getRectangleArray(){
		return rectangleArray;
	}

}
