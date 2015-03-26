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


