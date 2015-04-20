package com.androidboys.spellarena.model;

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
		return new Rectangle(getPosition().x-25,getPosition().y-25,50f,50f);
	}
	
	@Override
	public String toString() {
		return "Tornado created by "+getUsername();
	}
}
