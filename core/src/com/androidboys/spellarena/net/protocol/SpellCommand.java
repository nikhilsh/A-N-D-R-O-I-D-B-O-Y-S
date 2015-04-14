package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidboys.spellarena.model.Bob.Direction;
import com.androidboys.spellarena.model.Spell.Spells;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class SpellCommand extends Command {
	private static final String TAG = "SpellCommand";
	private int spell;
	private float x;
	private float y;
	private int direction;
	
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
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}
	
	public void setSpell(int spell){
		this.spell = spell;
	}

	public int getSpell(){
		return spell;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}
	
}
