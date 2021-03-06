package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

//"Update" command
public class UpdateCommand extends Command{

	private static final String TAG = "UpdateCommand";
	private float x;
	private float y;
	private float vx;
	private float vy;
	private float health;
	
	//Format: {"t":timestamp_of_command, "command":12, "fromUser":user_name, "x":x, "y":y, "vx":x_velocity, "vy":y_velocity, "health":hp}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("x", x);
		json.put("y", y);
		json.put("vx", vx);
		json.put("vy", vy);
		json.put("health", getHealth());
	}

	@Override
	public int getCommand() {
		return Command.UPDATE;
	}

	/**
	 * Sets the update.
	 *
	 * @param position the position
	 * @param velocity the velocity
	 */
	public void setUpdate(Vector2 position, Vector2 velocity) {
		this.x = position.x;
		this.y = position.y;
		this.vx = velocity.x;
		this.vy = velocity.y;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}

	/**
	 * Gets the velocity.
	 *
	 * @return the velocity
	 */
	public Vector2 getVelocity(){
		return new Vector2(vx,vy);
	}
	
	/**
	 * Builds the json file for updating players detail.
	 *
	 * @param json the json
	 * @return the command
	 */
	public static Command build(JSONObject json) {
		UpdateCommand command = new UpdateCommand();
		try{
			command.parseCommonFields(json);
			command.x = (float) json.getDouble("x");
			command.y = (float) json.getDouble("y");
			command.vx = (float) json.getDouble("vx");
			command.vy = (float) json.getDouble("vy");
			command.health = (float) json.getDouble("health");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
		}
		return command;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

}
