package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class CreateGameCommand extends Command{

	
	
	private static final String TAG = "CreateGameCommand";

	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {		
	}

	@Override
	public int getCommand() {
		return CREATE_GAME;

	}

	public static Command build(JSONObject json) {
		CreateGameCommand command = new CreateGameCommand();
		try{
			command.parseCommonFields(json);
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}

}
