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
		HURRICANE,
		THUNDERSTORM,
		EXPLOSION,
		LASER,
		SPECTRALTHROW,
		SHADOWBLAST;
	}

	public static enum State {
		START, END
	}

	//Type of spell
	private Spells spell;
	private int index = 0;
	private Vector2 position = new Vector2();
	private GameWorld world;
	private float remainingSeconds = 0;

	/**
	 * Instantiates a new spell.
	 *
	 * @param world the world
	 * @param x the x
	 * @param y the y
	 * @param spell the spell
	 * @param direction the direction
	 */
	public Spell(GameWorld world, float x, float y, Spells spell, int direction) {
		this.position = position.set(x, y);
		this.spell = spell;
		this.world = world;
	}

	/**
	 * Update.
	 *
	 * @param deltaTime the delta time
	 */
	public void update(float deltaTime) {
		this.remainingSeconds -= deltaTime; //Decrease duration of spell
		if (remainingSeconds <= 0) { //End
		}
	}

	/**
	 * Gets the remaining seconds.
	 *
	 * @return the remaining seconds
	 */
	public float getRemainingSeconds() {
		return remainingSeconds;
	}

	public void setRemainingSeconds(float remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}

	/**
	 * Calculate the spell according to the state of x,y,z
	 * x,y,z: Ice is 1, Lightning is 10, Fire is 100
	 * QQQ = Divine Shield
	 * QQW = Haste
	 * QQE = Blink
	 * WWQ = Shadow Blast
	 * WWW = Thunderstorm
	 * WWE = Spark
	 * EEQ = Mine
	 * EEW = Laser
	 * EEE = Firewall
	 * QWE = Bladestorm
	 * where Q = Ice
	 * where W = Lightning
	 * where E = Fire
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
			spell = Spells.SHADOWBLAST;
		}
		else if (index == 30) { //WWW
			spell = Spells.THUNDERSTORM;
		} 
		else if (index == 102) { //QQE
			spell = Spells.BLINK;
		}
		else if (index == 111) { //QWE
			spell = Spells.HURRICANE;
		}
		else if (index == 120) { //WWE
			spell = Spells.SPECTRALTHROW;
		}
		else if (index == 201) { //EEQ
			spell = Spells.EXPLOSION;
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

	/**
	 * Gets the integer value of spell enum.
	 *
	 * @return the spell
	 */
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
		case HURRICANE:
			return 4;
		case THUNDERSTORM:
			return 5;
		case EXPLOSION:
			return 6;
		case LASER:
			return 7;
		case SPECTRALTHROW:
			return 8;
		case SHADOWBLAST:
			return 9;
		default:
			break;
		}
		return 0;
	}
}
