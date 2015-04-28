package com.androidboys.spellarena.gameworld;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Game objects.
 */
public class GameFactory {

	/**
	 * The Class GameModel.
	 */
	public static class GameModel{
		
		/** The map. */
		public int map;
		
		/**
		 * Instantiates a new game model.
		 *
		 * @param map the map
		 */
		private GameModel(int map){
			this.map = map;
		}
	}
	
	/**
	 * Gets the game model.
	 *
	 * @param map the map
	 * @return the game model
	 */
	public static GameModel getGameModel(int map){
		return new GameModel(map);
	}
	
	
	//Random map picker
	/**
	 * Gets the game model.
	 *
	 * @return the game model
	 */
	public static GameModel getGameModel(){
		return new GameModel(new Random().nextInt(3));
	}
}
