package com.androidboys.spellarena.model;

import org.json.JSONObject;

import appwarp.WarpController;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.badlogic.gdx.math.Vector2;

public class Spell {

	public enum Spells{
		DIVINESHIELD, 
		FORCESTAFF,
		ATOS,
		STASISTRAP,
		SPROUT,
		DARKPACT,
		MINE,
		LASER,
		ACID,
		FANOFKNIVES;
	}

	public static enum State {
		COUNT_DOWN, END
	}

	private Spells spell;
	private int index = 0;
	private Vector2 position;
	private GameWorld world;
	private Bob bob;
	private Bob enemyBob;
	private float remainingSeconds = 0;
	private State state = State.COUNT_DOWN;


	public Spell(int i, int j) {
		this.position = new Vector2(i,j);
		this.spell = null;
		this.world = GameWorld.getInstance();
		this.bob = this.world.getBob();
		this.enemyBob = this.world.getEnemy();

	}

	public void update(float deltaTime) {
		this.remainingSeconds -= deltaTime;
		if (remainingSeconds <= 0) {

		}
	}

	public float getRemainingSeconds() {
		return remainingSeconds;
	}

	public void setRemainingSeconds(float remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}

	//Quas is 1, Wex is 2, Exort is 4
	//call method to cast spell
	public void spellSettler(int x, int y, int z) {
		index = x + y + z;
		if (index < 3){
			return;
		}
		else if (index == 3) {
			spell = Spells.DIVINESHIELD;
		}
		else if (index == 4) {
			spell = Spells.FORCESTAFF;
		}
		else if (index == 5) {
			spell = Spells.STASISTRAP;
		}
		else if (index == 6) {
			if (x == y && x == z){
				spell = Spells.ATOS;
			}
			else {
				spell = Spells.MINE;
			}
		}
		else if (index == 7) {
			spell = Spells.SPROUT;
		}
		else if (index == 8) {
			spell = Spells.ACID;
		}
		else if (index == 9) {
			spell = Spells.FANOFKNIVES;
		}
		else if (index == 10) {
			spell = Spells.DARKPACT;
		}
		else if (index == 12) {
			spell = Spells.LASER;
		}

		generateSpell();
	}

	public void generateSpell() {
		switch (spell) {
		case ACID:
			position = bob.getPosition();
			remainingSeconds = 5;
			//send position and time remaining to server
			//display on UI
			//clear on screen
			break;
		case DIVINESHIELD:
			bob.setState(bob.STATE_INVULNERABLE);
			remainingSeconds = 3;
			//send time remaining and state to server
			break;
		case FORCESTAFF:
			switch (bob.getDirection()) {
				case EAST:
					bob.setPosition(bob.getPosition().x+50, bob.getPosition().y);
					break;
				case NORTH:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y+50);
					break;
				case NORTHEAST:
					bob.setPosition(bob.getPosition().x+50, bob.getPosition().y+50);
					break;
				case NORTHWEST:
					bob.setPosition(bob.getPosition().x-50, bob.getPosition().y+50);
					break;
				case SOUTH:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y-50);
					break;
				case SOUTHEAST:
					bob.setPosition(bob.getPosition().x+50, bob.getPosition().y-50);
					break;
				case SOUTHWEST:
					bob.setPosition(bob.getPosition().x-50, bob.getPosition().y-50);
					break;
				case WEST:
					bob.setPosition(bob.getPosition().x-50, bob.getPosition().y);
					break;

				default:
					break;
				}
			break;

		case ATOS:
			enemyBob.setVelocity(100, 100);
			remainingSeconds = 3;
			break;

		case STASISTRAP:
			//insert mine at bob position
			//if stasis trap near enemy,
			enemyBob.setVelocity(0, 0);
			remainingSeconds = 5;
			break;

		case SPROUT:
			
			break;

		case DARKPACT:
			remainingSeconds = 3;
			//so start animation 3 seconds before,
			//then if the state is end,
			if (enemyBob.getPosition().x+100 > bob.getPosition().x && enemyBob.getPosition().x - 100 < bob.getPosition().x){
				if (enemyBob.getPosition().y+100 > bob.getPosition().y && enemyBob.getPosition().y - 100 < bob.getPosition().y){
					//so if within radius of 100,
					//reduce enemy life
					enemyBob.decrementLifeCount();
					//flashy graphics on server
				}
			}
			break;

		case MINE:
			//insert mine at bob position
			break;

		case LASER:
			switch (bob.getDirection()) {
			case EAST:
				
				break;
			case NORTH:
				break;
			case NORTHEAST:
				break;
			case NORTHWEST:
				break;
			case SOUTH:
				break;
			case SOUTHEAST:
				break;
			case SOUTHWEST:
				break;
			case WEST:
				break;

			default:
				break;
			}
			break;

		case FANOFKNIVES:
			if (enemyBob.getPosition().x+100 > bob.getPosition().x && enemyBob.getPosition().x - 100 < bob.getPosition().x){
				if (enemyBob.getPosition().y+100 > bob.getPosition().y && enemyBob.getPosition().y - 100 < bob.getPosition().y){
					//so if within radius of 100,
					//reduce enemy life
					enemyBob.decrementLifeCount();
					//flashy graphics on server
					//no remaining count. straightaway do
				}
			}
			break;

		default:
			break;
		}
		//do spell everytime
		sendSpell();
	}

	public void sendSpell(){
		try {
			JSONObject data = new JSONObject();
			data.put("x", position.x);
			data.put("y", position.y);
			data.put("spell", spell);
			data.put("remainingSeconds", remainingSeconds);
			data.put("state", state);
			WarpController.getInstance().sendGameUpdate(data.toString());
		} catch (Exception e) {
		}
	}




}
