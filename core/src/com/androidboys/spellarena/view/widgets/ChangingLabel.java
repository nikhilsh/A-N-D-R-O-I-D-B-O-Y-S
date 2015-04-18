package com.androidboys.spellarena.view.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ChangingLabel extends Label{
		
		private String[] text;
		private int nFrame;
		private float time;
		
		public ChangingLabel(String[] text, LabelStyle style) {
			super(text[0], style);
			this.text = text;
			nFrame = text.length;
		}
	
		@Override
		public void act(float delta){
			time+=delta;
			setText(text[(int)time%nFrame]);
			super.act(delta);
		}	
}

