package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Rectangle;

public class DummyBlinkObject extends GameObject {

	public DummyBlinkObject(float x, float y, String playerName) {
		super(x, y, playerName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x,getPosition().y,100f,100f);
	}
}
