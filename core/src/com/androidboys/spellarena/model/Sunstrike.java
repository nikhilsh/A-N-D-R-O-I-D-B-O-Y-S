package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Rectangle;

public class Sunstrike extends GameObject {
	private Rectangle rectangle = new Rectangle();

	public Sunstrike(float x, float y, String playerName) {
		super(x, y, playerName);
		rectangle.set(x-100, y-100, 200, 200);
	}

	@Override
	public Rectangle getRectangle() {
		// TODO Auto-generated method stub
		return rectangle;
	}

}
