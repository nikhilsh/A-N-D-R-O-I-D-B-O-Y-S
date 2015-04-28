package com.androidboys.spellarena.net.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

//"Game is end" command
public class GameEndCommand extends Command{

	public static enum GameEndReason{
		GAME_END, OWNER_LEFT;
	}

	private static final String TAG = "GameEndCommand";
	
	private GameEndReason reason;
	private String winner;
	
	/**
	 * Builds the json file to end game.
	 *
	 * @param json the json
	 * @return the command
	 */
	public static Command build(JSONObject json){
		GameEndCommand command = new GameEndCommand();
		try{
			command.parseCommonFields(json);
			command.reason = GameEndReason.valueOf(json.getString("reason"));
			if(json.has("winner")) {
				command.winner = json.getString("winner");
			}
		} catch (JSONException e){
			Gdx.app.log(TAG, e.toString());
			return new UndefinedCommand(json.toString());
		}
		return command;
	}
	
	//Format: {"t":timestamp_of_command, "command": 0, "fromUser":user_name, "reason": reason, "winner": winner}
	@Override
	protected void serializeCustomFields(JSONObject json) throws JSONException {
		json.put("reason", reason);
		if(this.winner != null){
			json.put("winner", winner);
		}
	}

	@Override
	public int getCommand() {
		return GAME_END;
	}

	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public GameEndReason getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 *
	 * @param reason the new reason
	 */
	public void setReason(GameEndReason reason) {
		this.reason = reason;
	}

	/**
	 * Gets the winner.
	 *
	 * @return the winner
	 */
	public String getWinner() {
		return winner;
	}

	/**
	 * Sets the winner.
	 *
	 * @param winner the new winner
	 */
	public void setWinner(String winner) {
		this.winner = winner;
	}

}
