package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class CreateGameCommand extends Command{

	private static final String TAG = "CreateGameCommand";
	private static String ip;
	private boolean serverReady;
	
	//Format: {"t":timestamp_of_command, "command": 100, "fromUser":user_name, "ip": ip}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {	
		json.put("ip", ip);
	}

	@Override
	public int getCommand() {
		return CREATE_GAME;
	}

	public static Command build(JSONObject json) {
		CreateGameCommand command = new CreateGameCommand();
		try{
			command.parseCommonFields(json);
			command.ip = json.getString("ip");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getIP() {
		return ip;
	}
}
