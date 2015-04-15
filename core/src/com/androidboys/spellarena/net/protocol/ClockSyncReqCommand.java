package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

//Request for synchronizing clock
public class ClockSyncReqCommand extends Command{

	private static final String TAG = "ClockSyncReqCommand";
	
	public static Command build(JSONObject json){
		ClockSyncReqCommand command = new ClockSyncReqCommand();
		try{
			command.parseCommonFields(json);
		} catch (JSONException e) {
			Gdx.app.error(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}
	
	//Format: {"t":timestamp_of_command, "command": 1, "fromUser":user_name}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
	}

	@Override
	public int getCommand() {
		return CLOCK_SYNC_REQ;
	}

}
