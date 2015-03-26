package com.androidboys.spellarena.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class Bob {

	private static final float MAX_SPEED = 500f;
	
	private Vector2 position;
	private Vector2 velocity;
	
	private Touchpad touchpad;
	
	public Bob(int i, int j) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2();
	}

	public Vector2 getPosition(){
		return position;
	}

	public void onClick() {
		position.add(1, 0);
	}
	
	public void update(float delta){
		float touchX = touchpad.getKnobPercentX();
		float touchY = touchpad.getKnobPercentY();
		if(Math.abs(touchX)>Math.abs(touchY)){	
			this.setVelocity(touchX,0);
		} 
		else {
			this.setVelocity(0, touchY);
		}
		this.position.add(this.velocity.cpy().scl(delta));
	}

	public void setVelocity(float touchX, float touchY) {
		velocity = new Vector2(touchX*MAX_SPEED, touchY*MAX_SPEED);
	}
	
	public void setTouchpad(Touchpad touchpad){
		this.touchpad = touchpad;
	}
}
