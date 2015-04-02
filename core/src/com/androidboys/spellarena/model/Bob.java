package com.androidboys.spellarena.model;

import org.json.JSONObject;

import appwarp.WarpController;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

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
	
	public static final int STATE_ALIVE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_DEAD = 2;
	
	private static final float MAX_SPEED = 500f;
	
	private Vector2 position;
	protected Vector2 velocity;
	protected Direction direction;
	
	private boolean needsUpdate;
	
	private Touchpad touchpad;

	private int state;
	
	private Rectangle bobRect;
	
	private Array<Rectangle> tiles;
	
	public Bob(int i, int j) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2();		
		this.direction = Direction.SOUTH;
		this.state = STATE_ALIVE;
		this.bobRect = new Rectangle();
		this.tiles = null;
	}

	public Vector2 getPosition(){
		return position;
	}

	public void onClick() {
		position.add(1, 0);
	}
	
	public void update(float delta){
		checkCollision(delta);
		updateVelocity();
		updateState();
	}

	protected void updateState(){
		if(this.velocity.isZero()){
			this.state = STATE_ALIVE;
		} else {
			this.state = STATE_RUNNING;
		}
	}
	
	protected void updateVelocity(){
		Vector2 newV = null;
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
	}
	
	
	@Deprecated
	private void checkBoundaries(float delta){
		this.position.add(this.velocity.cpy().scl(delta));			
		System.out.println("Position: "+this.position);
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
	
	protected void checkCollision(float delta){
		Vector2 newPos = this.position.cpy().add(this.velocity.cpy().scl(delta));
		this.setRect(newPos);
		if(getTiles() != null){
			for(Rectangle tile: getTiles()){
				if(tile.overlaps(this.bobRect)){
					System.out.println("Overlap");
					switch(direction){
					case EAST:
						System.out.println("Colliding east");
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
						System.out.println("x: "+x+"y: "+y);
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

	public Vector2 getVelocity(){
		return velocity;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setVelocity(float vx, float vy){
		velocity = new Vector2(vx,vy);
	}
	
	@Deprecated
	private void scaleAndSetVelocity(float touchX, float touchY) {
		velocity = new Vector2(touchX*MAX_SPEED, touchY*MAX_SPEED);
	}
	
	public void setTouchpad(Touchpad touchpad){
		this.touchpad = touchpad;
	}
	
	int i=0;
	public void sendLocation(int state){
		i++;
		if(i%10==0){
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void updateObstacles(Array<Rectangle> tiles) {
		this.tiles = tiles;
		System.out.println("tiles updated");
	}

	public Array<Rectangle> getTiles() {
		return tiles;
	}

	public Rectangle getbobRect() {
		return bobRect;
	}

	
	
}
