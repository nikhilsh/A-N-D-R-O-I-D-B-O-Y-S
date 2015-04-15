package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

//Response of clock synchronization command
public class ClockSyncResCommand extends Command{

	private static final String TAG = "ClockSyncResCommand";
	
	private long initialTimeStamp;
	
	public static Command build(JSONObject json){
		ClockSyncResCommand command = new ClockSyncResCommand();
		try{
			command.parseCommonFields(json);
			command.initialTimeStamp = json.getLong("initialTimeStamp");
		} catch (JSONException e){
			Gdx.app.error(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}
	
	public long getInitialTimeStamp() {
		return initialTimeStamp;
	}

	public void setInitialTimeStamp(long initialTimeStamp) {
		this.initialTimeStamp = initialTimeStamp;
	}
	
	//Format: {"t":timestamp_of_command, "command": 2, "fromUser":user_name}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("initialTimeStamp", initialTimeStamp);
	}

	@Override
	public int getCommand() {
		return CLOCK_SYNC_RES;
	}

	
	
}
