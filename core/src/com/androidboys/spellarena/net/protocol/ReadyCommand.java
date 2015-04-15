package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

//"Server is ready" command
public class ReadyCommand extends Command{

	private static final String TAG = "ServerReadyCommand";
	
	//Format: {"t":timestamp_of_command, "command":101, "fromUser":user_name}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {	

	}

	@Override
	public int getCommand() {
		return READY;
	}

	public static Command build(JSONObject json) {
		ReadyCommand command = new ReadyCommand();
		try{
			command.parseCommonFields(json);
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}
	
}
