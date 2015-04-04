package com.androidboys.spellarena.gameworld;

import appwarp.WarpController;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class GameWorld {

	//Boundaries
	public static final float WORLD_BOUND_LEFT = 80;
	public static final float WORLD_BOUND_RIGHT = 1805;
	public static final float WORLD_BOUND_TOP = 960;
	public static final float WORLD_BOUND_BOTTOM = 70;

	private World physicsWorld;
	private static GameWorld instance;

	
	//Characters
	private final Bob local_bob;
	private final Bob enemy_bob;
	
	//Map
	private TiledMap map;
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();
	
	public GameWorld(){
		//Create new characters
		this.local_bob = new Bob(810, 540, false);
		this.enemy_bob = new Bob(810, 540, true);

		map = AssetLoader.map;
	}
	
	public static GameWorld getInstance(){
		if(instance == null){
			instance = new GameWorld();
		}
		return instance;
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
		getObstacles();
		local_bob.updateObstacles(tiles);
		
		local_bob.update(delta);
		enemy_bob.update(delta);
		local_bob.sendLocation(0);
		
	}
	
	private void getObstacles(){
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
	
	/**
	 * Update enemy bob.
	 */
	public void updateEnemy(float x, float y, float vx, float vy, int state) {
		enemy_bob.setVelocity(vx, vy);
		enemy_bob.setPosition(x, y);
	}
	
	/**
	 * Get the instance of enemy.
	 */
	public Bob getEnemy() {
		return enemy_bob;
	}
}
