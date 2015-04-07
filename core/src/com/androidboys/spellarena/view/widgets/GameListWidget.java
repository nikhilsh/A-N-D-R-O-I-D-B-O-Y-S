package com.androidboys.spellarena.view.widgets;

import javax.swing.GroupLayout.Alignment;

import com.androidboys.spellarena.helper.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class GameListWidget extends WidgetGroup {

	private ScrollPane scrollPane;
	private Table table;

	public GameListWidget(){
		super();
		scrollPane = new ScrollPane(generateTable());
		scrollPane.setPosition(200, 200);
		table.add(scrollPane).height(215).padBottom(30);
	}

	private Table generateTable() {
		Table itemTable = new Table();
		itemTable.setSize(100, 215);
		
		for(int i = 0;i < 5; i++){
			itemTable.row();
			
			GameWidget widget;
			if(i%2 == 0){
				widget = new GameWidget();
			} else {
				widget = new GameWidget();
			}
			itemTable.add(widget).width(widget.getWidth()).height(widget.getHeight());
		}
		return itemTable;
	}
}
