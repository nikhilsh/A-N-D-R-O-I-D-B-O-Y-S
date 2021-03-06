package com.androidboys.spellarena.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetLoader {

	private static final String TAG = "AssetLoader";

	public static AssetManager manager = new AssetManager();
	
	public static Texture backgroundTexture;
	public static TextureRegion backgroundRegion;
	
	public static Texture loadingTexture;
	
	public static Texture questionTexture, musicOnTexture, musicOffTexture;
	
	public static Texture quasTexture, wexTexture, exortTexture;
	
	public static Texture charTexture;
	public static TextureRegion northBob, eastBob, southBob, westBob, 
		northEastBob, southEastBob, southWestBob, northWestBob;
	public static Animation northBobAnimation, eastBobAnimation, southBobAnimation, westBobAnimation, 
		northEastBobAnimation, southEastBobAnimation, southWestBobAnimation, northWestBobAnimation;
	public static Animation bobDeathAnimation;
	
	public static Texture tornadoTexture;
	public static Animation tornadoAnimation;
	
	public static Texture shieldTexture;
	public static Animation shieldAnimation;
	
	public static Texture swordTexture;
	public static Animation swordAnimation;
	
	public static Texture laserTexture;
	public static Animation laserAnimation;
		
	public static Texture dustTexture;
	public static Animation dustAnimation;
	public static Animation reverseDustAnimation;

	public static Texture thunderstormTexture;
	public static TextureRegion[] thunderstormAnimation;
	
	public static Texture fireTexture;
	public static Animation fireAnimation;
	
	public static Texture sunstrikeTexture;
	public static Animation sunstrikeAnimation;
	
	public static Texture energyTexture;
	public static Animation energyAnimation;
	
	public static Texture greenTexture, redTexture;

	public static BitmapFont header, playText, playTextSmall, swordText, parchmentText, smallParchmentText;
	
	public static TiledMap map;
	
	public static Preferences prefs;


	
	/**
	 * Queue loading.
	 */
	public static void queueLoading(){
		loadPreferences();
		
		manager.load("images/bg.jpg", Texture.class);
		manager.load("images/ParchmentLabel.png", Texture.class);
		manager.load("images/question.png", Texture.class);
		manager.load("images/music_on.png", Texture.class);
		manager.load("images/music_off.png", Texture.class);
		
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Nearest;
		param.magFilter = TextureFilter.Nearest;
		manager.load("images/quas.png", Texture.class);
		manager.load("images/wex.png", Texture.class);
		manager.load("images/exort.png", Texture.class);
		manager.load("sprites/wizard.png", Texture.class, param);
		
		manager.load("sprites/tornado.PNG", Texture.class, param);
		manager.load("sprites/shield.png", Texture.class, param);
		manager.load("sprites/sword.png", Texture.class, param);
		manager.load("sprites/laser.png", Texture.class, param);
		manager.load("sprites/explode.png", Texture.class, param);
		manager.load("sprites/lightning.png", Texture.class, param);
		manager.load("sprites/fire.GIF", Texture.class, param);
		manager.load("sprites/energy.png", Texture.class, param);
		manager.load("sprites/explosion.png", Texture.class, param);
		
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
		manager.load("fonts/header.TTF",FreeTypeFontGenerator.class);
		manager.load("fonts/play.TTF",FreeTypeFontGenerator.class);
		manager.load("fonts/Fantasy.ttf",FreeTypeFontGenerator.class);
		
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("maps/Dungeon.tmx",TiledMap.class);
	}
	
	/**
	 * Sets the main menu resources.
	 */
	public static void setMainMenuResources(){
		Gdx.app.log(TAG, "Setting main menu resources");
		if (backgroundTexture == null){
			backgroundTexture = manager.get("images/bg.jpg",Texture.class);
		}
		if (loadingTexture == null){
			loadingTexture = manager.get("images/ParchmentLabel.png",Texture.class);
		}
		backgroundRegion = new TextureRegion(backgroundTexture);
		if (questionTexture == null){
			questionTexture = manager.get("images/question.png",Texture.class);
		}
		if (musicOnTexture == null){
			musicOnTexture = manager.get("images/music_on.png",Texture.class);
		}
		if (musicOffTexture == null){
			musicOffTexture = manager.get("images/music_off.png",Texture.class);
		}
		FreeTypeFontParameter fontPars;
		if (header == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 60;
			fontPars.color = new Color(199/255f, 176/255f, 151/255f, 1);
			fontPars.borderColor = Color.BLACK;
			fontPars.borderWidth = 1;
			header = manager.get("fonts/header.TTF",FreeTypeFontGenerator.class).generateFont(fontPars);
			Gdx.app.log(TAG, "Setting fonts");
		}
		if (playText == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 32;
			fontPars.color = Color.WHITE;
			fontPars.borderColor = Color.BLACK;
			fontPars.borderWidth = 1;
			playText = manager.get("fonts/play.TTF",FreeTypeFontGenerator.class).generateFont(fontPars);
		}
		if (parchmentText == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 28;
			fontPars.color = Color.BLACK;
			fontPars.borderColor = Color.GRAY;
			fontPars.borderWidth = 1;
			parchmentText = manager.get("fonts/play.TTF",FreeTypeFontGenerator.class).generateFont(fontPars);
		}
		if (playTextSmall == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 18;
			fontPars.color = Color.WHITE;
			fontPars.borderColor = Color.BLACK;
			fontPars.borderWidth = 1;
			playTextSmall = manager.get("fonts/play.TTF",FreeTypeFontGenerator.class).generateFont(fontPars);
		}
		if (smallParchmentText == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 18;
			fontPars.color = Color.BLACK;
			fontPars.borderColor = Color.GRAY;
			fontPars.borderWidth = 1;
			smallParchmentText = manager.get("fonts/play.TTF",FreeTypeFontGenerator.class).generateFont(fontPars);
		}
		if (swordText == null){
			fontPars = new FreeTypeFontParameter();
			fontPars.size = 60;
			fontPars.color = new Color(199/255f, 176/255f, 151/255f, 1);
			fontPars.borderColor = Color.BLACK;
			fontPars.borderWidth = 1;
			swordText = manager.get("fonts/Fantasy.ttf",FreeTypeFontGenerator.class).generateFont(fontPars);
		}
		if(quasTexture == null){
			quasTexture = manager.get("images/quas.png",Texture.class);
		}
		if(wexTexture == null){
			wexTexture = manager.get("images/wex.png",Texture.class);
		}
		if(exortTexture == null){
			exortTexture = manager.get("images/exort.png",Texture.class);
		}
	}
	
	/**
	 * Load preferences like audio
	 */
	public static void loadPreferences() {
		prefs = Gdx.app.getPreferences("Spell Arena");
		if(!prefs.contains("audio")){
			prefs.putBoolean("audio",false);
		}
	}

	/**
	 * Sets the game resources.
	 */
	public static void setGameResources(){
		Gdx.app.log(TAG, "Setting game resources");
		if(charTexture == null){
			charTexture = manager.get("sprites/wizard.png",Texture.class);
		}
		if(tornadoTexture == null){
			tornadoTexture = manager.get("sprites/tornado.PNG",Texture.class);
		}
		if(shieldTexture == null){
			shieldTexture = manager.get("sprites/shield.png", Texture.class);
		}
		if(swordTexture == null){
			swordTexture = manager.get("sprites/sword.png",Texture.class);
		}
		if(dustTexture == null){
			dustTexture = manager.get("sprites/explode.png",Texture.class);
		}
		if(laserTexture == null){
			laserTexture = manager.get("sprites/laser.png", Texture.class);
		}
		if(thunderstormTexture == null){
			thunderstormTexture = manager.get("sprites/lightning.png",Texture.class);
		}
		if(fireTexture == null){
			fireTexture = manager.get("sprites/fire.GIF",Texture.class);
		}
		if(energyTexture == null){
			energyTexture = manager.get("sprites/energy.png",Texture.class);
		}
		if(sunstrikeTexture == null){
			sunstrikeTexture = manager.get("sprites/explosion.png",Texture.class);
		}
		if(map == null){
			map = manager.get("maps/Dungeon.tmx",TiledMap.class);
		}
		Pixmap greenPixmap = new Pixmap(5, 5, Format.RGBA8888);
		greenPixmap.setColor(Color.GREEN);
		greenPixmap.fill();
		greenTexture = new Texture(greenPixmap);
		Pixmap redPixmap = new Pixmap(5, 5, Format.RGBA8888);
		redPixmap.setColor(Color.RED);
		redPixmap.fill();
		redTexture = new Texture(redPixmap);
		loadSprites();
		loadAnimations();
	}
	
	/**
	 * Update.
	 *
	 * @return true, if there is an update
	 */
	public static boolean update(){
		return manager.update();
	}
	
	/**
	 * Load animations.
	 */
	private static void loadAnimations() {
		northBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 0, 155, 30, 30),
				new TextureRegion(charTexture, 30, 155, 30, 30),
				new TextureRegion(charTexture, 60, 155, 30, 30),
				new TextureRegion(charTexture, 90, 155, 30, 30),
				new TextureRegion(charTexture, 120, 155, 30, 30)
			});
		
		northEastBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 150, 125, 30, 30),
				new TextureRegion(charTexture, 180, 125, 30, 30),
				new TextureRegion(charTexture, 210, 125, 30, 30),
				new TextureRegion(charTexture, 240, 125, 30, 30),
				new TextureRegion(charTexture, 269, 125, 30, 30)
			});
		
		eastBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 150, 95, 30, 30),
				new TextureRegion(charTexture, 180, 95, 30, 30),
				new TextureRegion(charTexture, 210, 95, 30, 30),
				new TextureRegion(charTexture, 240, 95, 30, 30),
				new TextureRegion(charTexture, 269, 95, 30, 30)
			});
		
		southEastBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 150, 65, 30, 30),
				new TextureRegion(charTexture, 180, 65, 30, 30),
				new TextureRegion(charTexture, 210, 65, 30, 30),
				new TextureRegion(charTexture, 240, 65, 30, 30),
				new TextureRegion(charTexture, 269, 65, 30, 30)
			});
		
		southBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 150, 35, 30, 30),
				new TextureRegion(charTexture, 180, 35, 30, 30),
				new TextureRegion(charTexture, 210, 35, 30, 30),
				new TextureRegion(charTexture, 240, 35, 30, 30),
				new TextureRegion(charTexture, 269, 35, 30, 30)
			});
		
		southWestBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 0, 65, 30, 30),
				new TextureRegion(charTexture, 30, 65, 30, 30),
				new TextureRegion(charTexture, 60, 65, 30, 30),
				new TextureRegion(charTexture, 90, 65, 30, 30),
				new TextureRegion(charTexture, 120, 65, 30, 30)
			});
		
		westBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 0, 95, 30, 30),
				new TextureRegion(charTexture, 30, 95, 30, 30),
				new TextureRegion(charTexture, 60, 95, 30, 30),
				new TextureRegion(charTexture, 90, 95, 30, 30),
				new TextureRegion(charTexture, 120, 95, 30, 30)
			});
		
		northWestBobAnimation = new Animation(0.06f,new TextureRegion[]{
				new TextureRegion(charTexture, 0, 125, 30, 30),
				new TextureRegion(charTexture, 30, 125, 30, 30),
				new TextureRegion(charTexture, 60, 125, 30, 30),
				new TextureRegion(charTexture, 90, 125, 30, 30),
				new TextureRegion(charTexture, 120, 125, 30, 30)
			});
		
		bobDeathAnimation = new Animation(2.0f,new TextureRegion[]{
				new TextureRegion(charTexture, 120, 275, 30, 30),
				new TextureRegion(charTexture, 90, 275, 30, 30),
				new TextureRegion(charTexture, 60, 275, 30, 30),
				new TextureRegion(charTexture, 30, 275, 30, 30)
        });
		
		dustAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(dustTexture, 0, 0, 180, 180),
				new TextureRegion(dustTexture, 180, 0, 180, 180),
				new TextureRegion(dustTexture, 360, 0, 180, 180),
				new TextureRegion(dustTexture, 540, 0, 180, 180),
				new TextureRegion(dustTexture, 720, 0, 180, 180)
	    });
		
		reverseDustAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(dustTexture, 720, 0, 180, 180),
				new TextureRegion(dustTexture, 540, 0, 180, 180),
				new TextureRegion(dustTexture, 360, 0, 180, 180),
				new TextureRegion(dustTexture, 180, 0, 180, 180),
				new TextureRegion(dustTexture, 0, 0, 180, 180)
		});
		
		tornadoAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(tornadoTexture, 0, 0, 66, 75),
				new TextureRegion(tornadoTexture, 66, 0, 66, 75),
				new TextureRegion(tornadoTexture, 132, 0, 66, 75),
				new TextureRegion(tornadoTexture, 198, 0, 66, 75),
				new TextureRegion(tornadoTexture, 264, 0, 66, 75),
				new TextureRegion(tornadoTexture, 330, 0, 66, 75),
				new TextureRegion(tornadoTexture, 396, 0, 66, 75),
				new TextureRegion(tornadoTexture, 462, 0, 66, 75),
		});
		
		shieldAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(shieldTexture, 0, 0, 192, 192),
				new TextureRegion(shieldTexture, 192, 0, 192, 192),
				new TextureRegion(shieldTexture, 384, 0, 192, 192),
				new TextureRegion(shieldTexture, 576, 0, 192, 192),
				new TextureRegion(shieldTexture, 768, 0, 192, 192),
				new TextureRegion(shieldTexture, 0, 192, 192, 192),
				new TextureRegion(shieldTexture, 192, 192, 192, 192),
				new TextureRegion(shieldTexture, 384, 192, 192, 192),
				new TextureRegion(shieldTexture, 576, 192, 192, 192),
				new TextureRegion(shieldTexture, 768, 192, 192, 192),
				new TextureRegion(shieldTexture, 0, 384, 192, 192),
				new TextureRegion(shieldTexture, 192, 384, 192, 192),
				new TextureRegion(shieldTexture, 384, 384, 192, 192),
				new TextureRegion(shieldTexture, 576, 384, 192, 192),
				new TextureRegion(shieldTexture, 768, 384, 192, 192),
				new TextureRegion(shieldTexture, 0, 576, 192, 192),
				new TextureRegion(shieldTexture, 192, 576, 192, 192),
				new TextureRegion(shieldTexture, 384, 576, 192, 192),
				new TextureRegion(shieldTexture, 576, 576, 192, 192),
				new TextureRegion(shieldTexture, 768, 576, 192, 192),
		});
		
		swordAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(swordTexture, 0, 0, 100, 100),
				new TextureRegion(swordTexture, 100, 0, 100, 100),
				new TextureRegion(swordTexture, 200, 0, 100, 100),
				new TextureRegion(swordTexture, 300, 0, 100, 100),
				new TextureRegion(swordTexture, 200, 300, 100, 100),
		});
		

         laserAnimation = new Animation(0.1f, new TextureRegion[]{
				new TextureRegion(laserTexture, 0, 200, 198, 25),
				new TextureRegion(laserTexture, 0, 175, 198, 25),
                new TextureRegion(laserTexture, 0, 150, 198, 25),
				new TextureRegion(laserTexture, 0, 125, 198, 25),
                new TextureRegion(laserTexture, 0, 100, 198, 25),
				new TextureRegion(laserTexture, 0, 75, 198, 25),
                new TextureRegion(laserTexture, 0, 50, 198, 25),
				new TextureRegion(laserTexture, 0, 25, 198, 25),
		});

        thunderstormAnimation = new TextureRegion[]{
              new TextureRegion(thunderstormTexture, 0, 0, 43, 213),
              new TextureRegion(thunderstormTexture, 43, 0, 43, 213),
		      new TextureRegion(thunderstormTexture, 86, 0, 43, 213),
              new TextureRegion(thunderstormTexture, 129, 0, 43, 213),
         };

        fireAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(fireTexture, 0, 0, 49, 94),
				new TextureRegion(fireTexture, 49, 0, 49, 94),
				new TextureRegion(fireTexture, 98, 0, 49, 94),
				new TextureRegion(fireTexture, 147, 0, 49, 94),
		});
        
        energyAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(energyTexture, 0, 33, 33, 33),
				new TextureRegion(energyTexture, 33, 33, 33, 33),
				new TextureRegion(energyTexture, 66, 33, 33, 33),
				new TextureRegion(energyTexture, 0, 66, 33, 33),
				new TextureRegion(energyTexture, 33, 66, 33, 33),
				new TextureRegion(energyTexture, 66, 66, 33, 33),
				new TextureRegion(energyTexture, 0, 99, 33, 33),
				new TextureRegion(energyTexture, 33, 99, 33, 33),
				new TextureRegion(energyTexture, 66, 99, 33, 33),
		});
        
        sunstrikeAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(sunstrikeTexture, 0, 192, 96, 96),
				new TextureRegion(sunstrikeTexture, 96, 192, 96, 96),
				new TextureRegion(sunstrikeTexture, 192, 192, 96, 96),
				new TextureRegion(sunstrikeTexture, 288, 192, 96, 96),
				new TextureRegion(sunstrikeTexture, 384, 192, 96, 96),
				new TextureRegion(sunstrikeTexture, 0, 96, 96, 96),
				new TextureRegion(sunstrikeTexture, 96, 96, 96, 96),
				new TextureRegion(sunstrikeTexture, 192, 96, 96, 96),
				new TextureRegion(sunstrikeTexture, 288, 96, 96, 96),
				new TextureRegion(sunstrikeTexture, 384, 96, 96, 96),
				new TextureRegion(sunstrikeTexture, 0, 0, 96, 96),
				new TextureRegion(sunstrikeTexture, 96, 0, 96, 96),
				new TextureRegion(sunstrikeTexture, 192, 0, 96, 96),
				new TextureRegion(sunstrikeTexture, 288, 0, 96, 96),
				new TextureRegion(sunstrikeTexture, 384, 0, 96, 96),
		});
        
		northBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		northEastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		eastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southEastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southWestBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		westBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		northWestBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		bobDeathAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		
		tornadoAnimation.setPlayMode(Animation.PlayMode.LOOP);
		shieldAnimation.setPlayMode(Animation.PlayMode.LOOP);
		swordAnimation.setPlayMode(Animation.PlayMode.LOOP);
		dustAnimation.setPlayMode(Animation.PlayMode.LOOP);
		reverseDustAnimation.setPlayMode(Animation.PlayMode.LOOP);
		laserAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
		energyAnimation.setPlayMode(Animation.PlayMode.LOOP);
		sunstrikeAnimation.setPlayMode(Animation.PlayMode.LOOP);
	}

	/**
	 * Load sprites.
	 */
	private static void loadSprites(){
		
		southBob = new TextureRegion(charTexture, 60, 35, 30, 30);
		southWestBob = new TextureRegion(charTexture, 60, 65, 30, 30);
		westBob = new TextureRegion(charTexture, 60, 95, 30, 30);
		northWestBob = new TextureRegion(charTexture, 60, 125, 30, 30);
		northBob = new TextureRegion(charTexture, 60, 155, 30, 30);
		northEastBob = new TextureRegion(charTexture, 210, 125, 30, 30);		
		eastBob = new TextureRegion(charTexture, 210, 95, 30, 30);
		southEastBob = new TextureRegion(charTexture, 210, 65, 30, 30);	
	}
	
	/**
	 * Load fonts.
	 */
	private static void loadFonts(){
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/header.TTF"));
		FreeTypeFontParameter fontPars = new FreeTypeFontParameter();
		fontPars.size = 60;
		fontPars.color = new Color(199/255f, 176/255f, 151/255f, 1);
		fontPars.borderColor = Color.BLACK;
		fontPars.borderWidth = 1;
		//fontPars.shadowColor = Color.BLACK;
		//fontPars.shadowOffsetX = fontPars.shadowOffsetY = 5;
		header = fontGen.generateFont(fontPars);
		fontGen.dispose();

		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/play.TTF"));
		fontPars = new FreeTypeFontParameter();
		fontPars.size = 28;
		fontPars.color = Color.WHITE;
		fontPars.borderColor = Color.BLACK;
		fontPars.borderWidth = 1;
		//fontPars.shadowColor = Color.BLACK;
		//fontPars.shadowOffsetX = fontPars.shadowOffsetY = 5;
		playText = fontGen.generateFont(fontPars);
		fontGen.dispose();
		
		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Fantasy.ttf"));
		fontPars = new FreeTypeFontParameter();
		fontPars.size = 28;
		fontPars.color = Color.LIGHT_GRAY;
		fontPars.borderColor = Color.BLACK;
		fontPars.borderWidth = 1;
		//fontPars.shadowColor = Color.BLACK;
		//fontPars.shadowOffsetX = fontPars.shadowOffsetY = 5;
		swordText = fontGen.generateFont(fontPars);
		fontGen.dispose();
	}
	
	/**
	 * Dispose of header.
	 */
	public static void dispose(){
		header.dispose();
	}

	
	/**
	 * Gets the audio settings.
	 *
	 * @return the audio settings
	 */
	public static boolean getAudioSettings(){
		return prefs.getBoolean("audio");
	}
	
	/**
	 * Sets the audio settings.
	 *
	 * @param newAudio sets whether muted or not
	 */
	public static void setAudioSettings(boolean newAudio){
		prefs.putBoolean("audio",newAudio);
		prefs.flush();
	}
	
	/**
	 * Toggle music.
	 */
	public static void toggleMusic() {
		prefs.putBoolean("audio", !prefs.getBoolean("audio"));
		prefs.flush();
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public static String getUsername(){
		if(prefs.contains("name")){
			Gdx.app.log(TAG, prefs.getString("name"));
			return prefs.getString("name");
		}else {
			Gdx.app.log(TAG, "null");
			return "";
		}
	}
	
	/**
	 * Sets the username.
	 *
	 * @param newName the new username
	 */
	public static void setUsername(String newName){
		Gdx.app.log(TAG,"Setting username: "+newName);
		prefs.putString("name",newName);
		prefs.flush();
	}
}


