package com.androidboys.spellarena.gameworld;

import java.util.ArrayList;
import java.util.Map;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Boomerang;
import com.androidboys.spellarena.model.DummyBlinkObject;
import com.androidboys.spellarena.model.Firewall;
import com.androidboys.spellarena.model.Laser;
import com.androidboys.spellarena.model.Projectile;
import com.androidboys.spellarena.model.Sunstrike;
import com.androidboys.spellarena.model.Sword;
import com.androidboys.spellarena.model.Thunderstorm;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.Gdx;
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

// TODO: Auto-generated Javadoc
/**
 * The Class GameRenderer.
 */
public class GameRenderer {

	/** The Constant TAG. */
	private static final String TAG = "Game Renderer";

	/** The world. */
	private GameWorld world;

	/** The cam. */
	private OrthographicCamera cam;

	/** The batcher. */
	private SpriteBatch batcher;

	/** The shape renderer. */
	private ShapeRenderer shapeRenderer;

	/** The bob. */
	private Bob bob;

	/** The enemies. */
	private ArrayList<Bob> enemies;

	/** The map. */
	private TiledMap map;

	/** The map renderer. */
	private TiledMapRenderer mapRenderer;

	//Game Assets
	/** The south east bob. */
	private TextureRegion southBob, southWestBob, westBob, northWestBob,
	northBob, northEastBob, eastBob, southEastBob;

	/** The bob death animation. */
	private Animation southBobAnimation, southWestBobAnimation, westBobAnimation, northWestBobAnimation,
	northBobAnimation, northEastBobAnimation, eastBobAnimation, southEastBobAnimation, bobDeathAnimation;

	/** The sunstrike animation. */
	private Animation tornadoAnimation, shieldAnimation, swordAnimation, dustAnimation, 
	reverseDustAnimation, laserAnimation, fireAnimation, energyAnimation, sunstrikeAnimation;

	/** The thunderstorm animation. */
	TextureRegion[] thunderstormAnimation;

	/** The default shader. */
	private ShaderProgram defaultShader;

	/** The final shader. */
	private ShaderProgram finalShader;

	/** The ambient color. */
	private Vector3 ambientColor;

	/** The current shader. */
	private ShaderProgram currentShader;

	/** The light. */
	private Texture light;

	/** The fbo. */
	private FrameBuffer fbo;

	/** The ambient intensity. */
	private float ambientIntensity = 0.3f;

	/** The height. */
	private int height;

	/** The width. */
	private int width;

	/** The green health bar. */
	private Texture greenHealthBar;

	/** The red health bar. */
	private Texture redHealthBar;

	/** The is debug on. */
	private Boolean isDebugOn = false;

	/** The Constant bright. */
	private final static Vector3 bright = new Vector3(0.7f, 0.7f, 0.9f);
	// Load shaders from text files
	/** The Constant vertexShader. */
	private final static String vertexShader = Gdx.files.internal("data/shaders/vertexShader.glsl").readString();

	/** The Constant defaultPixelShader. */
	private final static String defaultPixelShader = Gdx.files.internal("data/shaders/defaultPixelShader.glsl").readString();

	/** The Constant finalPixelShader. */
	private final static String finalPixelShader =  Gdx.files.internal("data/shaders/pixelShader.glsl").readString();

	/**
	 * Instantiates a new game renderer.
	 *
	 * @param batcher batcher to render the sprites
	 * @param world reference to the game world
	 */
	public GameRenderer(SpriteBatch batcher, GameWorld world) {
		this.world = world;
		//Create a new camera
		this.setCam(new OrthographicCamera(960,540));
		//Initialization of camera
		getCam().position.set(1920/2, 1080/2, 0);
		this.enemies = new ArrayList<Bob>();
		this.batcher = batcher;
		shapeRenderer = new ShapeRenderer();

		initAssets();
		initShaders();
	}

	/**
	 * Initializes the shaders.
	 */
	private void initShaders() {
		ShaderProgram.pedantic = false;
		defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
		finalShader = new ShaderProgram(vertexShader, finalPixelShader);
		currentShader = finalShader;
		ambientColor = bright;

		finalShader.begin();
		finalShader.setUniformi("u_lightmap", 1);
		finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
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

	/**
	 * Initializes the assets.
	 */
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

		bobDeathAnimation = AssetLoader.bobDeathAnimation;

		greenHealthBar = AssetLoader.greenTexture;
		redHealthBar = AssetLoader.redTexture;

		dustAnimation = AssetLoader.dustAnimation;
		reverseDustAnimation = AssetLoader.reverseDustAnimation;
		tornadoAnimation = AssetLoader.tornadoAnimation;
		shieldAnimation = AssetLoader.shieldAnimation;
		swordAnimation = AssetLoader.swordAnimation;
		laserAnimation = AssetLoader.laserAnimation;
		thunderstormAnimation = AssetLoader.thunderstormAnimation;
		fireAnimation = AssetLoader.fireAnimation;
		energyAnimation = AssetLoader.energyAnimation;
		sunstrikeAnimation = AssetLoader.sunstrikeAnimation;
	}

	/**
	 * Initializes the game objects.
	 */
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


	/**
	 * Render. This method is called to render most of the game objects in the game world like spells
	 *
	 * @param runTime the run time
	 */
	public void render(float runTime) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		moveCamera();

		if(bob!=null)renderLight();

		batcher.setShader(currentShader);
		mapRenderer.setView(getCam());
		mapRenderer.render();

		batcher.setProjectionMatrix(getCam().combined);
		if(bob!=null)renderBob(runTime);
		if(!enemies.isEmpty())renderEnemy(runTime);

		synchronized (world.getGameObjects()) {

			for(Object o: world.getGameObjects()){
				if(o instanceof Projectile){
					renderProjectile(runTime,(Projectile)o);
				}
				else if(o instanceof Sword){
					renderSword(runTime,(Sword)o);
				}
				else if(o instanceof DummyBlinkObject){
					renderBlink(runTime, (DummyBlinkObject)o);
				}
				else if(o instanceof Laser){
					renderLaser(runTime, (Laser)o);
				}
				else if(o instanceof Thunderstorm){
					renderThunderstorm(runTime, (Thunderstorm)o);
				}
				else if(o instanceof Sunstrike){
					renderSunstrike(runTime, (Sunstrike)o);
				}
				else if(o instanceof Boomerang){
					renderBoomerang(runTime, (Boomerang)o);
				}
				else if(o instanceof Firewall){
					renderFirewall(runTime, (Firewall)o);
				}
			}
		}
	}

	/**
	 * Render firewall.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the firewall
	 */
	private void renderFirewall(float runTime, Firewall o) {
		batcher.begin();
		batcher.draw(fireAnimation.getKeyFrame(runTime),
				o.getPosition().x,o.getPosition().y,50f,100f);
		batcher.end();

		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getRectangle().x,
					o.getRectangle().y,
					o.getRectangle().width,
					o.getRectangle().height);
			shapeRenderer.end();
		}
	}


	/**
	 * Render boomerang.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the boomerang
	 */
	private void renderBoomerang(float runTime, Boomerang o) {
		batcher.begin();
		batcher.draw(swordAnimation.getKeyFrame(runTime),
				o.getPosition().x-50,o.getPosition().y-50,150f,150f);
		batcher.end();
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getPosition().x-35,o.getPosition().y-25,110f,100f);
			shapeRenderer.end();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getDestination().x,o.getDestination().y,5,5);
			shapeRenderer.end();
		}
	}

	/**
	 * Render thunderstorm.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the thunderstorm
	 */
	private void renderThunderstorm(float runTime, Thunderstorm o) {
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getPosition().x-100,o.getPosition().y-75,200f,112f);
			shapeRenderer.end();
		}
		batcher.begin();
		batcher.draw(thunderstormAnimation[(int) ((Math.random()*4)%4)],
				o.getPosition().x-125,o.getPosition().y-75,100f,200f);
		batcher.draw(thunderstormAnimation[(int) ((Math.random()*4)%4)],
				o.getPosition().x+25,o.getPosition().y-75,100f,200f);
		batcher.draw(thunderstormAnimation[(int) ((Math.random()*4)%4)],
				o.getPosition().x-50,o.getPosition().y-25,100f,200f);
		batcher.draw(thunderstormAnimation[(int) ((Math.random()*4)%4)],
				o.getPosition().x+25,o.getPosition().y+25,100f,200f);
		batcher.draw(thunderstormAnimation[(int) ((Math.random()*4)%4)],
				o.getPosition().x-125,o.getPosition().y+25,100f,200f);

		batcher.end();
	}

	/**
	 * Render sword.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the sword
	 */
	private void renderSword(float runTime, Sword o) {
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getPosition().x-35,o.getPosition().y-15,105f,90f);
			shapeRenderer.end();
		}
		batcher.begin();
		batcher.draw(tornadoAnimation.getKeyFrame(runTime),
				o.getPosition().x-50,o.getPosition().y-50,150f,150f);
		batcher.end();
	}


	/**
	 * Render projectile.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the projectile
	 */
	private void renderProjectile(float runTime, Projectile o) {
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getPosition().x-35,o.getPosition().y-25,110f,100f);
			shapeRenderer.end();
		}
		batcher.begin();
		batcher.draw(energyAnimation.getKeyFrame(runTime),
				o.getPosition().x-25,o.getPosition().y-25,50f,50f);
		batcher.end();

	}

	/**
	 * Render blink.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of blinking
	 */
	private void renderBlink(float runTime, DummyBlinkObject o){
		Bob blinkOwner = world.getPlayerModel(o.getUsername());
		batcher.begin();
		batcher.draw(dustAnimation.getKeyFrame(runTime), blinkOwner.getPosition().x-25f,blinkOwner.getPosition().y-25f,75f,75f);
		batcher.draw(reverseDustAnimation.getKeyFrame(runTime), o.getPosition().x-25f,o.getPosition().y-25f,75f,75f);
		batcher.end();
	}

	/**
	 * Render sunstrike.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the instance of the sunstrike
	 */
	private void renderSunstrike(float runTime, Sunstrike o){
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(getCam().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(o.getPosition().x-50,o.getPosition().y-50,100f,100f);
			shapeRenderer.end();
		}
		batcher.begin();		
		batcher.draw(sunstrikeAnimation.getKeyFrame(runTime), o.getPosition().x-50f,o.getPosition().y-50f,100f,100f);
		batcher.end();
	}

	/**
	 * Render laser.
	 *
	 * @param runTime runtime(like delta)
	 * @param o the laser object
	 */
	private void renderLaser(float runTime, Laser o){
		Bob laserOwner = world.getPlayerModel(o.getUsername());
		if (isDebugOn){
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(laserOwner.getPosition().x+20,laserOwner.getPosition().y-10,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+35,laserOwner.getPosition().y-25f,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+52,laserOwner.getPosition().y-42.5f,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+70,laserOwner.getPosition().y-65.5f,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+90,laserOwner.getPosition().y-84f,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+110,laserOwner.getPosition().y-107.5f,20f,25f);
			shapeRenderer.rect(laserOwner.getPosition().x+130,laserOwner.getPosition().y-120f,20f,25f);
			shapeRenderer.end();
		}
		int sum = 0;
		batcher.begin();
		sum += Gdx.graphics.getDeltaTime();
		switch(laserOwner.getDirection()){
		case EAST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+25f,laserOwner.getPosition().y, 0, 0, 198f, 25f, 1, 1.5f, 0);
			break;
		case NORTH:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+25f,laserOwner.getPosition().y+42f, 0, 0, 198f, 25f, 1, 1.5f, 90);
			break;
		case NORTHEAST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+30f,laserOwner.getPosition().y, 0, 0, 198f, 25f, 1, 1.5f, 45);
			break;
		case NORTHWEST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+12.5f,laserOwner.getPosition().y+22f, 0, 0, 198f, 25f, 1, 1.5f, 135);
			break;
		case SOUTH:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x,laserOwner.getPosition().y+12f, 0, 0, 198f, 25f, 1, 1.5f, 270);
			break;
		case SOUTHEAST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+5f,laserOwner.getPosition().y+8f, 0, 0, 198f, 25f, 1, 1.5f, 315);
			break;
		case SOUTHWEST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x+5f,laserOwner.getPosition().y+28f, 0, 0, 198f, 25f, 1, 1.5f, 225);
			break;
		case WEST:
			batcher.draw(laserAnimation.getKeyFrame(sum), laserOwner.getPosition().x,laserOwner.getPosition().y+28f, 0, 0, 198f, 25f, 1, 1.5f, 180);
			break;
		default:
			break;
		}

		batcher.end();

	}

	/**
	 * Render light.
	 */
	private void renderLight() {
		fbo.begin();
		fbo.getColorBufferTexture().bind(1);
		batcher.setShader(defaultShader);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.begin();
		light.bind(0);
		batcher.draw(light, 
				(getCam().position.x - 960*bob.getLightRadius()),
				(getCam().position.y - 540*bob.getLightRadius()), 1920f*bob.getLightRadius(),1080f*bob.getLightRadius());
		batcher.end();
		fbo.end();
	}


	/**
	 * Render bob.
	 *
	 * @param runTime runtime(like delta)
	 */
	private void renderBob(float runTime){
		//Renders collision shape for debugging purposes
		if (isDebugOn){
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
			shapeRenderer.rect(bob.getCollisionEdge().x,
					bob.getCollisionEdge().y,
					bob.getCollisionEdge().width,
					bob.getCollisionEdge().height);
			shapeRenderer.end();
		}
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
		} else if(bob.getState() == Bob.STATE_DEAD){
			batcher.draw(bobDeathAnimation.getKeyFrame(runTime),
					bob.getPosition().x-25f,bob.getPosition().y-25f,75f,75f);
		}
		if(bob.isInvulnerable()){
			batcher.draw(shieldAnimation.getKeyFrame(runTime),
					bob.getPosition().x-38f,bob.getPosition().y-35f,100f,100f);
		}
		batcher.end();
		float healthPercentage = bob.getHealth()/Bob.MAX_HEALTH;
		batcher.begin();
		batcher.draw(greenHealthBar,
				bob.getPosition().x-10f,
				bob.getPosition().y+50f,
				50*healthPercentage,5);
		batcher.draw(redHealthBar,
				bob.getPosition().x-10f + 50*healthPercentage,
				bob.getPosition().y+50f,
				50*(1-healthPercentage),5);
		batcher.end();
	}

	/**
	 * Render enemy.
	 *
	 * @param runTime runtime(like delta)
	 */
	private void renderEnemy(float runTime){
		for(Bob enemy: enemies){
			batcher.begin();
			if(enemy.getState() == Bob.STATE_ALIVE && enemy.getPlayerName()!=UserSession.getInstance().getUserName()){
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
			} else if(enemy.getState() == Bob.STATE_DEAD){
				batcher.draw(bobDeathAnimation.getKeyFrame(runTime),
						enemy.getPosition().x-25f,enemy.getPosition().y-25f,75f,75f);
			}
			if(enemy.isInvulnerable()){
				batcher.draw(shieldAnimation.getKeyFrame(runTime),
						enemy.getPosition().x-38f,enemy.getPosition().y-35f,100f,100f);
			}
			batcher.end();
			float healthPercentage = enemy.getHealth()/Bob.MAX_HEALTH;
			batcher.begin();
			batcher.draw(greenHealthBar,
					enemy.getPosition().x-10f,
					enemy.getPosition().y+50f,
					50*healthPercentage,5);
			batcher.draw(redHealthBar,
					enemy.getPosition().x-10f + 50*healthPercentage,
					enemy.getPosition().y+50f,
					50*(1-healthPercentage),5);
			batcher.end();
			if (isDebugOn){
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(enemy.getPosition().x,enemy.getPosition().y,50,50);
			}
		}
	}

	/**
	 * Move camera.
	 */
	public void moveCamera(){
		if(bob != null){
			getCam().position.set(bob.getPosition().x+15f,bob.getPosition().y+25f,0);
		}
		getCam().update();
	}

	/**
	 * Removes the player from render.
	 *
	 * @param playerName the player name
	 */
	public void removePlayer(String playerName) {
		enemies.remove(world.getPlayerModel(playerName));
	}

	/**
	 * Gets the cam.
	 *
	 * @return the cam
	 */
	public OrthographicCamera getCam() {
		return cam;
	}

	/**
	 * Sets the cam.
	 *
	 * @param cam the new cam
	 */
	public void setCam(OrthographicCamera cam) {
		this.cam = cam;
	}
}
