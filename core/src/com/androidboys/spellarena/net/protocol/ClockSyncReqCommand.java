package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

// TODO: Auto-generated Javadoc
//Request for synchronizing clock
/**
 * The Class ClockSyncReqCommand.
 */
public class ClockSyncReqCommand extends Command{

	/** The Constant TAG. */
	private static final String TAG = "ClockSyncReqCommand";
	
	/**
	 * Builds the.
	 *
	 * @param json the json
	 * @return the command
	 */
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
	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.protocol.Command#serializeCustomFields(org.json.JSONObject)
	 */
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.protocol.Command#getCommand()
	 */
	@Override
	public int getCommand() {
		return CLOCK_SYNC_REQ;
	}

}
