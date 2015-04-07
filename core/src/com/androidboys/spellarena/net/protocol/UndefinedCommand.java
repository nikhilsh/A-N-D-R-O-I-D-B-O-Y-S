package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class UndefinedCommand extends Command {

	private String message;
	
	public UndefinedCommand(String string) {
		this.message = string;
	}

	public String getMessage(){
		return this.message;
	}
	
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
	}

	@Override
	public int getCommand() {
		return 0;
	}

}
