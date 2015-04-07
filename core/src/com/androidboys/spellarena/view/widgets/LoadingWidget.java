package com.androidboys.spellarena.view.widgets;

import javax.swing.GroupLayout.Alignment;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.helper.StyleLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

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
				new String[]{"l","lo","loa","load","loadi","loadin","loading"}
		, StyleLoader.parchmentLabel);
		text.setAlignment(1);
		this.addActor(background);
		this.addActor(text);
	}

}
