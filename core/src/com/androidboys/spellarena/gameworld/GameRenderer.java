package com.androidboys.spellarena.gameworld;

import org.json.JSONObject;

import appwarp.WarpController;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	
	private SpriteBatch batcher;
	private ShapeRenderer shapeRenderer;
	
	private Bob bob;
	private Bob enemy;
	
	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	
	//Game Assets
	private TextureRegion southBob, southWestBob, westBob, northWestBob,
		northBob, northEastBob, eastBob, southEastBob;
	private Animation southBobAnimation, southWestBobAnimation, westBobAnimation, northWestBobAnimation,
		northBobAnimation, northEastBobAnimation, eastBobAnimation, southEastBobAnimation;
	
	public GameRenderer(SpriteBatch batcher, GameWorld world) {
		this.world = world;
		//Create a new camera
		this.cam = new OrthographicCamera(960,540);
		//Initialization of camera
		cam.position.set(1920/2, 1080/2, 0);

		this.batcher = batcher;
		shapeRenderer = new ShapeRenderer();
		
		initAssets();
		initGameObjects();
	}

	private void initAssets(){
		map = AssetLoader.map;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		southBob = AssetLoader.southBob;
		southEastBob = AssetLoader.southEastBob;
		eastBob = AssetLoader.eastBob;
		northEastBob = AssetLoader.northEastBob;
		northBob = AssetLoader.northBob;
		northWestBob = AssetLoader.northWestBob;
		westBob = AssetLoader.westBob;
		southWestBob = AssetLoader.southWestBob;
		
		southBobAnimation = AssetLoader.southBobAnimation;
		southEastBobAnimation = AssetLoader.southEastBobAnimation;
		eastBobAnimation = AssetLoader.eastBobAnimation;
		northEastBobAnimation = AssetLoader.northEastBobAnimation;
		northBobAnimation = AssetLoader.northBobAnimation;
		northWestBobAnimation = AssetLoader.northWestBobAnimation;
		westBobAnimation = AssetLoader.westBobAnimation;
		southWestBobAnimation = AssetLoader.southWestBobAnimation;
	}
	
	private void initGameObjects(){
		bob = world.getBob();
		enemy = world.getEnemy();
	}
	
	public void render(float runTime) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		moveCamera();
		
		mapRenderer.setView(cam);
		mapRenderer.render();
		
		batcher.setProjectionMatrix(cam.combined);
		renderBob(runTime);
		renderEnemy(runTime);
		



	}

	private void renderBob(float runTime){
		//Renders collision shape for debugging purposes
		/*
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(70,70,1780,940);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for(Rectangle rect: bob.getTiles()){
			shapeRenderer.rect(rect.x,rect.y,rect.width,rect.height);
		}
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(bob.getPosition().x-10,bob.getPosition().y,50,50);
		

		shapeRenderer.end();
		*/
		
		batcher.begin();
		if(bob.getState() == Bob.STATE_ALIVE){
			switch(bob.getDirection()){
				case EAST:
					batcher.draw(eastBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTH:
					batcher.draw(northBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTHEAST:
					batcher.draw(northEastBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTHWEST:
					batcher.draw(northWestBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTH:
					batcher.draw(southBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTHEAST:
					batcher.draw(southEastBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTHWEST:
					batcher.draw(southWestBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case WEST:
					batcher.draw(westBob,bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				default:
					break;
			}
		} else if (bob.getState() == Bob.STATE_RUNNING){
			switch(bob.getDirection()){
				case EAST:
					batcher.draw(eastBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTH:
					batcher.draw(northBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTHEAST:
					batcher.draw(northEastBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case NORTHWEST:
					batcher.draw(northWestBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTH:
					batcher.draw(southBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTHEAST:
					batcher.draw(southEastBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case SOUTHWEST:
					batcher.draw(southWestBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				case WEST:
					batcher.draw(westBobAnimation.getKeyFrame(runTime),
							bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
					break;
				default:
					break;
			
			}
		}
		batcher.end();
	}
	
	private void renderEnemy(float runTime){
		batcher.begin();
		batcher.setColor(180/255f,0,0,1f);
		if(enemy.getState() == enemy.STATE_ALIVE){
			switch(enemy.getDirection()){
				case EAST:
					batcher.draw(eastBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTH:
					batcher.draw(northBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTHEAST:
					batcher.draw(northEastBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTHWEST:
					batcher.draw(northWestBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTH:
					batcher.draw(southBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTHEAST:
					batcher.draw(southEastBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTHWEST:
					batcher.draw(southWestBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case WEST:
					batcher.draw(westBob,enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				default:
					break;
			}
		} else if (enemy.getState() == enemy.STATE_RUNNING){
			switch(enemy.getDirection()){
				case EAST:
					batcher.draw(eastBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTH:
					batcher.draw(northBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTHEAST:
					batcher.draw(northEastBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case NORTHWEST:
					batcher.draw(northWestBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTH:
					batcher.draw(southBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTHEAST:
					batcher.draw(southEastBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case SOUTHWEST:
					batcher.draw(southWestBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				case WEST:
					batcher.draw(westBobAnimation.getKeyFrame(runTime),
							enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
					break;
				default:
					break;
			
			}
		}
		batcher.end();
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.rect(enemy.getPosition().x,enemy.getPosition().y,50,50);
	}
	
	public void moveCamera(){
		cam.position.set(bob.getPosition().x,bob.getPosition().y,0);
		cam.update();
	}
	

}
