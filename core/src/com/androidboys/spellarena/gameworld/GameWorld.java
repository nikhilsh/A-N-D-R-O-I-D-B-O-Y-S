package com.androidboys.spellarena.gameworld;

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

	public static final float WORLD_BOUND_LEFT = 80;
	public static final float WORLD_BOUND_RIGHT = 1805;
	public static final float WORLD_BOUND_TOP = 960;
	public static final float WORLD_BOUND_BOTTOM = 70;

	private World physicsWorld;
	
	private final Bob local_bob;
	private final Bob enemy_bob;
	
	private TiledMap map;
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();
	
	public GameWorld(){
		this.local_bob = new Bob(810, 540, false);
		this.enemy_bob = new Bob(810, 540, true);
		
		map = AssetLoader.map;
	}
	
	public Bob getBob(){
		return local_bob;
	}

	public void update(float delta) {
		int startX, startY = 0, endX, endY = 0;
		switch(local_bob.getDirection()){
			case EAST:
				startX = endX = (int)(local_bob.getPosition().x + 40 + local_bob.getVelocity().x);
				startY = (int) (local_bob.getPosition().y - 25);
				endY = (int) (local_bob.getPosition().y - 25);
				break;
			case NORTH:
				startY = endY = (int)(local_bob.getPosition().y + 25 + local_bob.getVelocity().y);
				startX = (int) (local_bob.getPosition().x - 10);
				endX = (int) (local_bob.getPosition().x + 40);
				break;
			case SOUTH:
				startX = endX = (int)(local_bob.getPosition().y - 25 + local_bob.getVelocity().y);
				startX = (int) (local_bob.getPosition().x - 10);
				endX = (int) (local_bob.getPosition().x + 40);
				break;
			case WEST:
				startX = endX = (int)(local_bob.getPosition().x + 40 + local_bob.getVelocity().x);
				startY = (int) (local_bob.getPosition().y - 25);
				endY = (int) (local_bob.getPosition().y + 25);
				break;
			default:
				startX = (int) (local_bob.getPosition().x - 10);
				endX = (int) (local_bob.getPosition().x + 40);
				startY = (int) (local_bob.getPosition().y - 25);
				endY = (int) (local_bob.getPosition().y + 25);
				break;
		}
		getTiles(startX, startY, endX, endY);
		local_bob.updateTiles(tiles);
		
		local_bob.update(delta);
		enemy_bob.update(delta);
		local_bob.sendLocation(0);
		
	}
	
	private void getTiles(int startX, int startY, int endX, int endY){
		MapLayer layer = (MapLayer) map.getLayers().get("Collidable");
		rectPool.freeAll(tiles);
		tiles.clear();
		for(MapObject obj: layer.getObjects()){
			System.out.println("Object found");
			float x = (Float) obj.getProperties().get("x");
			float y = (Float) obj.getProperties().get("y");
			float width = (Float) obj.getProperties().get("width");
			float height = (Float) obj.getProperties().get("height");
			System.out.println(x+" "+y+" "+width+" "+height);
			Rectangle rect = rectPool.obtain();
			rect.set(x, y, width, height);
			tiles.add(rect);
		}
	}

	public void updateEnemy(float x, float y, float vx, float vy, int state) {
		enemy_bob.setVelocity(vx, vy);
		enemy_bob.setPosition(x, y);
	}

	public Bob getEnemy() {
		return enemy_bob;
	}
}
