package com.androidboys.spellarena.model;

import com.androidboys.spellarena.model.Bob.Direction;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//Tornado object
public class Projectile extends GameObject {

	private static final float MAX_SPEED = 300f;
	
	public Projectile(float x, float y, float rotation, String playerName) {
		super(x, y, playerName);
		this.getPosition().add(15, 25);
		Vector2 vel = new Vector2(MAX_SPEED,0).rotate(rotation);
		this.setVelocity(vel);
		}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-50,getPosition().y-50,150f,150f);
	}
	
	@Override
	public String toString() {
		return "Tornado created by "+getUsername();
	}
}
