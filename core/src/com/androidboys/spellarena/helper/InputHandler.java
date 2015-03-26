package com.androidboys.spellarena.helper;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class InputHandler implements InputProcessor{

	private GameWorld world;
	private Bob bob;
	
	private GameScreen screen;
	private Stage stage;
	private Touchpad touchpad;
	
	private InputMultiplexer im;

	
	public InputHandler(GameWorld world, GameScreen screen) {
		this.world = world;
		this.bob = world.getBob();
		
		this.screen = screen;
		this.stage = screen.getStage();
		this.touchpad = screen.getTouchpad();
		
		im = new InputMultiplexer();
		im.addProcessor(stage);
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
