package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

// TODO: Auto-generated Javadoc
//Response of clock synchronization command
/**
 * The Class ClockSyncResCommand.
 */
public class ClockSyncResCommand extends Command{

	/** The Constant TAG. */
	private static final String TAG = "ClockSyncResCommand";
	
	/** The initial time stamp. */
	private long initialTimeStamp;
	
	/**
	 * Builds the.
	 *
	 * @param json the json
	 * @return the command
	 */
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
	
	/**
	 * Gets the initial time stamp.
	 *
	 * @return the initial time stamp
	 */
	public long getInitialTimeStamp() {
		return initialTimeStamp;
	}

	/**
	 * Sets the initial time stamp.
	 *
	 * @param initialTimeStamp the new initial time stamp
	 */
	public void setInitialTimeStamp(long initialTimeStamp) {
		this.initialTimeStamp = initialTimeStamp;
	}
	
	//Format: {"t":timestamp_of_command, "command": 2, "fromUser":user_name}
	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.protocol.Command#serializeCustomFields(org.json.JSONObject)
	 */
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("initialTimeStamp", initialTimeStamp);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.protocol.Command#getCommand()
	 */
	@Override
	public int getCommand() {
		return CLOCK_SYNC_RES;
	}

	
	
}
