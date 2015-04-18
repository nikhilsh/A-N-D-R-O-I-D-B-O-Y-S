package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//Tornado object
public class Boomerang extends GameObject {

	private static final float MAX_SPEED = 300f;
	
	private boolean isReturning;
	private Vector2 destination;
	private Bob owner;
	
	public Boomerang(float x, float y, Direction direction, String playerName, Bob bob) {
		super(x, y, playerName);
		this.owner = bob;
		isReturning = false;
		switch(direction){
		case EAST:
			this.destination = getPosition().cpy().add(750,0);
			setVelocity(new Vector2(MAX_SPEED,0));
			break;
		case NORTH:
			this.destination = getPosition().cpy().add(0,750);
			setVelocity(new Vector2(0,MAX_SPEED));
			break;
		case NORTHEAST:
			this.destination = getPosition().cpy().add(530,530);
			setVelocity(new Vector2(MAX_SPEED,MAX_SPEED));
			break;
		case NORTHWEST:
			this.destination = getPosition().cpy().add(-530,530);
			setVelocity(new Vector2(-MAX_SPEED,MAX_SPEED));
			break;
		case SOUTH:
			this.destination = getPosition().cpy().add(0,-750);
			setVelocity(new Vector2(0,-MAX_SPEED));
			break;
		case SOUTHEAST:
			this.destination = getPosition().cpy().add(530,-530);
			setVelocity(new Vector2(MAX_SPEED,-MAX_SPEED));
			break;
		case SOUTHWEST:
			this.destination = getPosition().cpy().add(-530,-530);
			setVelocity(new Vector2(-MAX_SPEED,-MAX_SPEED));
			break;
		case WEST:
			this.destination = getPosition().cpy().add(-750,0);
			setVelocity(new Vector2(-MAX_SPEED,0));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void update(float delta) {
		if(!isReturning){
			if(getRectangle().contains(destination)){
				isReturning = true;
			}
		} else {
			Vector2 newVelocity = 
					owner.getPosition().cpy().add(getPosition().cpy().scl(-1)).limit(1).scl(MAX_SPEED);
			setVelocity(newVelocity);
		}
		super.update(delta);
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-35,getPosition().y-15,105f,90f);
	}
	
	@Override
	public String toString() {
		return "Boomerang created by "+getUsername();
	}

	public boolean checkFinished() {
		return owner.getbobRect().overlaps(getRectangle())&&isReturning;
	}

	public Vector2 getDestination() {
		return destination;
	}
}
