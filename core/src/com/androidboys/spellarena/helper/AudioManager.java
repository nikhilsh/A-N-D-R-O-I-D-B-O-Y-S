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

	private static final String TAG = "AudioManager";

	private static AssetManager assetManager;

	private static Music activeMusic;
	
	/**
	 * Initialize audio assets.
	 */
	public static void initialize(){
		assetManager = new AssetManager();
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
	}

	/**
	 * Unload audio files.
	 */
	public static void unload(){
		assetManager.clear();
	}



	/**
	 * Play music.
	 *
	 * @param name the name of the file to play
	 * @param isLoop set if audio is to be looped
	 * @return return if played
	 */
	private static boolean playSound(final String name, final boolean isLoop){
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

	/**
	 * Play music.
	 *
	 * @param name the name of the file to play
	 * @param isLoop set if audio is to be looped
	 * @return return if played
	 */
	private static boolean playMusic(String name, boolean isLoop){
		Gdx.app.log(TAG,"Playing music "+name);
		try {
			Music music = Gdx.app.getAudio().newMusic(Gdx.files.internal(name));
			music.setLooping(isLoop);
			music.play();
			stopMusic();
			activeMusic = music;
			toggleMusic();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * Stop music.
	 */
	private static void stopMusic(){
		if (activeMusic != null){
			activeMusic.stop();
			activeMusic.dispose();
		}
		activeMusic = null;
	}

	/**
	 * Play main theme.
	 */
	public static void playMainTheme() {
		playMusic(MAIN_THEME, true);
	}
	
	/**
	 * Stop main theme.
	 */
	public static void stopMainTheme() {
		stopMusic();
	}
	
	/**
	 * Play start game.
	 */
	public static void playStartGame() {
		playMusic(START_GAME, false);
	}
	
	/**
	 * Play end game sound.
	 */
	public static void playEndGame() {
		playMusic(END_GAME, false);
	}	
		
	/**
	 * Play shield sound.
	 */
	public static void playShieldSound() {
		playSound(SHIELD, false);
	}

	/**
	 * Play boomerang sound.
	 */
	public static void playBoomerang() {
		playSound(BOOMERANG,false);
	}

	/**
	 * Play firewall sound.
	 */
	public static void playFirewall() {
		playSound(FIREWALL,false);
	}

	/**
	 * Play blink sound.
	 */
	public static void playBlink() {
		playMusic(BLINK, false);
	}

	/**
	 * Play haste sound.
	 */
	public static void playHaste() {
		playMusic(HASTE, false);
	}
	
	/**
	 * Play hurricane sound.
	 */
	public static void playHurricane() {
		playMusic(HURRICANE, false);
	}
	
	/**
	 * Play spark sound.
	 */
	public static void playSpark() {
		playMusic(SPARK, false);
	}
	
	/**
	 * Play thunderstorm sound.
	 */
	public static void playThunderstorm() {
		playMusic(THUNDERSTORM, false);
	}
	
	/**
	 * Play laser sound.
	 */
	public static void playLaser() {
		playMusic(LASER, false);
	}
	
	/**
	 * Play sunstrike sound.
	 */
	public static void playSunstrike(){
		playMusic(SUNSTRIKE, false);
	}

	/**
	 * Update sound.
	 *
	 * @return true, if successful
	 */
	public static boolean update() {
		return assetManager.update();
	}
	
	/**
	 * Toggle music on or off.
	 */
	public static void toggleMusic(){
		boolean musicOn = AssetLoader.getAudioSettings();
		if(musicOn){
			activeMusic.setVolume(1);
		} else {
			activeMusic.setVolume(0);
		}
	}
}
