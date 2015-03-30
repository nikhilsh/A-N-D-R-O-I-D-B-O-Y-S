package com.androidboys.spellarena.model;

import org.json.JSONObject;

import appwarp.WarpController;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class Bob {

	private static final float MAX_SPEED = 500f;
	
	private Vector2 position;
	private Vector2 velocity;
	
	private boolean isEnemy;
	
	private Touchpad touchpad;
	
	public Bob(int i, int j, boolean isEnemy) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2();		
		this.isEnemy = isEnemy;
	}

	public Vector2 getPosition(){
		return position;
	}

	public void onClick() {
		position.add(1, 0);
	}
	
	public void update(float delta){
		if(!isEnemy){
			float touchX = touchpad.getKnobPercentX();
			float touchY = touchpad.getKnobPercentY();
			if(Math.abs(touchX)>Math.abs(touchY)){	
				this.scaleAndSetVelocity(touchX,0);
			} 
			else {
				this.scaleAndSetVelocity(0, touchY);
			}
		}
		this.position.add(this.velocity.cpy().scl(delta));
	}

	public Vector2 getVelocity(){
		return velocity;
	}
	
	public void setVelocity(float vx, float vy){
		velocity = new Vector2(vx,vy);
	}
	
	private void scaleAndSetVelocity(float touchX, float touchY) {
		velocity = new Vector2(touchX*MAX_SPEED, touchY*MAX_SPEED);
	}
	
	public void setTouchpad(Touchpad touchpad){
		this.touchpad = touchpad;
	}
	
	int n = 0;
	public void sendLocation(int state){
		n++;
		if(n%10 == 0){
			try {
				JSONObject data = new JSONObject();
				data.put("x", position.x);
				data.put("y", position.y);
				data.put("vx", velocity.x);
				data.put("vy", velocity.y);
				data.put("state", state);
				WarpController.getInstance().sendGameUpdate(data.toString());
			} catch (Exception e) {
			}
		}
	}

	public void setPosition(float x, float y) {
		this.position = new Vector2(x,y);
	}
}
