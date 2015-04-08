package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class MoveCommand extends Command{

	private static final String TAG = "MoveCommand";
	private int movement;
	
	public static Command build(JSONObject json) {
		MoveCommand command = new MoveCommand();
		try{
			command.parseCommonFields(json);
			command.movement = json.getInt("movement");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
		}
		return command;
	}

	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("movement", movement);
	}

	@Override
	public int getCommand() {
		return MOVE;
	}

	public void setMovement(int movement) {
		this.movement = movement;
	}

	public int getMovement() {
		return movement;
	}

}
