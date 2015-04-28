package com.androidboys.spellarena.view.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * The Class ChangingLabel.
 */
public class ChangingLabel extends Label{
		
		/** The text. */
		private String[] text;
		
		/** The n frame. */
		private int nFrame;
		
		/** The time. */
		private float time;
		
		/**
		 * Instantiates a new changing label.
		 *
		 * @param text the text
		 * @param style the style
		 */
		public ChangingLabel(String[] text, LabelStyle style) {
			super(text[0], style);
			this.text = text;
			nFrame = text.length;
		}
	
		/* (non-Javadoc)
		 * @see com.badlogic.gdx.scenes.scene2d.Actor#act(float)
		 */
		@Override
		public void act(float delta){
			time+=delta;
			setText(text[(int)time%nFrame]);
			super.act(delta);
		}	
}

