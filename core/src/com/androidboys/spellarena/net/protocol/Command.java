package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

//Base class of JSON commands
public abstract class Command {
	
	public static final int CLOCK_SYNC_REQ = 1; //ClockSyncReqCommand
    public static final int CLOCK_SYNC_RES = 2; //ClockSyncResCommand
    
    public static final int MOVE = 10; //MoveCommand
    public static final int CAST = 11; //SpellCommand
    public static final int UPDATE = 12; //UpdateCommand
    
    public static final int CREATE_GAME = 100; //CreateGameCommand
    public static final int READY = 101; //ReadyCommand
    public static final int START_GAME = 102; //StartGameCommand
    
    public static final int GAME_END = 0; //GameEndCommand
   	
    protected long timestamp; //Timestamp of the command created
    protected String fromUser; //Creator of the command
    protected int command; //Command type
    
    public static final int MAX_MESSAGE_LENGTH = 200;
    
    protected void parseCommonFields(JSONObject json) throws JSONException {
        this.timestamp = json.getLong("t");
        this.command = json.getInt("command");
        this.fromUser = json.getString("fromUser");
    }

    //Format: {"t":timestamp_of_command, "command": command_type, "fromUser":user_name}
    public String serialize() {
        JSONObject json = new JSONObject();
        try {
            this.timestamp = System.currentTimeMillis(); //Get the time of now
            json.put("t", this.timestamp);
            json.put("command", this.getCommand());
            json.put("fromUser", this.fromUser);
            //More fields will be added by implementing this method
            serializeCustomFields(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
    
    public String[] splitMessage(String message) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            int id = (int)(Math.random()*1000);
            String[] messages = new String[(int)Math.ceil((double)message.length()/MAX_MESSAGE_LENGTH)];
            for (int i=0; i<messages.length; i++) {
                messages[i] = "split#" + id + "#" + messages.length + "#" + i + "#" + message.substring(i*MAX_MESSAGE_LENGTH, Math.min((i+1)*MAX_MESSAGE_LENGTH, message.length()));
            }

            return messages;
        }

        return new String[] {message};
    }
    
	//More fields will be added by implementing this method
    protected abstract void serializeCustomFields(JSONObject json) throws JSONException;

    public abstract int getCommand();
    
    public long getTimeStamp(){
    	return timestamp;
    }
    
    public String getFromUser(){
    	return fromUser;
    }
    
    public void setFromUser(String fromUser){
    	this.fromUser = fromUser;
    }
    
    @Override
    public String toString(){
    	return "Command{" +
                "timestamp=" + timestamp +
                ", fromUser='" + fromUser + '\'' +
                ", command=" + command +
                '}';

    }
}
