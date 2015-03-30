package com.androidboys.spellarena.gameworld;

import org.json.JSONObject;

import appwarp.WarpController;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	
	private ShapeRenderer shapeRenderer;
	
	private Bob bob;
	private Bob enemy;
	
	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	
	private float _x,_y;
	
	public GameRenderer(SpriteBatch batcher, GameWorld world) {
		this.world = world;
		this.cam = new OrthographicCamera(960,540);
		cam.position.set(1920/2, 1080/2, 0);

		shapeRenderer = new ShapeRenderer();
		
		bob = world.getBob();
		enemy = world.getEnemy();
		
		map = AssetLoader.map;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
	}

	public void render() {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		moveCamera();
		
		mapRenderer.setView(cam);
		mapRenderer.render();
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		renderBob();
		renderEnemy();
		shapeRenderer.end();
	}

	private void renderBob(){
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(bob.getPosition().x,bob.getPosition().y,50,50);
	}
	
	private void renderEnemy(){
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(enemy.getPosition().x,enemy.getPosition().y,50,50);
	}
	
	public void moveCamera(){
		cam.position.set(bob.getPosition().x,bob.getPosition().y,0);
		cam.update();
	}
	

}
