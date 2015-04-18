package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Sunstrike extends GameObject {
	private Rectangle rectangle = new Rectangle();
	private float actualX;
	private float actualY;

	public Sunstrike(float x, float y, String playerName) {
		super(x, y, playerName);
		actualX = x;
		actualY = y;
		setPosition(new Vector2(-10025, -10025));
		rectangle.set(-10025, -10025, 100, 100);
	}
	
	@Override
	public void update(float delta) {
		new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						rectangle.set(actualX-50, actualY-50, 100, 100);
						setPosition(new Vector2(actualX, actualY));
		            }
		        }, 
		        2000 
		);
		super.update(delta);
	}

	@Override
	public Rectangle getRectangle() {
		// TODO Auto-generated method stub
		return rectangle;
	}

}
