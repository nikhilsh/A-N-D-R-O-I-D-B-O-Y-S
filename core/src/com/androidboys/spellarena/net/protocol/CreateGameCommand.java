package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class CreateGameCommand extends Command{

	private static final String TAG = "CreateGameCommand";
	private String ip;
	private long random;
	
	//Format: {"t":timestamp_of_command, "command": 100, "fromUser":user_name, "ip": ip}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {	
		json.put("ip", ip);
		json.put("random", getRandom());
	}

	@Override
	public int getCommand() {
		return CREATE_GAME;
	}

	/**
	 * Builds the JSON message to send over.
	 *
	 * @param json the json
	 * @return the command
	 */
	public static Command build(JSONObject json) {
		CreateGameCommand command = new CreateGameCommand();
		try{
			command.parseCommonFields(json);
			command.ip = json.getString("ip");
			command.setRandom(json.getLong("random"));
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Gets the random.
	 *
	 * @return the random
	 */
	public long getRandom() {
		return random;
	}

	/**
	 * Sets the random.
	 *
	 * @param random the new random
	 */
	public void setRandom(long random) {
		this.random = random;
	}
}
