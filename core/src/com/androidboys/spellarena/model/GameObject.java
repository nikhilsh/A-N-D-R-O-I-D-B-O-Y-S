package com.androidboys.spellarena.model;


import java.util.ArrayList;

import org.json.JSONObject;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

public abstract class GameObject {
	
	//Speed

	private Vector2 position;
	private Vector2 velocity;
	
	/**
	 * Create a new Bob instance.
	 * @param i,j		initial position
	 * @param isEnemy	whether it is enemy
	 */
	public GameObject(float x, float y) {
		this.position = new Vector2(x,y);
		this.setVelocity(new Vector2());		
	}

	/**
	 * Get the position of Bob.
	 * @return Bob's position
	 */
	public Vector2 getPosition(){
		return position;
	}

	/**
	 * Update Bob's position and state.
	 */
	public void update(float delta){
		this.position.add((this.getVelocity().cpy()).scl(delta));
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public abstract Rectangle getRectangle();
}
