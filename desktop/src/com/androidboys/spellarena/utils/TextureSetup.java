package com.androidboys.spellarena.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TextureSetup {
	
	public static void main(String[] args){
		TexturePacker.process("/Users/haoqintan/Desktop/Development/Spell Arena/android/assets/items/", 
				"/Users/haoqintan/Desktop/Development/Spell Arena/android/assets/items/", 
				"textures.pack");
	}
	
}
