package com.androidboys.spellarena.model;


import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import appwarp.WarpController;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

public class Bob {

	//Directions
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

	//States
	public static final int STATE_ALIVE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_DEAD = 2;
	public static final int STATE_INVULNERABLE = 3;

	//Speed
	private static final float MAX_SPEED = 500f;

	private Vector2 position;
	private Vector2 velocity;
	private Direction direction;
	private int lifeCount = 3;
	private float manaCount = 200;

	private boolean isEnemy;
	private boolean needsUpdate;

	private Touchpad touchpad;

	private int state;

	private Rectangle bobRect;

	private Array<Rectangle> tiles;

	/**
	 * Create a new Bob instance.
	 * @param i,j		initial position
	 * @param isEnemy	whether it is enemy
	 */
	public Bob(int i, int j, boolean isEnemy) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2();		
		this.isEnemy = isEnemy;
		this.direction = Direction.SOUTH;
		this.state = STATE_ALIVE;
		this.bobRect = new Rectangle();
		this.tiles = null;
	}

	/**
	 * Get the position of Bob.
	 * @return Bob's position
	 */
	public Vector2 getPosition(){
		return position;
	}

	public void onClick() {
		position.add(1, 0);
	}

	/**
	 * Update Bob's position and state.
	 */
	public void update(float delta){
		checkCollision(delta);
		updateVelocity();
		updateState();
	}

	private void updateState(){
		if(this.velocity.isZero()){
			this.state = STATE_ALIVE;
		} else {
			this.state = STATE_RUNNING;
		}
	}

	private void updateVelocity(){
		Vector2 newV = null;
		if(!isEnemy){
			incrementManaCount();
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
		} else {
			if(this.velocity.x > 0){
				if(this.velocity.y > 0){
					this.direction = Direction.NORTHEAST;
				} else if (this.velocity.y < 0){
					this.direction = Direction.SOUTHEAST;
				} else {
					this.direction = Direction.EAST;
				}
			} else if (this.velocity.x < 0){
				if(this.velocity.y > 0){
					this.direction = Direction.NORTHWEST;
				} else if (this.velocity.y < 0){
					this.direction = Direction.SOUTHWEST;
				} else {
					this.direction = Direction.WEST;
				}
			} else {
				if(this.velocity.y > 0){
					this.direction = Direction.NORTH;
				} else if (this.velocity.y < 0){
					this.direction = Direction.SOUTH;
				}
			}
		}
	}


	@Deprecated
	private void checkBoundaries(float delta){
		this.position.add(this.velocity.cpy().scl(delta));			
		//		System.out.println("Position: "+this.position);
		if(position.x > GameWorld.WORLD_BOUND_RIGHT){
			System.out.println("Hit Right");
			position.x = GameWorld.WORLD_BOUND_RIGHT;
		} else if (position.x < GameWorld.WORLD_BOUND_LEFT){
			System.out.println("Hit Left");
			position.x = GameWorld.WORLD_BOUND_LEFT;
		}
		if(position.y > GameWorld.WORLD_BOUND_TOP){
			System.out.println("Hit Top");
			position.y = GameWorld.WORLD_BOUND_TOP;
		} else if (position.y < GameWorld.WORLD_BOUND_BOTTOM){
			System.out.println("Hit Bottom");
			position.y = GameWorld.WORLD_BOUND_BOTTOM;
		}
	}

	private void checkCollision(float delta){
		Vector2 newPos = this.position.cpy().add(this.velocity.cpy().scl(delta));
		this.setRect(newPos);
		if(getTiles() != null){
			for(Rectangle tile: getTiles()){
				if(tile.overlaps(this.bobRect)){
					//					System.out.println("Overlap");
					switch(direction){
					case EAST:
						//						System.out.println("Colliding east");
						newPos.x = tile.x - 40;
						break;
					case NORTH:
						newPos.y = tile.y - 50;
						break;
					case NORTHEAST:
						float x = (newPos.x + 40) - tile.x;
						float y = (newPos.y + 50) - tile.y;
						if(x>y){
							newPos.x -= y;
							newPos.y -= y;
						} else {
							newPos.x -= x;
							newPos.y -= x;
						}
						break;
					case NORTHWEST:
						x = tile.x + tile.width - (newPos.x - 10);
						y = (newPos.y + 50) - tile.y;
						if(x>y){
							newPos.x += y;
							newPos.y -= y;
						} else {
							newPos.x += x;
							newPos.y -= x;
						}
						break;
					case SOUTH:
						newPos.y = tile.y + tile.height;	
						break;
					case SOUTHEAST:
						x = (newPos.x + 40) - tile.x;
						y = tile.y + tile.height - (newPos.y);
						if(x>y){
							newPos.x -= y;
							newPos.y += y;
						} else {
							newPos.x -= x;
							newPos.y += x;
						}
						break;
					case SOUTHWEST:
						x = tile.x + tile.width - (newPos.x - 10);
						y = tile.y + tile.height - (newPos.y);
						//						System.out.println("x: "+x+"y: "+y);
						if(x>y){
							newPos.x += y;
							newPos.y += y;
						} else {
							newPos.x += x;
							newPos.y += x;
						}
						break;
					case WEST:
						newPos.x = tile.x + tile.width + 10;
						break;
					default:
						break;

					}
				}
			}
		}
		this.position = newPos;
	}

	private void setRect(Vector2 newPos) {
		this.bobRect.set(newPos.x - 10, newPos.y, 50, 50);
	}

	/**
	 * Get the velocity of Bob.
	 * @return velocity of Bob
	 */
	public Vector2 getVelocity(){
		return velocity;
	}

	/**
	 * Get the direction of Bob.
	 * @return direction of Bob
	 */
	public Direction getDirection(){
		return direction;
	}

	/**
	 * Set the velocity of Bob.
	 * @param vx,vy	new velocity
	 */
	public void setVelocity(float vx, float vy){
		velocity = new Vector2(vx,vy);
	}

	private void scaleAndSetVelocity(float touchX, float touchY) {
		velocity = new Vector2(touchX*MAX_SPEED, touchY*MAX_SPEED);
	}

	public void setTouchpad(Touchpad touchpad){
		this.touchpad = touchpad;
	}

	/**
	 * Send the location of Bob with JSON.
	 * @param state	the state of Bob
	 */
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

	/**
	 * Set the position of Bob.
	 * @param x,y	new position
	 */
	public void setPosition(float x, float y) {
		this.position = new Vector2(x,y);
	}

	public void decrementLifeCount() {
		if (state == STATE_INVULNERABLE) {
			return;
		}
		this.lifeCount = this.lifeCount - 1;
	}

	public void setLifeCount(int x) {
		this.lifeCount = x;
	}

	public int getLifeCount() {
		return this.lifeCount;
	}

	public float getManaCount(){
		return this.manaCount;
	}

	public void decrementManaCount(int x){
		this.manaCount -= x;
	}

	public void incrementManaCount(){
		System.out.println("mana: " + this.manaCount);
		if (this.manaCount<200){
			this.manaCount += 0.1;
		}
	}

	/**
	 * Get the state of Bob.
	 * @return state of Bob
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state of Bob.
	 * @param state	new state
	 */
	public void setState(int state) {
		this.state = state;
	}

	public void updateObstacles(Array<Rectangle> tiles) {
		this.tiles = tiles;
		//		System.out.println("tiles updated");
	}

	/**
	 * Get the tiles array.
	 * @return tiles array
	 */
	public Array<Rectangle> getTiles() {
		return tiles;
	}

	/**
	 * Get the Rectangle instance.
	 * @return Rectangle instance
	 */
	public Rectangle getbobRect() {
		return bobRect;
	}

}
