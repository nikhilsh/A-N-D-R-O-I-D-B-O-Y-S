package com.androidboys.spellarena.gameworld;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.DummyBlinkObject;
import com.androidboys.spellarena.model.GameObject;
import com.androidboys.spellarena.model.Spell;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.model.Sword;
import com.androidboys.spellarena.model.Tornado;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class GameRenderer {

	private static final String TAG = "Game Renderer";
	private GameWorld world;
	private OrthographicCamera cam;

	private SpriteBatch batcher;
	private ShapeRenderer shapeRenderer;

	private Bob bob;
	private ArrayList<Bob> enemies;

	private Spells spell;

	private TiledMap map;
	private TiledMapRenderer mapRenderer;

	//Game Assets
	private TextureRegion southBob, southWestBob, westBob, northWestBob,
	northBob, northEastBob, eastBob, southEastBob;
	private Animation southBobAnimation, southWestBobAnimation, westBobAnimation, northWestBobAnimation,
	northBobAnimation, northEastBobAnimation, eastBobAnimation, southEastBobAnimation;

	private Animation tornadoAnimation, shieldAnimation, swordAnimation, dustAnimation, reverseDustAnimation;

	private ShaderProgram defaultShader;
	private ShaderProgram finalShader;
	private Vector3 ambientColor;
	private ShaderProgram currentShader;
	private Texture light;
	private FrameBuffer fbo;
	private float ambientIntensity = 0.3f;
	private int height;
	private int width;

	private final static Vector3 bright = new Vector3(0.7f, 0.7f, 0.9f);
	// Load shaders from text files
	private final static String vertexShader = Gdx.files.internal("data/shaders/vertexShader.glsl").readString();
	private final static String defaultPixelShader = Gdx.files.internal("data/shaders/defaultPixelShader.glsl").readString();
	private final static String finalPixelShader =  Gdx.files.internal("data/shaders/pixelShader.glsl").readString();

	public GameRenderer(SpriteBatch batcher, GameWorld world) {
		this.world = world;
		//Create a new camera
		this.cam = new OrthographicCamera(960,540);
		//Initialization of camera
		cam.position.set(1920/2, 1080/2, 0);
		this.enemies = new ArrayList<Bob>();
		this.batcher = batcher;
		shapeRenderer = new ShapeRenderer();

		initAssets();
		initShaders();
	}

	private void initShaders() {
		ShaderProgram.pedantic = false;
		defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
		finalShader = new ShaderProgram(vertexShader, finalPixelShader);
		currentShader = finalShader;
		ambientColor = bright;

		finalShader.begin();
		finalShader.setUniformi("u_lightmap", 1);
		finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity );
		finalShader.end();
		// Image for spot light 

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		light = new Texture(Gdx.files.internal("data/shaders/light.png"));
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);

		finalShader.begin();
		finalShader.setUniformf("resolution", width, height);
		finalShader.end();

		mapRenderer = new OrthogonalTiledMapRenderer(map,batcher);
	}

	private void initAssets(){
		map = AssetLoader.map;

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

		dustAnimation = AssetLoader.dustAnimation;
		reverseDustAnimation = AssetLoader.reverseDustAnimation;
		tornadoAnimation = AssetLoader.tornadoAnimation;
		shieldAnimation = AssetLoader.shieldAnimation;
		swordAnimation = AssetLoader.swordAnimation;
	}

	public void initGameObjects(){
		Gdx.app.log(TAG,"Initialising game objects");
		Map<String,Bob> bobs = world.getPlayerModels();
		for(String name: bobs.keySet()){
			Gdx.app.log(TAG,"initGameObjects "+name.equals(UserSession.getInstance().getUserName()));
			if(name.equals(UserSession.getInstance().getUserName())){
				bob = bobs.get(name);
			}
			else{
				enemies.add(bobs.get(name));
			}
		}
	}


	public void render(float runTime) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		moveCamera();

		//		batcher.setProjectionMatrix(cam.combined);
		if(bob!=null)renderLight();

		batcher.setShader(currentShader);
		mapRenderer.setView(cam);
		mapRenderer.render();

		batcher.setProjectionMatrix(cam.combined);
		if(bob!=null)renderBob(runTime);
		if(!enemies.isEmpty())renderEnemy(runTime);

		for(Object o: world.getGameObjects()){
			if(o instanceof Tornado){
				renderTornado(runTime,(Tornado)o);
			}
			else if(o instanceof Sword){
				renderSword(runTime,(Sword)o);
			}
			else if(o instanceof DummyBlinkObject){
				renderBlink(runTime, (DummyBlinkObject)o);
			}
		}		
	}

	private void renderSword(float runTime, Sword o) {
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(o.getPosition().x-35,o.getPosition().y-15,105f,90f);
		shapeRenderer.end();
		batcher.begin();
		//		batcher.setColor(Color.WHITE);
		batcher.draw(swordAnimation.getKeyFrame(runTime),
				o.getPosition().x-50,o.getPosition().y-50,150f,150f);
		batcher.end();
	}

	private void renderLight() {
		fbo.begin();
		fbo.getColorBufferTexture().bind(1);
		batcher.setShader(defaultShader);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.begin();
		light.bind(0);
		batcher.draw(light, 
				(cam.position.x - 960*bob.getLightRadius()),
				(cam.position.y - 540*bob.getLightRadius()), 1920f*bob.getLightRadius(),1080f*bob.getLightRadius());
		batcher.end();
		fbo.end();
	}

	private void renderTornado(float runTime, Tornado o) {
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(o.getPosition().x-35,o.getPosition().y-25,110f,100f);
		shapeRenderer.end();
		batcher.begin();
		//		batcher.setColor(Color.WHITE);
		batcher.draw(tornadoAnimation.getKeyFrame(runTime),
				o.getPosition().x-50,o.getPosition().y-50,150f,150f);
		batcher.end();

	}

	private void renderBlink(float runTime, DummyBlinkObject o){
		
		if (bob.getPlayerName() == o.getUsername()){
			batcher.begin();

			batcher.draw(dustAnimation.getKeyFrame(runTime), bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
			batcher.draw(dustAnimation.getKeyFrame(runTime), o.getPosition().x-25f,o.getPosition().y-25f,75f,75f);

			batcher.end();
		}
	}

	private void renderBob(float runTime){
		//Renders collision shape for debugging purposes
		//
		//				shapeRenderer.setProjectionMatrix(cam.combined);
		//				shapeRenderer.begin(ShapeType.Line);
		//				shapeRenderer.setColor(Color.BLUE);
		//				shapeRenderer.rect(70,70,1780,940);
		//				shapeRenderer.end();
		//				
		//				shapeRenderer.begin(ShapeType.Filled);
		//				shapeRenderer.setColor(Color.RED);
		//				for(Rectangle rect: bob.getTiles()){
		//					shapeRenderer.rect(rect.x,rect.y,rect.width,rect.height);
		//				}
		//				shapeRenderer.setColor(Color.BLUE);
		//				shapeRenderer.rect(bob.getCollisionEdge().x,
		//						bob.getCollisionEdge().y,
		//						bob.getCollisionEdge().width,
		//						bob.getCollisionEdge().height);
		//				shapeRenderer.end();

		//dark pact
		//		shapeRenderer.begin(ShapeType.Line);
		//		shapeRenderer.setColor(Color.BLACK);
		//		shapeRenderer.circle(bob.getPosition().x, bob.getPosition().y, 300);
		//				shapeRenderer.end();


		batcher.begin();
		//		batcher.setColor(Color.WHITE);
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
		if(bob.isInvulnerable()){
			batcher.draw(shieldAnimation.getKeyFrame(runTime),
					bob.getPosition().x-38f,bob.getPosition().y-35f,100f,100f);
		}
		batcher.end();
	}

	private void renderEnemy(float runTime){
		batcher.begin();
		//		batcher.setColor(255/255f,128/255f,128/255f,1f);
		for(Bob enemy: enemies){
			if(enemy.getState() == Bob.STATE_ALIVE&&enemy.getPlayerName()!=UserSession.getInstance().getUserName()){
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
			} else if (enemy.getState() == Bob.STATE_RUNNING){
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
			if(enemy.isInvulnerable()){
				batcher.draw(shieldAnimation.getKeyFrame(runTime),
						enemy.getPosition().x-38f,enemy.getPosition().y-35f,100f,100f);
			}
		}
		batcher.end();
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.rect(enemy.getPosition().x,enemy.getPosition().y,50,50);
	}

	public void moveCamera(){
		if(bob != null){
			cam.position.set(bob.getPosition().x+15f,bob.getPosition().y+25f,0);
		}
		cam.update();
	}

	public void removePlayer(String playerName) {
		enemies.remove(world.getPlayerModel(playerName));
	}

}
