package com.androidboys.spellarena.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.androidboys.spellarena.gameworld.GameFactory.GameModel;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.DummyBlinkObject;
import com.androidboys.spellarena.model.GameObject;
import com.androidboys.spellarena.model.Laser;
import com.androidboys.spellarena.model.Spell;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.model.Sword;
import com.androidboys.spellarena.model.Thunderstorm;
import com.androidboys.spellarena.model.Tornado;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class GameWorld {

	private Random random = new Random();
	
	//Boundaries
	public static final float WORLD_BOUND_LEFT = 80;
	public static final float WORLD_BOUND_RIGHT = 1805;
	public static final float WORLD_BOUND_TOP = 960;
	public static final float WORLD_BOUND_BOTTOM = 70;

	private static final String TAG = "GameWorld";
	
	//Characters
	private Map<String, Bob> playerModels = new HashMap<String, Bob>();
	private ArrayList<Object> gameObjects = new ArrayList<Object>();
	
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
	
	private GameScreenMediator mediator;
	
	public void initialize(GameModel model){
		//Create new characters
		map = AssetLoader.map;
		prepareObstacles();
	}
	
	private void updatePlayerModels(float delta){
		for(Bob bob: playerModels.values()){
			if (bob.getState() != Bob.STATE_DEAD){

				updatePlayerModel(bob, delta);
			}
		}
	}
	
	private void updatePlayerModel(Bob bob, float delta) {
		bob.update(delta);
		checkPlayerCollision(delta);
	}

	private void checkObjectCollision() {
		for(Object object: gameObjects.toArray()){
			if(tiles != null){
				for(Rectangle tile: tiles){
					if(tile.overlaps(((GameObject)object).getRectangle()) && !(object instanceof DummyBlinkObject)){
						gameObjects.remove(object);
					}
				}
			}
		}
	}
	
	private void checkPlayerObjectCollision(float delta) {
		Bob bob = getPlayerModel(UserSession.getInstance().getUserName());
		if(!bob.isInvulnerable()){
			float damage = 0;
			for(Object object: gameObjects.toArray()){
//				Gdx.app.log(TAG,"Checking object collision: "+object);
				GameObject gameObject = (GameObject)object;
				if (bob.getbobRect().overlaps(gameObject.getRectangle()) && !UserSession.getInstance().getUserName().equals(gameObject.getUsername())){
//					Gdx.app.log(TAG,"Colliding");
					if (object instanceof Tornado){
						damage += 200f;
					} else if (object instanceof Sword){
						damage += 300f;
					} else if (object instanceof Laser){
						damage += 200f;
					}
				}
			}
			if(!bob.takeDamage(damage*delta)){
				if(UserSession.getInstance().isServer()){
					if(this.isGameEnd()){
						mediator.endGame(getWinner());
					}
				}
			}
		}
	}

	private void checkPlayerCollision(float delta) {
		for(Bob bob: playerModels.values()){
			Vector2 newPos = (bob.getPosition().cpy()).add((bob.getVelocity().cpy()).scl(delta));
			bob.setRect(newPos);
			if(tiles != null){
				for(Rectangle tile: tiles){
					if(tile.overlaps(bob.getbobRect())){
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
				if(bob.getState() == Bob.STATE_RUNNING){
					for(Bob otherBob: playerModels.values()){
						if(!bob.equals(otherBob)){
							Rectangle rect = otherBob.getbobRect();
							if(rect.overlaps(bob.getCollisionEdge())){
								switch(bob.getDirection()){
								case EAST:
									newPos.x = rect.x - 40;
									break;
								case NORTH:
									newPos.y = rect.y - 50;
									break;
								case NORTHEAST:
									float x = (newPos.x + 40) - rect.x;
									float y = (newPos.y + 50) - rect.y;
									if(x>y){
										newPos.x -= y;
										newPos.y -= y;
									} else {
										newPos.x -= x;
										newPos.y -= x;
									}
									break;
								case NORTHWEST:
									x = rect.x + rect.width - (newPos.x - 10);
									y = (newPos.y + 50) - rect.y;
									if(x>y){
										newPos.x += y;
										newPos.y -= y;
									} else {
										newPos.x += x;
										newPos.y -= x;
									}
									break;
								case SOUTH:
									newPos.y = rect.y + rect.height;	
									break;
								case SOUTHEAST:
									x = (newPos.x + 40) - rect.x;
									y = rect.y + rect.height - (newPos.y);
									if(x>y){
										newPos.x -= y;
										newPos.y += y;
									} else {
										newPos.x -= x;
										newPos.y += x;
									}
									break;
								case SOUTHWEST:
									x = rect.x + rect.width - (newPos.x - 10);
									y = rect.y + rect.height - (newPos.y);
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
									newPos.x = rect.x + rect.width + 10;
									break;
								default:
									break;
	
								}
							}
						}
					}
				}
			}
			bob.setPosition(newPos);
		}
	}

	/**
	 * Update game world.
	 */
	public void update(float delta) {
		updatePlayerModels(delta);
		updateGameObjects(delta);
		checkPlayerObjectCollision(delta);
	}
	
	private void updateGameObjects(float delta) {
		for(Object o: gameObjects.toArray()){
			if(o instanceof GameObject){
				((GameObject) o).update(delta);
			}
		}
		checkObjectCollision();
	}

	private void prepareObstacles(){
		//Get the "Collidable" layer of map
		MapLayer layer = (MapLayer) map.getLayers().get("Collidable");
		//Puts objects of "tiles" in the pool.
		rectPool.freeAll(tiles);
		tiles.clear();
		for(MapObject obj: layer.getObjects()){
//			System.out.println("Object found");
			float x = (Float) obj.getProperties().get("x");
			float y = (Float) obj.getProperties().get("y");
			float width = (Float) obj.getProperties().get("width");
			float height = (Float) obj.getProperties().get("height");
//			System.out.println(x+" "+y+" "+width+" "+height);
			Rectangle rect = rectPool.obtain();
			rect.set(x, y, width, height);
			tiles.add(rect);
		}
	}

	public Bob getPlayerModel(String playerName) {
		return playerModels.get(playerName);
	}

	public int getNextGameIndex() {
		int max = 0;
		for(Bob bob: playerModels.values()){
			Gdx.app.log(TAG,"Game index: "+bob.getGameIndex());
			if(bob.getGameIndex() > max) {
				Gdx.app.log(TAG,"Game index: "+bob.getGameIndex());
				max = bob.getGameIndex();
			}
		}
		return max + 1;
	}

	public void addPlayerModel(Bob bob) {
		Gdx.app.log(TAG,bob.toString());
		bob.updateObstacles(tiles);
		synchronized (playerModels) {
			playerModels.put(bob.getPlayerName(), bob);
		}
		
	}
	
	public Spells getSpell(){
		return spell;
	}
	
	public void setSpell(Spells spell){
		this.spell = spell;
		
	}

	public Map<String,Bob> getPlayerModels() {
		return playerModels;
	}

	public void movePlayer(long time, String fromUser, int movement, float x, float y) {
		Bob bob = playerModels.get(fromUser);
		bob.move(time, movement, x, y);
	}

	public void updatePlayer(String fromUser, long timestamp, Vector2 position, Vector2 velocity, float health) {
		Bob bob = playerModels.get(fromUser);
		bob.setUpdateDetails(timestamp, position, velocity);
		if((!bob.updateHealth(health))&&UserSession.getInstance().isServer()){
			if(isGameEnd()){
				mediator.endGame(getWinner());
			}
		};
		
	}

	private String getWinner() {
		for(String playerName: playerModels.keySet()){
			if(playerModels.get(playerName).getState() == Bob.STATE_ALIVE){
				return playerName;
			}
		}
		return null;
	}

	public void movePlayer(String userName, int movement) {
		Bob bob = playerModels.get(userName);
		bob.move(movement);
	}

	public void removePlayer(String playerName) {
		synchronized (playerModels) {
			playerModels.remove(playerName);
		}
	}
	
	public boolean isGameEnd() {

        int playingUser = 0;
        for (Bob playerModel : playerModels.values()) {
            if (playerModel.getState() != Bob.STATE_DEAD) {
                playingUser++;
            }
        }
		Gdx.app.log(TAG,"isGameEnd: "+(playingUser <= 1));
        return playingUser <= 1;
    }

	public void castSpell(String playerName, float x, float y, Spells spellEnum,
			int direction) {
		Gdx.app.log(TAG, "Casting spell "+spellEnum);
		Bob bob = getPlayerModel(playerName);
		switch (spellEnum) {
		case SPARK:
			//consider changing
			//check for collision
			//send position and time remaining to server
			//display on UI
			//clear on screen
			break;
		case DIVINESHIELD:
			bob.setInvulnerable();
			break;

		case BLINK:
			blinkBob(bob);
			break;

		case HASTE:
			bob.setBoosted();
			break;

		case FIREWALL:

			//collision with sprite model (+100 radius)
			//insert mine at bob position
			//if stasis trap near enemy,
			break;

		case BLADESTORM:
			createFlyingSword(bob);
			//collision with sprite

			//need add new collision			
			break;

		case THUNDERSTORM:
			createThunderstorm(bob);
			break;

		case MINE:
			//collision with sprite, decrement health

			//insert mine at bob position
			break;

		case LASER:
			createLaser(bob);
			break;


		case TORNADO:
			createTornado(bob);
			break;
			
		default:
			break;
		}
	}
	private void createThunderstorm(Bob bob) {
		final Thunderstorm thunderstorm = new Thunderstorm(bob.getPosition().x, bob.getPosition().y, bob.getDirection(), bob.getPlayerName());
		gameObjects.add(thunderstorm);
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	synchronized (gameObjects) {
			        		gameObjects.remove(thunderstorm);
						}
		            }
		        }, 
		        1500 
		);
	}

	private void blinkBob(Bob bob){
		final DummyBlinkObject blinkObject = new DummyBlinkObject(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName());
		gameObjects.add(blinkObject);
		
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
	
	private void createLaser(Bob bob){
		final Laser laser = new Laser(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(), bob);
		gameObjects.add(laser);
//		new java.util.Timer().schedule( 
//		        new java.util.TimerTask() {
//		            @Override
//		            public void run() {
//		        		gameObjects.remove(laser);
//		            }
//		        }, 
//		        2000 
//		);
	}

	private void createFlyingSword(Bob bob) {
		Sword sword0 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(0));
		Sword sword1 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(120));
		Sword sword2 = new Sword(bob.getPosition().x, bob.getPosition().y, bob.getPlayerName(),(float) Math.toRadians(240));
		gameObjects.add(sword0);
		gameObjects.add(sword1);
		gameObjects.add(sword2);
	}

	private void createTornado(Bob bob) {
		Tornado tornado = new Tornado(bob.getPosition().x, bob.getPosition().y, bob.getDirection(), bob.getPlayerName());
		gameObjects.add(tornado);
	}

	public ArrayList<Object> getGameObjects() {
		return gameObjects;
	}

	public void setMediator(GameScreenMediator mediator) {
		this.mediator = mediator;
	}

}
