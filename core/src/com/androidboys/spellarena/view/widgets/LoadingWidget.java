package com.androidboys.spellarena.view.widgets;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.StyleLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class LoadingWidget extends Stack {

	private Image background;
	private ChangingLabel text;
	
	private static LoadingWidget INSTANCE;

	public static LoadingWidget getInstance(){
		if(INSTANCE == null){
			INSTANCE = new LoadingWidget();
		}
		return INSTANCE;
	}
	
	private LoadingWidget(){
		background = new Image(AssetLoader.loadingTexture);
		text = new ChangingLabel(
				new String[]{"loading","l","lo","loa","load","loadi","loadin"}
		, StyleLoader.parchmentLabel);
		text.setAlignment(1);
		this.addActor(background);
		this.addActor(text);
	}

}
