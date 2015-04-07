package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class StartGameCommand extends Command{

	private static final String TAG = null;

	public static Command build(JSONObject json) {
		StartGameCommand command = new StartGameCommand();
		try{
			command.parseCommonFields(json);
		} catch (JSONException e){
			Gdx.app.log(TAG,e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}
	
	
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		
	}

	@Override
	public int getCommand() {
		return START_GAME;
	}

}
