package com.androidboys.spellarena.gameworld;

import java.util.Random;

public class GameFactory {

	public static class GameModel{
		public int map;
		
		private GameModel(int map){
			this.map = map;
		}
	}
	
	public static GameModel getGameModel(int map){
		return new GameModel(map);
	}
	
	
	//Random map picker
	public static GameModel getGameModel(){
		return new GameModel(new Random().nextInt(3));
	}
}
