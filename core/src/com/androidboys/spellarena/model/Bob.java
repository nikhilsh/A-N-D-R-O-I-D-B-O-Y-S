package com.androidboys.spellarena.model;

import org.json.JSONObject;

import appwarp.WarpController;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class Bob {

	public enum Direction{
		NORTH, 
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST;
	}
	
	public enum State{
		ALIVE,
		RUNNING,
		DEAD;
	}
	
	private static final float MAX_SPEED = 500f;
	
	private Vector2 position;
	private Vector2 velocity;
	private Direction direction;
	
	private boolean isEnemy;
	private boolean needsUpdate;
	
	private Touchpad touchpad;

	private State state;
	
	public Bob(int i, int j, boolean isEnemy) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2();		
		this.isEnemy = isEnemy;
		this.direction = Direction.SOUTH;
		this.setState(State.ALIVE);
	}

	public Vector2 getPosition(){
		return position;
	}

	public void onClick() {
		position.add(1, 0);
	}
	
	public void update(float delta){
		this.position.add(this.velocity.cpy().scl(delta));
		Vector2 newV = null;
		if(!isEnemy){
			float touchX = touchpad.getKnobPercentX();
			float touchY = touchpad.getKnobPercentY();
			if(touchX > 0.5){
				if(touchY > 0.5){
					this.direction = Direction.NORTHEAST;
					newV = new Vector2(MAX_SPEED, MAX_SPEED);
					
				} else if (touchY < -0.5){
					this.direction = Direction.SOUTHEAST;
					newV = new Vector2(MAX_SPEED, -MAX_SPEED);
					
				} else {
					this.direction = Direction.EAST;
					newV = new Vector2(MAX_SPEED,	0);
				}
			}else if (touchX < -0.5){
				if(touchY > 0.5){
					this.direction = Direction.NORTHWEST;
					newV = new Vector2(-MAX_SPEED, MAX_SPEED);
				} else if (touchY < -0.5){
					this.direction = Direction.SOUTHWEST;
					newV = new Vector2(-MAX_SPEED, -MAX_SPEED);
				} else {
					this.direction = Direction.WEST;
					newV = new Vector2(-MAX_SPEED, 0);
				}
			} else {
				if(touchY > 0.5){
					this.direction = Direction.NORTH;
					newV = new Vector2(0, MAX_SPEED);
				} else if(touchY < -0.5) {
					this.direction = Direction.SOUTH;
					newV = new Vector2(0, -MAX_SPEED);
				} else {
					newV = new Vector2(0,0);
				}
			}
			if(!newV.equals(velocity)){
				needsUpdate = true;
				this.setVelocity(newV.x, newV.y);
			}
			//this.scaleAndSetVelocity(touchX, touchY);
			if(this.velocity.isZero()){
				this.setState(State.ALIVE);
			} else {
				this.setState(State.RUNNING);
			}
		}
		/*
		if(!isEnemy){
			float touchX = touchpad.getKnobPercentX();
			float touchY = touchpad.getKnobPercentY();
			if(Math.abs(touchX)>Math.abs(touchY)){	
				this.scaleAndSetVelocity(touchX,0);
			} 
			else {
				this.scaleAndSetVelocity(0, touchY);
			}
		}*/

	}

	public Vector2 getVelocity(){
		return velocity;
	}
	
	public Direction getDirection(){
		return direction;
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
	
	public void sendLocation(int state){
		if(needsUpdate){
			needsUpdate = false;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
}
