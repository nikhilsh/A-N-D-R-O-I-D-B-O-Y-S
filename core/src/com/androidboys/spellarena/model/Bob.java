package com.androidboys.spellarena.model;


import java.util.ArrayList;

import org.json.JSONObject;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

public class Bob {

	public static interface StateChangeListener{
		
		public void onVelocityChange(int state, Direction direction);
	
	}
	
	//Directions
	public enum Direction{
		NORTH, 
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST, 
		NONE;
	}

	//States
	public static final int STATE_ALIVE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_DEAD = 2;
	
	@Deprecated
	public static final int STATE_INVULNERABLE = 3;

	//Speed
	private float MAX_SPEED = 100f;
	//Max HP
	public static final float MAX_HEALTH = 2000f;

	//Position
	private Vector2 position;
	//Velocity ((0,0) means at rest)
	private Vector2 velocity;
	//Direction
	private Direction direction;
	
	//Whether the Bob needs update (for position, direction or velocity)
	private boolean needsUpdate;

	private Touchpad touchpad;

	private int state;

	//Rectangle area that symbolizes Bob
	private Rectangle bobRect;

	private Array<Rectangle> tiles;
	//Invulnerable state
	private boolean invulnerable;
	//Player Name
	private String playerName;
	private int gameIndex;
	private StateChangeListener stateChangeListener;
	private Vector2 updatePosition;
	private Vector2 updateVelocity;
	private long updateTimestamp;
	private ArrayList<Object> gameObjects;
	private float shieldTimer;
	//HP
	private float health = 2000f;
	private float boostTimer;
	//Boosted buff
	private boolean boosted;
	
	/**
	 * Create a new Bob instance, with initial position
	 * @param i,j		initial position
	 */
	public Bob(int i, int j) {
		this.position = new Vector2(i,j);
		this.velocity = new Vector2(); //Velocity is (0,0)
		this.direction = Direction.SOUTH; //Face to south (bottom of screen)
		this.state = STATE_ALIVE; //Alive
		this.bobRect = new Rectangle();
		this.tiles = null;
	}

	/**
	 * Create a new Bob instance with default initial position
	 */
	public Bob() {
		this.position = new Vector2(); //Spawn at (0,0)
		this.velocity = new Vector2();
		this.direction = Direction.SOUTH;
		this.state = STATE_ALIVE;
		this.bobRect = new Rectangle();
		this.tiles = null;
	}

	public void setStateChangeListener(StateChangeListener stateChangeListener2){
		this.stateChangeListener = stateChangeListener2;
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
		//Invulnerable state
		if(invulnerable){
			shieldTimer -= delta; //Decrease the duration of invulnerable buff
			if(shieldTimer<0){
				invulnerable = false; //Remove buff
			}
		}
		//Boosted state
		if (boosted){
			boostTimer -= delta; //Decrease the duration of invulnerable buff
			if(boostTimer<=0){
				this.MAX_SPEED = 100f; //Restore the speed
				boosted = false; //Remove buff
				//Calculate new velocity
				this.velocity = new Vector2((Math.abs(getVelocity().x) == 100 ? 100 : getVelocity().x/3), (Math.abs(getVelocity().y) == 100 ? 100 : getVelocity().y/3));
			}
		}
		updateDirection();
		if(needsUpdate){
			applyUpdate(delta);
			needsUpdate = false;
		}
		updateState();
	}

	private void applyUpdate(float delta) {
		//float timeDiff = (System.currentTimeMillis() - updateTimestamp)/1000f;
		this.position = new Vector2(updatePosition);
//		this.position = new Vector2(updatePosition.add(updateVelocity.scl(timeDiff)));
	}

	/**
	 * Update state between alive and running
	 */
	private void updateState(){
		if(state != STATE_DEAD){
			if(this.velocity.isZero()){
				synchronized (this) {
					state = STATE_ALIVE;
				}
			} else {
				synchronized (this) {
					state = STATE_RUNNING;
				}
			}
		}
	}

	/**
	 * Update direction of Bob
	 */
	private void updateDirection(){
		if(this.velocity.x > 0){ //Right
			if(this.velocity.y > 0){ //Top right
				this.direction = Direction.NORTHEAST;
			} else if (this.velocity.y < 0){ //Bottom right
				this.direction = Direction.SOUTHEAST;
			} else { //Right only
				this.direction = Direction.EAST;
			}
		} else if (this.velocity.x < 0){ //Left
			if(this.velocity.y > 0){ //Top left
				this.direction = Direction.NORTHWEST;
			} else if (this.velocity.y < 0){ //Bottom left
				this.direction = Direction.SOUTHWEST;
			} else { //Left only
				this.direction = Direction.WEST;
			}
		} else { //No speed on x-axis
			if(this.velocity.y > 0){ //Top only
				this.direction = Direction.NORTH;
			} else if (this.velocity.y < 0){ //Bottom only
				this.direction = Direction.SOUTH;
			}
		}
	}
	
	
	@Deprecated
	private void updateVelocity(){
		Vector2 newV = null;
		if(playerName.equals(UserSession.getInstance().getUserName())){
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
					newV = new Vector2(MAX_SPEED, 0);
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
				this.setVelocity(newV.x, newV.y);
				//this.stateChangeListener.onVelocityChange(state, direction);
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

	/**
	 * Check whether a collision happens
	 * @param delta
	 */
	private void checkCollision(float delta){
		Vector2 newPos = (this.position.cpy()).add((this.velocity.cpy()).scl(delta));
		this.setRect(newPos);
		if(getTiles() != null){
			for(Rectangle tile: getTiles()){
				if(tile.overlaps(this.bobRect)){
					switch(direction){
					case EAST:
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

	public void setRect(Vector2 newPos) {
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
	
	public boolean isInvulnerable() {
		return invulnerable;
	}
	
	public boolean isBoosted(){
		return boosted;
	}

	public int getGameIndex() {
		return gameIndex;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPosition(Vector2 loadStartPosition) {
		this.position.x = loadStartPosition.x;
		this.position.y = loadStartPosition.y;
	}

	public void setGameIndex(int gameIndex) {
		this.gameIndex = gameIndex;
	}
	
	/**
	 * Move Bob towards a specified direction
	 * @param time
	 * @param movement
	 * @param x
	 * @param y
	 */
	public void move(long time, int movement, float x, float y) {
		switch(movement){
			case GameScreen.MOVEMENT_NONE:
				this.velocity = new Vector2(0, 0); //Stop
				break;
			//For other case, set the velocity to that direction with max speed
			case GameScreen.MOVEMENT_EAST:
				this.velocity = new Vector2(MAX_SPEED, 0);
				break;
			case GameScreen.MOVEMENT_NORTH:
				this.velocity = new Vector2(0, MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_NORTHEAST:
				this.velocity = new Vector2(MAX_SPEED, MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_NORTHWEST:
				this.velocity = new Vector2(-MAX_SPEED, MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_SOUTH:
				this.velocity = new Vector2(0, -MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_SOUTHEAST:
				this.velocity = new Vector2(MAX_SPEED, -MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_SOUTHWEST:
				this.velocity = new Vector2(-MAX_SPEED, -MAX_SPEED);
				break;
			case GameScreen.MOVEMENT_WEST:
				this.velocity = new Vector2(-MAX_SPEED, 0);
				break;
		}
//		float timeDiff = (System.currentTimeMillis() - time)/1000f;
//		this.position = new Vector2(position).add(new Vector2(velocity).scl(timeDiff));
	}

	public void setUpdateDetails(long timestamp, Vector2 position, Vector2 velocity) {
		needsUpdate = true;
		this.updateTimestamp = timestamp;
		this.updatePosition = position;
		this.updateVelocity = velocity;
	}

	/**
	 * Move Bob (with a specified direction only)
	 * @param movement
	 */
	public void move(int movement) {
		switch(movement){
		case GameScreen.MOVEMENT_NONE:
			this.velocity = new Vector2(0, 0);
			break;
		case GameScreen.MOVEMENT_EAST:
			this.velocity = new Vector2(MAX_SPEED, 0);
			break;
		case GameScreen.MOVEMENT_NORTH:
			this.velocity = new Vector2(0, MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_NORTHEAST:
			this.velocity = new Vector2(MAX_SPEED, MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_NORTHWEST:
			this.velocity = new Vector2(-MAX_SPEED, MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_SOUTH:
			this.velocity = new Vector2(0, -MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_SOUTHEAST:
			this.velocity = new Vector2(MAX_SPEED, -MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_SOUTHWEST:
			this.velocity = new Vector2(-MAX_SPEED, -MAX_SPEED);
			break;
		case GameScreen.MOVEMENT_WEST:
			this.velocity = new Vector2(-MAX_SPEED, 0);
			break;
		}
	}

	public void setGameObjects(ArrayList<Object> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	public Rectangle getCollisionEdge(){
		Rectangle rect;
		switch(direction){
		case EAST:
			rect = new Rectangle(position.x + 40, position.y, 1, 50);
			break;
		case NORTH:
			rect = new Rectangle(position.x - 10, position.y + 49, 50, 1);
			break;
		case NORTHEAST:
			rect = new Rectangle(position.x + 35, position.y + 45, 5, 5);
			break;
		case NORTHWEST:
			rect = new Rectangle(position.x - 10, position.y + 45, 5, 5);
			break;
		case SOUTH:
			rect = new Rectangle(position.x - 10, position.y - 1, 50, 1);
			break;
		case SOUTHEAST:
			rect = new Rectangle(position.x + 35, position.y, 5, 5);
			break;
		case SOUTHWEST:
			rect = new Rectangle(position.x - 10, position.y, 5, 5);
			break;
		case WEST:
			rect = new Rectangle(position.x - 11, position.y, 1, 50);
			break;
		default:
			rect = null;
			break;
		}
		return rect;
	}

	/**
	 * Set an invulnerable buff
	 * Duration: 1.5
	 */
	public void setInvulnerable() {
		this.invulnerable = true;
		this.shieldTimer = 1.5f;
	}
	
	/**
	 * Set a boosted buff
	 * Duration: 1
	 * Max speed increases to 300
	 */
	public void setHasted(){
		this.boosted = true;
		this.boostTimer = 1f;
		this.MAX_SPEED = 300f;
		if (getVelocity().x == 100){
			this.velocity.x = 300;
		}
		if (getVelocity().x == -100){
			this.velocity.x = -300;
		}
		if (getVelocity().y == 100){
			this.velocity.y = 300;
		}
		if (getVelocity().y == -100){
			this.velocity.y = 300;
		}
	}

	/**
	 * Receive damage
	 * @param f: amount of damage
	 * @return: whether Bob is alive
	 */
	public boolean takeDamage(float f) {
		if(state != STATE_DEAD){
			health  -= f; //Remove HP
			if(health < 0){ //HP is lower than 0
				health = 0; //HP becomes 0
				synchronized (this) {
					state = STATE_DEAD; //Dead
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get light radius
	 * Radius = HP / MAX_HP
	 * If HP=0, radius will be 1
	 * @return light radius
	 */
	public float getLightRadius(){
		return health/MAX_HEALTH > 0 ? Math.max(health/MAX_HEALTH , 0.15f): 1;
	}
	
	/**
	 * Get HP value
	 * @return HP value
	 */
	public float getHealth(){
		return health;
	}

	/**
	 * Set HP value
	 * @param health: new HP
	 * @return whether Bob is alive
	 */
	public boolean updateHealth(float health) {
		this.health = health;
		if(this.health == 0){
			this.state = STATE_DEAD;
			return false;
		}
		return true;
	}
}
