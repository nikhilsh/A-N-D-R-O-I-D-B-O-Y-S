package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

//"Game start" command
public class StartGameCommand extends Command{

	private static final String TAG = null;

	/**
	 * Builds the json file to start game.
	 *
	 * @param json the json
	 * @return the command
	 */
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
	
	//Format: {"t":timestamp_of_command, "command":102, "fromUser":user_name}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		
	}

	@Override
	public int getCommand() {
		return START_GAME;
	}

}
