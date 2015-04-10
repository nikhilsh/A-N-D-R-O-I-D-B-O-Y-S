package com.androidboys.spellarena.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.androidboys.spellarena.gameworld.GameFactory.GameModel;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Spell;
import com.androidboys.spellarena.model.Spell.Spells;
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
	//Characters
		private  Bob local_bob;
		private  Bob enemy_bob;
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
//		if(bob.isInvulnerable()){
//			bob.decrementImmortalDuration(delta);
//		}

		bob.update(delta);
		
	}

	/**
	 * Get the instance of character.
	 */
	public Bob getBob(){
		return local_bob;
	}

	/**
	 * Update game world.
	 */
	public void update(float delta) {
		updatePlayerModels(delta);
		
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
		playerModels.put(bob.getPlayerName(), bob);
	}
	
	/**
	 * Update enemy bob.
	 */
	public void updateEnemy(float x, float y, float vx, float vy, int state) {
		enemy_bob.setVelocity(vx, vy);
		enemy_bob.setPosition(x, y);
	}
	
	public Spells getSpell(){
		return spell;
	}
	
	public void setSpell(Spells spell){
		this.spell = spell;
		
	}
	
	/**
	 * Get the instance of enemy.
	 */
	public Bob getEnemy() {
		return enemy_bob;
	}

	public Map<String,Bob> getPlayerModels() {
		return playerModels;
	}

	public void movePlayer(long time, String fromUser, int movement, float x, float y) {
		Bob bob = playerModels.get(fromUser);
		bob.move(time, movement, x, y);
	}

	public void updatePlayer(String fromUser, long timestamp, Vector2 position, Vector2 velocity) {
		Bob bob = playerModels.get(fromUser);
		bob.setUpdateDetails(timestamp, position, velocity);
	}

	public void movePlayer(String userName, int movement) {
		Bob bob = playerModels.get(userName);
		bob.move(movement);
	}
}
