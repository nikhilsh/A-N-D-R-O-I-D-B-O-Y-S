package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Rectangle;

public class Laser extends GameObject {

	public Laser(float x, float y, String playerName) {
		super(x, y, playerName);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-50,getPosition().y-50,150f,150f);
	}

}
