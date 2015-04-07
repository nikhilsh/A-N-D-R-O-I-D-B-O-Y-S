package com.androidboys.spellarena.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetLoader {

	public static TextureRegion backgroundRegion;

	public static Texture buttonTexture;
	
	public static TextureRegion playRegion, helpRegion;
	
	public static Texture charTexture, dustTexture;
	public static TextureRegion northBob, eastBob, southBob, westBob, 
		northEastBob, southEastBob, southWestBob, northWestBob, dustSpell;
	public static Animation northBobAnimation, eastBobAnimation, southBobAnimation, westBobAnimation, 
		northEastBobAnimation, southEastBobAnimation, southWestBobAnimation, northWestBobAnimation, dustSpellAnimation;
	
	
	public static BitmapFont header, playText, swordText;
	
	public static TiledMap map;
	
	public static Preferences prefs;
	
	public static void load(){
		
		backgroundRegion = new TextureRegion(new Texture(Gdx.files.internal("images/bg.jpg")));
		
		//buttonTexture = new Texture(Gdx.files.internal("items/textures.pack.png"));
		
		//playRegion = new TextureRegion(buttonTexture, 2, 2, 200, 50);
		//helpRegion = new TextureRegion(buttonTexture, 2, 2, 204, 50);
		
		loadFonts();
		loadMaps();
		loadSprites();
		loadAnimations();
	}
	
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
		
		dustSpellAnimation = new Animation(0.06f, new TextureRegion[]{
				new TextureRegion(dustTexture, 0, 0, 128, 128),
				new TextureRegion(dustTexture, 128, 128, 128, 128)
		});
		
		northBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		northEastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		eastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southEastBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		southWestBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		westBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		northWestBobAnimation.setPlayMode(Animation.PlayMode.LOOP);
		dustSpellAnimation.setPlayMode(Animation.PlayMode.NORMAL);
	}

	private static void loadSprites(){
		charTexture = new Texture(Gdx.files.internal("sprites/wizard.png"));
		charTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		dustTexture = new Texture(Gdx.files.internal("sprites/dust.png"));
		dustTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		southBob = new TextureRegion(charTexture, 60, 35, 30, 30);
		southWestBob = new TextureRegion(charTexture, 60, 65, 30, 30);
		westBob = new TextureRegion(charTexture, 60, 95, 30, 30);
		northWestBob = new TextureRegion(charTexture, 60, 125, 30, 30);
		northBob = new TextureRegion(charTexture, 60, 155, 30, 30);
		northEastBob = new TextureRegion(charTexture, 210, 125, 30, 30);		
		eastBob = new TextureRegion(charTexture, 210, 95, 30, 30);
		southEastBob = new TextureRegion(charTexture, 210, 65, 30, 30);	
		dustSpell = new TextureRegion(dustTexture, 0, 0, 128, 128);
	}
	
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
	
	private static void loadMaps(){
		map = new TmxMapLoader().load("maps/Dungeon.tmx");
	}
	
	public static int getHighScore(){
		return prefs.getInteger("highScore");
	}
	
	public static void setHighScore(int newScore){
		prefs.putInteger("highScore",newScore);
		prefs.flush();
	}
	
	public static void dispose(){
		buttonTexture.dispose();
		header.dispose();
	}
}


