package com.androidboys.spellarena.net.protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class CommandFactory {
	
	private static final String TAG = "CommandFactory";

	private Set<String> receivedCommands = new HashSet<String>();
	
	private Map<Integer, Map<Integer,String>> messages = new HashMap<Integer, Map<Integer,String>>();
	
	public Command createCommand(String jsonString){
		if(receivedCommands.contains(jsonString)){
//			Gdx.app.log(TAG, "Duplicate message received: "+jsonString);
			return null;
		}
//		Gdx.app.log(TAG, "Received: "+jsonString);
		receivedCommands.add(jsonString);
		
		if(jsonString.startsWith("split")){
			String arr[] = jsonString.split("#");
			int messageId = Integer.parseInt(arr[1]);
			int messageLength = Integer.parseInt(arr[2]);
			int messageIndex = Integer.parseInt(arr[3]);
			String message = arr[4];
			
			Map<Integer, String> map = messages.get(messageId);
			if(map == null){
				map = new HashMap<Integer, String>();
				messages.put(messageId, map);
			}
			
			map.put(messageIndex, message);
			
			if(map.size() != messageLength) {
				return null;
			}
			
			jsonString = "";
			
			for(int i = 0; i <= messageLength; i++){
				jsonString += map.get(i);
			}
			
			messages.remove(messageId);
		}
		
		try{
			JSONObject json = new JSONObject(jsonString);
			int command = json.getInt("command");
			switch(command) {
			case Command.CLOCK_SYNC_REQ:
                return ClockSyncReqCommand.build(json);
            case Command.CLOCK_SYNC_RES:
                return ClockSyncResCommand.build(json);
            case Command.START_GAME:
            	return StartGameCommand.build(json);
            case Command.CREATE_GAME:
            	return CreateGameCommand.build(json);
            case Command.READY:
            	return ReadyCommand.build(json);
            case Command.MOVE:
                return MoveCommand.build(json);
            case Command.UPDATE:
            	return UpdateCommand.build(json);
            case Command.CAST:
            	return SpellCommand.build(json);
            case Command.GAME_END:
            	return GameEndCommand.build(json);
			}
		} catch (JSONException e){
			Gdx.app.error(TAG,e.toString());
		}
		
		return new UndefinedCommand(jsonString);
	}
}
