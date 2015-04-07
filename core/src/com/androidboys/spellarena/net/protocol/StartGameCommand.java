package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class StartGameCommand extends Command{

	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCommand() {
		// TODO Auto-generated method stub
		return 0;
	}

}
