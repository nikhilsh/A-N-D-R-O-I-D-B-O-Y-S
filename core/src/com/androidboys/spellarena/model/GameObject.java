package com.androidboys.spellarena.model;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
	
	//Speed

	private Vector2 position;
	private Vector2 velocity;
	private String playerName;
	
	/**
	 * Create a new Bob instance.
	 * @param i,j		initial position
	 * @param isEnemy	whether it is enemy
	 */
	public GameObject(float x, float y, String playerName) {
		this.position = new Vector2(x,y);
		this.setVelocity(new Vector2());
		this.playerName = playerName;
	}

	/**
	 * Get the position of Bob.
	 * @return Bob's position
	 */
	public Vector2 getPosition(){
		return position;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
	}

	public void update(float delta){
		this.position.add((this.getVelocity().cpy()).scl(delta));
	}

	/**
	 * Get the velocity of Bob
	 * @return Bob's velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * Set the velocity of Bob
	 * @param velocity: Bob's new velocity
	 */
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Get the username (readonly)
	 * @return username
	 */
	public String getUsername(){
		return playerName;
	}

	public abstract Rectangle getRectangle();
}
