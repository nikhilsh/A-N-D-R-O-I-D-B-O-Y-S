package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Sword extends GameObject{

	private static final float ROTATIONAL_SPEED = 300f;
	private static final float SPIRAL_SPEED = 50f;
	private static final float ANGULAR_VELOCITY = 3f;
	private static final String TAG = "Sword";
	
	private float radius = 1;
	private float rotation;
	
	/**
	 * Instantiates a new sword.
	 *
	 * @param x the x
	 * @param y the y
	 * @param playerName the player name
	 * @param rotation the rotation
	 */
	public Sword(float x, float y, String playerName, float rotation) {
		super(x, y, playerName);
		this.rotation = rotation;
		this.getPosition().add(new Vector2(100, 0).rotate((float) Math.toDegrees(this.rotation)));
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getPosition().x-50,getPosition().y-50,100f,100f);
	}
	
	//Sword will rotate with an angular velocity of 3
	@Override
	public void update(float delta) {
		this.radius += 2*delta;
		this.rotation += ANGULAR_VELOCITY*delta;
		Vector2 parallelVector = new Vector2((float)Math.cos(rotation),
				(float) Math.sin(rotation));
		Vector2 perpendicularVector = new Vector2(parallelVector);
		perpendicularVector.rotate(90);
		parallelVector.scl(radius);
		perpendicularVector.scl(ROTATIONAL_SPEED);
		parallelVector.scl(SPIRAL_SPEED);
		Vector2 finalVelocity = parallelVector.add(perpendicularVector);
		this.setVelocity(finalVelocity);
		
		super.update(delta);
	}
}
