package com.androidboys.spellarena.helper;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Spell;
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
	
	/**
	 * Called when a key was pressed.
	 * @param keycode - one of the constants in Input.Keys
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when a key was released.
	 * @param keycode - one of the constants in Input.Keys
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when a key was typed.
	 * @param keycode - one of the constants in Input.Keys
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when the screen was touched or a mouse button was pressed.
	 * @param screenX - The x coordinate, origin is in the upper left corner
	 * @param screenY - The y coordinate, origin is in the upper left corner
	 * @param pointer - the pointer for the event.
	 * @param button - the button
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Called when a finger was lifted or a mouse button was released.
	 * @param screenX - The x coordinate, origin is in the upper left corner
	 * @param screenY - The y coordinate, origin is in the upper left corner
	 * @param pointer - the pointer for the event.
	 * @param button - the button
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Spell spell = new Spell(world);
		spell.spellSettler(1, 1, 2);
		// on tapping, call the spell generator, generate spell
		// then send on server and synchronously cast on own screen
		return true;
	}

	/**
	 * Called when a finger or the mouse was dragged.
	 * @param screenX - The x coordinate, origin is in the upper left corner
	 * @param screenY - The y coordinate, origin is in the upper left corner
	 * @param pointer - the pointer for the event.
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when the mouse was moved without any buttons being pressed.
	 * @param screenX - The x coordinate, origin is in the upper left corner
	 * @param screenY - The y coordinate, origin is in the upper left corner
	 * @return whether the input was processed
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when the mouse wheel was scrolled.
	 * @param amount - the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
	 * @return whether the input was processed
	 */
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
