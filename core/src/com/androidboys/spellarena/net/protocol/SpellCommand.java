package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidboys.spellarena.model.Bob.Direction;
import com.androidboys.spellarena.model.Spell.Spells;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

//Casting a spell
public class SpellCommand extends Command {
	private static final String TAG = "SpellCommand";
	private int spell;
	private float x;
	private float y;
	private int direction;
	
	/**
	 * Builds the JSON file to be sent for spells.
	 *
	 * @param json the json
	 * @return the command
	 */
	public static Command build(JSONObject json) {
		SpellCommand command = new SpellCommand();
		try{
			command.parseCommonFields(json);
			command.x = (float) json.getDouble("x");
			command.y = (float) json.getDouble("y");
			command.spell = (int) json.getInt("spell");
			command.direction = (int) json.getInt("direction");
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
		}
		return command;
	}

	//Format: {"t":timestamp_of_command, "command":11, "fromUser":user_name, "spell":spell, "x":x, "y":y, "direction":direction}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("spell", spell);
		json.put("x", x);
		json.put("y", y);
		json.put("direction", direction);
	}

	@Override
	public int getCommand() {
		return CAST;
	}
	
	/**
	 * Sets the position of the spell
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the position of the spell.
	 *
	 * @return the position
	 */
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}
	
	/**
	 * Sets the spell.
	 *
	 * @param spell the new spell
	 */
	public void setSpell(int spell){
		this.spell = spell;
	}

	/**
	 * Gets the spell.
	 *
	 * @return the spell
	 */
	public int getSpell(){
		return spell;
	}
	
	/**
	 * Gets the direction of the spell.
	 *
	 * @return the direction
	 */
	public int getDirection(){
		return direction;
	}
	
	/**
	 * Sets the direction of the spell.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(int direction){
		this.direction = direction;
	}
	
}
