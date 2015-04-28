package com.androidboys.spellarena.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.androidboys.spellarena.gameworld.GameFactory.GameModel;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Bob.Direction;
import com.androidboys.spellarena.model.DummyBlinkObject;
import com.androidboys.spellarena.model.Firewall;
import com.androidboys.spellarena.model.GameObject;
import com.androidboys.spellarena.model.Laser;
import com.androidboys.spellarena.model.Sunstrike;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.model.Sword;
import com.androidboys.spellarena.model.Thunderstorm;
import com.androidboys.spellarena.model.Projectile;
import com.androidboys.spellarena.model.Boomerang;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class GameWorld {

	//Boundaries
	public static final float WORLD_BOUND_LEFT = 80;
	public static final float WORLD_BOUND_RIGHT = 1805;
	public static final float WORLD_BOUND_TOP = 960;
	public static final float WORLD_BOUND_BOTTOM = 70;

	//Tag for the world
	private static final String TAG = "GameWorld";

	//Characters
	private Map<String, Bob> playerModels = new HashMap<String, Bob>();
	private ArrayList<Object> gameObjects = new ArrayList<Object>();
	
	//Instance of spell
	private Spells spell;

	//Map
	private TiledMap map;

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();

	//Reference of GameScreenMediator
	private GameScreenMediator mediator;

	//Initialize the game world
	public void initialize(GameModel model){
		map = AssetLoader.map;
		prepareObstacles();
	}

	
	/**
	 * Update player models.
	 *
	 * @param delta the delta value, which is the game render rate
	 */
	private void updatePlayerModels(float delta){
		for(Bob bob: playerModels.values()){
			if (bob.getState() != Bob.STATE_DEAD){
				updatePlayerModel(bob, delta);
			}
		}
	}

	/**
	 * Update player model.
	 *
	 * @param bob takes in a player model
	 * @param delta the delta value
	 */
	private void updatePlayerModel(Bob bob, float delta) {
		bob.update(delta);
		checkPlayerCollision(delta);
	}

	/**
	 * Check object collision.
	 */
	private void checkObjectCollision() {
		for(Object object: gameObjects.toArray()){
			if(tiles != null){
				for(Rectangle tile: tiles){
					if(tile.overlaps(((GameObject)object).getRectangle()) && !((object instanceof DummyBlinkObject) || (object instanceof  Laser))){
						synchronized (gameObjects) {
							gameObjects.remove(object);
						}
					}
				}
			}
			if(object instanceof Boomerang) {
				if(((Boomerang) object).checkFinished()){
					synchronized (gameObjects) {
						gameObjects.remove(object);
					}
				}
			}
		}
	}

	/**
	 * Check player object collision. This is used to see if game objects (damage dealing objects) hit the player rectangle
	 *
	 * @param delta the delta value
	 */
	private void checkPlayerObjectCollision(float delta) {
		Bob bob = getPlayerModel(UserSession.getInstance().getUserName());
		if(!bob.isInvulnerable()){
			float damage = 0;
			for(Object object: gameObjects.toArray()){
				GameObject gameObject = (GameObject)object;
				if (gameObject instanceof Laser && !UserSession.getInstance().getUserName().equals(gameObject.getUsername())){
					boolean isHit = false;
					for (Rectangle rectangle : ((Laser) gameObject).getRectangleArray()){
						if (bob.getbobRect().overlaps(rectangle)){
							isHit = true;
							break;
						}
					}
					if (isHit){
						damage += 400f;
					}
				}
				if (bob.getbobRect().overlaps(gameObject.getRectangle()) 
						&& !UserSession.getInstance().getUserName().equals(gameObject.getUsername())){
					if (object instanceof Projectile){
						damage += 200f;
					} else if (object instanceof Sword){
						damage += 200f;
					} else if (object instanceof Boomerang){
						damage += 300f;
					} else if (object instanceof Firewall){
						damage += 350f;
					} else if (object instanceof Thunderstorm){
						damage += 500f;
					} else if (object instanceof Sunstrike){
						damage += 800f;
					}
				}	
			}
			if(!bob.takeDamage(damage*delta)){
				if(UserSession.getInstance().isServer()){
					if(this.isGameEnd()){
						mediator.endGame(getWinner());
					}
				} else {
					mediator.update(getPlayerModel(UserSession.getInstance().getUserName()));
				}
			}
		}
	}

	/**
	 * Check player collision against walls
	 *
	 * @param delta the delta value
	 */
	private void checkPlayerCollision(float delta) {
		for(Bob bob: playerModels.values()){
			Vector2 newPos = (bob.getPosition().cpy()).add((bob.getVelocity().cpy()).scl(delta));
			bob.setRect(newPos);
			if(tiles != null){
				for(Rectangle tile: tiles){
					if(tile.overlaps(bob.getCollisionEdge())){
						switch(bob.getDirection()){
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
			if(newPos.x > GameWorld.WORLD_BOUND_RIGHT){
				newPos.x = GameWorld.WORLD_BOUND_RIGHT;
			} else if (newPos.x < GameWorld.WORLD_BOUND_LEFT){
				newPos.x = GameWorld.WORLD_BOUND_LEFT;
			}
			if(newPos.y > GameWorld.WORLD_BOUND_TOP){
				newPos.y = GameWorld.WORLD_BOUND_TOP;
			} else if (newPos.y < GameWorld.WORLD_BOUND_BOTTOM){
				newPos.y = GameWorld.WORLD_BOUND_BOTTOM;
			}
			bob.setPosition(newPos);
		}
	}

	
	/**
	 * Update the gameworld
	 *
	 * @param delta the delta value
	 */
	public void update(float delta) {
		updatePlayerModels(delta);
		updateGameObjects(delta);
		checkPlayerObjectCollision(delta);
	}

	/**
	 * Update game objects and see if collision occurs
	 *
	 * @param delta the delta value
	 */
	private void updateGameObjects(float delta) {
		for(Object o: gameObjects.toArray()){
			if(o instanceof GameObject){
				((GameObject) o).update(delta);
			}
		}
		checkObjectCollision();
	}

	/**
	 * Prepare obstacles for the map, such as the wall and the firepit
	 */
	private void prepareObstacles(){
		MapLayer layer = (MapLayer) map.getLayers().get("Collidable");
		rectPool.freeAll(tiles);
		tiles.clear();
		for(MapObject obj: layer.getObjects()){
			float x = (Float) obj.getProperties().get("x");
			float y = (Float) obj.getProperties().get("y");
			float width = (Float) obj.getProperties().get("width");
			float height = (Float) obj.getProperties().get("height");
			Rectangle rect = rectPool.obtain();
			rect.set(x, y, width, height);
			tiles.add(rect);
		}
	}

	/**
	 * Gets the player model.
	 *
	 * @param playerName the player name
	 * @return the player model
	 */
	public Bob getPlayerModel(String playerName) {
		return playerModels.get(playerName);
	}

	/**
	 * Gets the next game index.
	 *
	 * @return the next game index
	 */
	public int getNextGameIndex() {
		int max = 0;
		for(Bob bob: playerModels.values()){
			if(bob.getGameIndex() > max) {
				max = bob.getGameIndex();
			}
		}
		return max + 1;
	}

	/**
	 * Adds the player model to the world and updates obstacles.
	 *
	 * @param bob the bob
	 */
	public void addPlayerModel(Bob bob) {
		bob.updateObstacles(tiles);
		synchronized (playerModels) {
			playerModels.put(bob.getPlayerName(), bob);
		}

	}

	/**
	 * Gets the spell.
	 *
	 * @return the spell
	 */
	public Spells getSpell(){
		return spell;
	}

	/**
	 * Sets the spell.
	 *
	 * @param spell the new spell
	 */
	public void setSpell(Spells spell){
		this.spell = spell;

	}

	/**
	 * Gets the player models in the world.
	 *
	 * @return the player models
	 */
	public Map<String,Bob> getPlayerModels() {
		return playerModels;
	}

	/**
	 * Moves player.
	 *
	 * @param time the time
	 * @param fromUser which user movement is affecting
	 * @param movement the movement enum
	 * @param x the x value
	 * @param y the y value
	 */
	public void movePlayer(long time, String fromUser, int movement, float x, float y) {
		Bob bob = playerModels.get(fromUser);
		bob.move(time, movement, x, y);
	}

	/**
	 * Update player.
	 *
	 * @param fromUser which user is affected
	 * @param timestamp the timestamp that update occurred
	 * @param position the position that user is at
	 * @param velocity the velocity that user has
	 * @param health the health of the player
	 */
	public void updatePlayer(String fromUser, long timestamp, Vector2 position, Vector2 velocity, float health) {
		Bob bob = playerModels.get(fromUser);
		bob.setUpdateDetails(timestamp, position, velocity);
		if((!bob.updateHealth(health))&&UserSession.getInstance().isServer()){
			if(isGameEnd()){
				mediator.endGame(getWinner());
			}
		};

	}

	/**
	 * Gets the winner of the game!
	 *
	 * @return the winner
	 */
	private String getWinner() {
		for(String playerName: playerModels.keySet()){
			synchronized (playerModels.get(playerName)) {
				if(playerModels.get(playerName).getState() != Bob.STATE_DEAD){
					return playerName;
				}
			}
		}
		return null;
	}

	/**
	 * Moves player.
	 *
	 * @param userName the user name of the player
	 * @param movement the movement
	 */
	public void movePlayer(String userName, int movement) {
		Bob bob = playerModels.get(userName);
		bob.move(movement);
	}

	/**
	 * Removes the player from game.
	 *
	 * @param playerName the player name
	 */
	public void removePlayer(String playerName) {
		synchronized (playerModels) {
			playerModels.remove(playerName);
		}
	}

	/**
	 * Checks if game has ended
	 *
	 * @return true, if game has ended
	 */
	public boolean isGameEnd() {
		int playingUser = 0;
		for (Bob playerModel : playerModels.values()) {
			synchronized (playerModel) {
				if (playerModel.getState() != Bob.STATE_DEAD) {
					playingUser++;
				}
			}
		}
		return playingUser <= 1;
	}

	/**
	 * Cast spell that user has invoked
	 *
	 * @param playerName the player that is casting the spell
	 * @param x the x position of the spell
	 * @param y the y position of the spell
	 * @param spellEnum the spell enum, which is the name of the spell
	 * @param direction the direction that the spell is going to be cast in
	 */
	public void castSpell(String playerName, float x, float y, Spells spellEnum,
			int direction) {
		Bob bob = getPlayerModel(playerName);
		switch (spellEnum) {
		case SPECTRALTHROW:
			castSpark(bob);
			break;
		case DIVINESHIELD:
			bob.setInvulnerable();
			break;
		case BLINK:
			blinkBob(bob);
			break;
		case HASTE:
			bob.setHasted();
			break;
		case FIREWALL:
			castFirewall(bob);
			break;
		case HURRICANE:
			createFlyingSword(bob);
			break;
		case THUNDERSTORM:
			createThunderstorm(bob);
			break;
		case EXPLOSION:
			createSunstrike(bob, x, y);
			break;
		case LASER:
			createLaser(bob);
			break;
		case SHADOWBLAST:
			createTornado(bob);
			break;

		default:
			break;
		}
	}
	
	/**
	 * Cast firewall.
	 *
	 * @param bob takes in player
	 */
	private void castFirewall(Bob bob) {
		Direction direction = bob.getDirection();
		float rotation = 0;
		switch(direction){
		case EAST:
			rotation = 0;
			break;
		case NORTH:
			rotation = 90;
			break;
		case NORTHEAST:
			rotation = 45;
			break;
		case NORTHWEST:
			rotation = 135;
			break;
		case SOUTH:
			rotation = 90;
			break;
		case SOUTHEAST:
			rotation = 135;
			break;
		case SOUTHWEST:
			rotation = 45;
			break;
		case WEST:
			rotation = 0;
			break;
		default:
			break;	
		}
		Vector2 vect = new Vector2(0,250).rotate(rotation).scl(1/6f);
		synchronized (gameObjects) {
			gameObjects.add(new Firewall(bob.getPosition().x, bob.getPosition().y, 
					bob.getDirection(), bob.getPlayerName()));
			for(int i = 1; i<4 ; i++){
				gameObjects.add(new Firewall(bob.getPosition().x+i*vect.x, bob.getPosition().y+i*vect.y, 
						bob.getDirection(), bob.getPlayerName()));
				gameObjects.add(new Firewall(bob.getPosition().x-i*vect.x, bob.getPosition().y-i*vect.y, 
						bob.getDirection(), bob.getPlayerName()));
			}
		}

	}

	/**
	 * Cast spark.
	 *
	 * @param bob takes in player
	 */
	private void castSpark(Bob bob) {
		Boomerang boomerang = new Boomerang(bob.getPosition().x, 
				bob.getPosition().y, bob.getDirection(), bob.getPlayerName(), bob);
		synchronized (gameObjects) {
			gameObjects.add(boomerang);
		}
	}

	/**
	 * Creates the sunstrike.
	 *
	 * @param bob takes in player
	 * @param x x coordinate of where the screen was tapped
	 * @param y y coordinate of where the screen was tapped
	 */
	private void createSunstrike(Bob bob, float x, float y){
		final Sunstrike sunstrike = new Sunstrike(x, y, bob.getPlayerName());
		
		synchronized (gameObjects) {
			gameObjects.add(sunstrike);
		}
		new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						synchronized (gameObjects) {
							gameObjects.remove(sunstrike);
						}
					}
				}, 
				3000 
				);
	}

	/**
	 * Creates the thunderstorm.
	 *
	 * @param bob takes in player
	 */
	private void createThunderstorm(Bob bob) {
		final Thunderstorm thunderstorm = new Thunderstorm(bob.getPosition().x, bob.getPosition().y, bob.getDirection(), bob.getPlayerName());
		synchronized (gameObjects) {
			gameObjects.add(thunderstorm);			
		}
		new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						synchronized (gameObjects) {
							gameObjects.remove(thunderstorm);
						}
					}
				}, 
				4000 
				);
	}

	/**
	 * Blink bob.
	 *
	 * @param bob takes in player
	 */
	private void blinkBob(Bob bob){
		final DummyBlinkObject blinkObject = new DummyBlinkObject(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName());
		synchronized (gameObjects) {
			gameObjects.add(blinkObject);
		}
		switch (bob.getDirection()) {
		case EAST:
			bob.setPosition(bob.getPosition().x+100, bob.getPosition().y);
			break;
		case NORTH:
			bob.setPosition(bob.getPosition().x, bob.getPosition().y+100);
			break;
		case NORTHEAST:
			bob.setPosition(bob.getPosition().x+100, bob.getPosition().y+100);
			break;
		case NORTHWEST:
			bob.setPosition(bob.getPosition().x-100, bob.getPosition().y+100);
			break;
		case SOUTH:
			bob.setPosition(bob.getPosition().x, bob.getPosition().y-100);
			break;
		case SOUTHEAST:
			bob.setPosition(bob.getPosition().x+100, bob.getPosition().y-100);
			break;
		case SOUTHWEST:
			bob.setPosition(bob.getPosition().x-100, bob.getPosition().y-100);
			break;
		case WEST:
			bob.setPosition(bob.getPosition().x-100, bob.getPosition().y);
			break;

		default:
			break;
		}
		new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						synchronized (gameObjects) {
							gameObjects.remove(blinkObject);
						}
					}
				}, 
				200 
				);	
	}

	/**
	 * Creates the laser.
	 *
	 * @param bob takes in player
	 */
	private void createLaser(Bob bob){
		final Laser laser = new Laser(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(), bob);
		synchronized (gameObjects) {
			gameObjects.add(laser);
		}
		new java.util.Timer().schedule( 
				new java.util.TimerTask() {
					@Override
					public void run() {
						synchronized (gameObjects) {
							gameObjects.remove(laser);
						}
					}
				}, 
				3000 
				);
	}

	/**
	 * Creates the flying sword.
	 *
	 * @param bob takes in player
	 */
	private void createFlyingSword(Bob bob) {
		Sword sword0 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(0));
		Sword sword1 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(120));
		Sword sword2 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(240));
		synchronized (gameObjects) {
			gameObjects.add(sword0);
			gameObjects.add(sword1);
			gameObjects.add(sword2);
		}
	}

	/**
	 * Creates the tornado.
	 *
	 * @param bob takes in player
	 */
	private void createTornado(Bob bob) {
		float rotation = 0;
		switch(bob.getDirection()){
		case NORTH:
			rotation = 90;
			break;
		case NORTHEAST:
			rotation = 45;
			break;
		case NORTHWEST:
			rotation = 135;
			break;
		case SOUTH:
			rotation = 270;
			break;
		case SOUTHEAST:
			rotation = 325;
			break;
		case SOUTHWEST:
			rotation = 225;
			break;
		case WEST:
			rotation = 180;
			break;
		default:
			break;
		}
		Projectile tornado0 = new Projectile(bob.getPosition().x, bob.getPosition().y, rotation-15, bob.getPlayerName());
		Projectile tornado1 = new Projectile(bob.getPosition().x, bob.getPosition().y, rotation, bob.getPlayerName());
		Projectile tornado2 = new Projectile(bob.getPosition().x, bob.getPosition().y, rotation+15, bob.getPlayerName());
		synchronized (gameObjects) {
			gameObjects.add(tornado0);
			gameObjects.add(tornado1);
			gameObjects.add(tornado2);
		}
	}

	/**
	 * Gets the list of game objects.
	 *
	 * @return the game objects
	 */
	public ArrayList<Object> getGameObjects() {
		return gameObjects;
	}

	/**
	 * Sets the game mediator.
	 *
	 * @param mediator the new mediator
	 */
	public void setMediator(GameScreenMediator mediator) {
		this.mediator = mediator;
	}

}
