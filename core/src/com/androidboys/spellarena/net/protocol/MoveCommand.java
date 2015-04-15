package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

//Movement of Bob
public class MoveCommand extends Command{

	private static final String TAG = "MoveCommand";
	private int movement;
	private float x;
	private float y;
	
	public static Command build(JSONObject json) {
		MoveCommand command = new MoveCommand();
		try{
			command.parseCommonFields(json);
			command.movement = json.getInt("movement");
			command.x = (float) json.getDouble("x");
			command.y = (float) json.getDouble("y");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
		}
		return command;
	}

	//Format: {"t":timestamp_of_command, "command":10, "fromUser":user_name, "movement":movement, "x":x, "y":y}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("movement", movement);
		json.put("x",x);
		json.put("y",y);
	}

	@Override
	public int getCommand() {
		return MOVE;
	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}
	
	public void setMovement(int movement) {
		this.movement = movement;
	}

	public int getMovement() {
		return movement;
	}

}
