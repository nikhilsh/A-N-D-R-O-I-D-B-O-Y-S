package com.androidboys.spellarena.view.widgets;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.StyleLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/**
 * The Class LoadingWidget.
 */
public class LoadingWidget extends Stack {

	/** The background. */
	private Image background;
	
	/** The text. */
	private ChangingLabel text;
	
	/** The instance. */
	private static LoadingWidget INSTANCE;

	/**
	 * Gets the single instance of LoadingWidget.
	 *
	 * @return single instance of LoadingWidget
	 */
	public static LoadingWidget getInstance(){
		if(INSTANCE == null){
			INSTANCE = new LoadingWidget();
		}
		return INSTANCE;
	}
	
	/**
	 * Instantiates a new loading widget.
	 */
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
