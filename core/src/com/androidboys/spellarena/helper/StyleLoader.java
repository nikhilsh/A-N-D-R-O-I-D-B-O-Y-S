package com.androidboys.spellarena.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class StyleLoader {

	public static LabelStyle tableLabelStyle; 
	
	public static LabelStyle parchmentLabel, smallParchmentLabel; 
	
	public static TextButtonStyle buttonStyle;
	
	public static TextButtonStyle parchmentButtonStyle, smallParchmentButtonStyle;
	
	public static TextFieldStyle textFieldStyle;
	
	public static void prepareStyles(){
		
		parchmentLabel = new LabelStyle();
		parchmentLabel.font = AssetLoader.parchmentText;
		smallParchmentLabel = new LabelStyle();
		smallParchmentLabel.font = AssetLoader.smallParchmentText;
		tableLabelStyle = new LabelStyle();
		tableLabelStyle.font = AssetLoader.playTextSmall;
		
		buttonStyle = new TextButtonStyle();
		buttonStyle.font = AssetLoader.playText;
		buttonStyle.pressedOffsetX = 5;
		buttonStyle.pressedOffsetY = -5;
		parchmentButtonStyle = new TextButtonStyle(buttonStyle);
		parchmentButtonStyle.font = AssetLoader.parchmentText;
		smallParchmentButtonStyle = new TextButtonStyle(buttonStyle);
		smallParchmentButtonStyle.font = AssetLoader.smallParchmentText;
		
		textFieldStyle = new TextFieldStyle();
		textFieldStyle.fontColor = Color.WHITE;
		textFieldStyle.font = AssetLoader.playTextSmall;
		textFieldStyle.messageFont = AssetLoader.playTextSmall;
		
	}
	
}
