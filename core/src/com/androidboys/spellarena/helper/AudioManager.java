package com.androidboys.spellarena.helper;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioManager {

	private static final String SOUND_PATH ="gamesounds/";
	
	private static final String MAIN_THEME = SOUND_PATH + "bgmusic.mp3";
	private static final String START_GAME = SOUND_PATH + "startgame.mp3";
	private static final String END_GAME= SOUND_PATH + "endgame.mp3";

	private static final String SHIELD = SOUND_PATH + "shield.mp3";
	private static final String SUNSTRIKE = SOUND_PATH + "sunstrike.mp3";
	private static final String HASTE= SOUND_PATH + "haste.mp3";
	private static final String LASER = SOUND_PATH + "laser.mp3";
	private static final String BLINK = SOUND_PATH + "blink.mp3";
	private static final String HURRICANE =SOUND_PATH + "hurricane.mp3";
	private static final String FIREWALL =SOUND_PATH + "firewall.mp3";
	private static final String SPARK = SOUND_PATH + "spark.mp3";
	private static final String THUNDERSTORM = SOUND_PATH + "thunderstorm.mp3";
	private static final String BOOMERANG = SOUND_PATH + "boomerang.mp3";


	private AssetManager assetManager;

	private Music activeMusic;

	public void initialize(){
		this.assetManager = new AssetManager();
		activeMusic = null;

		if ( Gdx.app.getType() == Application.ApplicationType.Android){
			FileHandle directoryHandle = Gdx.files.internal(SOUND_PATH);

			for ( FileHandle file: directoryHandle.list()){
				assetManager.load( file.path(), Sound.class);
			}
		}
		else{
			assetManager.load(MAIN_THEME, Sound.class);
			assetManager.load(START_GAME, Sound.class);
			assetManager.load(END_GAME, Sound.class);

			assetManager.load(SHIELD, Sound.class);
			assetManager.load(SUNSTRIKE, Sound.class);
			assetManager.load(HASTE, Sound.class);         
			assetManager.load(LASER, Sound.class);
			assetManager.load(BLINK, Sound.class);
			assetManager.load(HURRICANE, Sound.class);
			assetManager.load(FIREWALL, Sound.class);
			assetManager.load(SPARK, Sound.class);
			assetManager.load(THUNDERSTORM, Sound.class);
			assetManager.load(BOOMERANG, Sound.class);
		}
		assetManager.finishLoading();
	}

	public void unload(){
		assetManager.clear();
	}

	private boolean playSound( String name){
		return playSound( name, false);
	}

	private boolean playSound(final String name, final boolean isLoop){
		try {
			Gdx.app.postRunnable(new Runnable() {

				@Override
				public void run() {
					Sound sound = assetManager.get(name, Sound.class);
					if (isLoop){
						sound.loop();
					}
					else{
						sound.play();
					}
				}
			});

		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private boolean playMusic(String name, boolean isLoop){
		try {
			Music music = Gdx.app.getAudio().newMusic(Gdx.files.internal(name));
			music.setLooping(isLoop);
			music.play();
			stopMusic();
			activeMusic = music;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private void stopMusic(){
		if (activeMusic != null){
			activeMusic.stop();
			activeMusic.dispose();
		}
		activeMusic = null;
	}

	public void playMainTheme() {
		playMusic(MAIN_THEME, true);
	}
	
	public void stopMainTheme() {
		stopMusic();
	}
	
	public void playStartGame() {
		playMusic(START_GAME, false);
	}
	
	public void playEndGame() {
		playMusic(END_GAME, false);
	}	
		
	public void playShieldSound() {
		playSound(SHIELD, false);
	}

	public void playBoomerang() {
		playSound(BOOMERANG,false);
	}

	public void playFirewall() {
		playSound(FIREWALL,false);
	}

	public void playBlink() {
		playMusic(BLINK, false);
	}

	public void playHaste() {
		playMusic(HASTE, false);
	}
	
	public void playHurricane() {
		playMusic(HURRICANE, false);
	}
	
	public void playSpark() {
		playMusic(SPARK, false);
	}
	
	public void playThunderstorm() {
		playMusic(THUNDERSTORM, false);
	}
	
	public void playLaser() {
		playMusic(LASER, false);
	}
	
	public void playSunstrike(){
		playMusic(SUNSTRIKE, false);
	}
	

}
