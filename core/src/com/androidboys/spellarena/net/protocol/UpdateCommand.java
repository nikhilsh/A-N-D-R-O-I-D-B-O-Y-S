package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class UpdateCommand extends Command{

	private static final String TAG = "UpdateCommand";
	private float x;
	private float y;
	private float vx;
	private float vy;
	
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("x", x);
		json.put("y", y);
		json.put("vx", vx);
		json.put("vy", vy);
	}

	@Override
	public int getCommand() {
		return Command.UPDATE;
	}

	public void setUpdate(Vector2 position, Vector2 velocity) {
		this.x = position.x;
		this.y = position.y;
		this.vx = velocity.x;
		this.vy = velocity.y;
	}
	
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}

	public Vector2 getVelocity(){
		return new Vector2(vx,vy);
	}
	
	public static Command build(JSONObject json) {
		UpdateCommand command = new UpdateCommand();
		try{
			command.parseCommonFields(json);
			command.x = (float) json.getDouble("x");
			command.y = (float) json.getDouble("y");
			command.vx = (float) json.getDouble("vx");
			command.vy = (float) json.getDouble("vy");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
		}
		return command;
	}

}
