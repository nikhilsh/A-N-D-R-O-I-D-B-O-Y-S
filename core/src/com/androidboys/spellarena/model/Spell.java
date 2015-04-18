package com.androidboys.spellarena.model;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.math.Vector2;

public class Spell {

	//List of spells
	public enum Spells{
		DIVINESHIELD, 
		BLINK,
		HASTE,
		FIREWALL,
		BLADESTORM,
		THUNDERSTORM,
		SUNSTRIKE,
		LASER,
		SPARK,
		TORNADO;
	}

	public static enum State {
		START, END
	}

	//Type of spell
	private Spells spell;
	private int index = 0;
	private Vector2 position = new Vector2();
	private GameWorld world;
	private Bob bob;
	private int direction;
	private float remainingSeconds = 0;

	public Spell(GameWorld world, float x, float y, Spells spell, int direction) {
		this.position = position.set(x, y);
		this.spell = spell;
		this.world = world;
		this.bob = world.getPlayerModel(UserSession.getInstance().getUserName());
		this.direction = direction;
	}

	public void update(float deltaTime) {
		this.remainingSeconds -= deltaTime; //Decrease duration of spell
		if (remainingSeconds <= 0) { //End

		}
	}

	public float getRemainingSeconds() {
		return remainingSeconds;
	}

	public void setRemainingSeconds(float remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}

	/**
	 * Calculate the spell according to the state of x,y,z
	 * x,y,z: Quas is 1, Wex is 10, Exort is 100
	 * QQQ = Divine Shield
	 * QQW = Haste
	 * QQE = Blink
	 * WWQ = Tornado
	 * WWW = Thunderstorm
	 * WWE = Spark
	 * EEQ = Mine
	 * EEW = Laser
	 * EEE = Firewall
	 * QWE = Bladestorm
	 */
	public void spellSettler(int x, int y, int z) {
		index = x + y + z;
		if (index == 3) { //QQQ
			spell = Spells.DIVINESHIELD;
		}
		else if (index == 12) { //QQW
			spell = Spells.HASTE;
		}
		else if (index == 21) { //WWQ
			spell = Spells.TORNADO;
		}
		else if (index == 30) { //WWW
			spell = Spells.THUNDERSTORM;
		} 
		else if (index == 102) { //QQE
			spell = Spells.BLINK;
		}
		else if (index == 111) { //QWE
			spell = Spells.BLADESTORM;
		}
		else if (index == 120) { //WWE
			spell = Spells.SPARK;
		}
		else if (index == 201) { //EEQ
			spell = Spells.SUNSTRIKE;
		}
		else if (index == 210) { //EEW
			spell = Spells.LASER;
		}
		else if (index == 300) { //EEE
			spell = Spells.FIREWALL;
		} else {
			return;
		}
		this.world.setSpell(spell);
	}	
	
//	public void castSpell() {
//		switch (spell) {
//		case ACID:
//			//consider changing
//			//check for collision
//			//send position and time remaining to server
//			//display on UI
//			//clear on screen
//			break;
//		case DIVINESHIELD:
//			
//			break;
//		case FORCESTAFF:
//				switch (direction) {
//				case 0:
//					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y);
//					break;
//				case 1:
//					bob.setPosition(bob.getPosition().x, bob.getPosition().y+100);
//					break;
//				case 2:
//					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y+100);
//					break;
//				case 3:
//					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y+100);
//					break;
//				case 4:
//					bob.setPosition(bob.getPosition().x, bob.getPosition().y-100);
//					break;
//				case 5:
//					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y-100);
//					break;
//				case 6:
//					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y-100);
//					break;
//				case 7:
//					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y);
//					break;
//
//				default:
//					break;
//				}
//
//			break;
//
//		case ATOS:
//			
//			break;
//
//		case STASISTRAP:
//
//			//collision with sprite model (+100 radius)
//			//insert mine at bob position
//			//if stasis trap near enemy,
//			break;
//
//		case SPROUT:
//			//collision with sprite
//			//mana cost 40
//
//			//need add new collision			
//			break;
//
//		case DARKPACT:
//			
//			break;
//
//		case MINE:
//			//collision with sprite, decrement health
//
//			//insert mine at bob position
//			break;
//
//		case LASER:
//			
//			break;
//
//		case FANOFKNIVES:
//			
//		default:
//			break;
//		}
//	}

	
	public int getSpell(){

		switch (spell) {
		case DIVINESHIELD:
			return 0;
		case BLINK:
			return 1;
		case HASTE:
			return 2;
		case FIREWALL:
			return 3;
		case BLADESTORM:
			return 4;
		case THUNDERSTORM:
			return 5;
		case SUNSTRIKE:
			return 6;
		case LASER:
			return 7;
		case SPARK:
			return 8;
		case TORNADO:
			return 9;
		default:
			break;
		}
		return 0;
	}

	public Boolean checkCollision(Bob bob){
		if(bob.getPosition().x > GameWorld.WORLD_BOUND_RIGHT){
			bob.setPosition(GameWorld.WORLD_BOUND_RIGHT, bob.getPosition().y);
			return false;
		} else if (bob.getPosition().x < GameWorld.WORLD_BOUND_LEFT){
			bob.setPosition(GameWorld.WORLD_BOUND_LEFT, bob.getPosition().y);
			return false;
		}
		if(bob.getPosition().y > GameWorld.WORLD_BOUND_TOP){
			bob.setPosition(bob.getPosition().x,GameWorld.WORLD_BOUND_TOP);
			return false;
		} else if (bob.getPosition().y < GameWorld.WORLD_BOUND_BOTTOM){
			bob.setPosition(bob.getPosition().x,GameWorld.WORLD_BOUND_BOTTOM);
			return false;
		}
		return true;
	}

}
