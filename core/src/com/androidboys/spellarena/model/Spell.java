package com.androidboys.spellarena.model;

import org.json.JSONObject;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.net.WarpController;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
		START, END
	}

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

	/**
	 * Calculate the spell according to the state of x,y,z
	 * x,y,z: Quas is 1, Wex is 2, Exort is 4
	 * QQQ = Divine Shield
	 * QQW = Force Staff
	 * QQE = Mine
	 * WWQ = Stasis Trap
	 * WWW = Atos
	 * WWE = Acid
	 * EEQ = Fan of Knives
	 * EEW = Dark Pact
	 * EEE = Laser
	 * QWE = Sprout
	 */
	public void spellSettler(int x, int y, int z) {
		index = x + y + z;
		if (index == 3) { //QQQ
			spell = Spells.DIVINESHIELD;
		}
		else if (index == 12) { //QQW
			spell = Spells.FORCESTAFF;
		}
		else if (index == 21) { //WWQ
			spell = Spells.STASISTRAP;
		}
		else if (index == 30) { //WWW
			spell = Spells.ATOS;
		} 
		else if (index == 102) { //QQE
			spell = Spells.MINE;
		}
		else if (index == 111) { //QWE
			spell = Spells.SPROUT;
		}
		else if (index == 120) { //WWE
			spell = Spells.ACID;
		}
		else if (index == 201) { //EEQ
			spell = Spells.FANOFKNIVES;
		}
		else if (index == 210) { //EEW
			spell = Spells.DARKPACT;
		}
		else if (index == 300) { //EEE
			spell = Spells.LASER;
		} else {
			return;
		}
		this.world.setSpell(spell);
		castSpell();
	}
	
	/**
	 * Cast the specified spell
	 * Acid (WWE): TODO (I don't understand this)
	 * Divine Shield (QQQ): Invulnerable for 3 sec. Mana cost: 50
	 * Force Staff (QQW): Pushes Bob 100 units in the direction he is facing. Mana cost: 30
	 * Atos (WWW): Slows the enemy's movement speed. (velocity=100, 3 sec) Mana cost: 50.
	 * Statis Trap (WWQ): Prohibit enemy's movement for 5 sec. (TODO no mana cost?)
	 * Sprout (QWE): TODO
	 * Dark Pact (EEW): HP-. Radius: 300. Duration: 3 sec. Mana cost: 80. REPLACE
	 * Mine (QQE): TODO
	 * Laser (EEE): HP-. Area: 300X30. Duration: Instantaneous. Mana cost: 80.
	 * Fan of Knives (EEQ): HP-. Radius: 100. Duration: ???(TODO). Mana cost: 100.
	 * 
	 * SUGGESTION: What's the difference between EEW and EEQ (only area?)
	 */
	
	
	
	public void castSpell() {
		switch (spell) {
		case ACID:
			//consider changing
			//check for collision
			//send position and time remaining to server
			//display on UI
			//clear on screen
			break;
		case DIVINESHIELD:
			if (bob.getManaCount()>50){
				bob.decrementManaCount(50);
				bob.setState(Bob.STATE_INVULNERABLE);
				//send time remaining and state to server
			}
			break;
		case FORCESTAFF:
			if (bob.getManaCount()>30){
				bob.decrementManaCount(30);
				switch (direction) {
				case 0:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y);
					break;
				case 1:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y+100);
					break;
				case 2:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y+100);
					break;
				case 3:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y+100);
					break;
				case 4:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y-100);
					break;
				case 5:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y-100);
					break;
				case 6:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y-100);
					break;
				case 7:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y);
					break;

				default:
					break;
				}
			}
			else {
				System.out.println("No enough Mana!");
			}
			break;

		case ATOS:
			if (bob.getManaCount()>50){
				bob.decrementManaCount(50);
			}
			else {
				System.out.println("No enough Mana!");
			}
			break;

		case STASISTRAP:

			//collision with sprite model (+100 radius)
			//insert mine at bob position
			//if stasis trap near enemy,
			break;

		case SPROUT:
			//collision with sprite
			//mana cost 40

			//need add new collision			
			break;

		case DARKPACT:
			if (bob.getManaCount()>80){
				bob.decrementManaCount(80);
				//so start animation 3 seconds before,
				//then if the state is end,			
			
			}
			else {
				System.out.println("No enough Mana!");
			}
			break;

		case MINE:
			//collision with sprite, decrement health

			//insert mine at bob position
			break;

		case LASER:
			if (bob.getManaCount()>80){
				bob.decrementManaCount(80);
				
			}
			else {
				System.out.println("No enough Mana!");
			}
			break;

		case FANOFKNIVES:
			if (bob.getManaCount()>100){
				bob.decrementManaCount(100);
				
			}
			else {
				System.out.println("No enough Mana!");
			}
		default:
			break;
		}
	}

	
	public int getSpell(){

		switch (spell) {
		case DIVINESHIELD:
			return 0;
		case FORCESTAFF:
			return 1;
		case ATOS:
			return 2;
		case STASISTRAP:
			return 3;
		case SPROUT:
			return 4;
		case DARKPACT:
			return 5;
		case MINE:
			return 6;
		case LASER:
			return 7;
		case ACID:
			return 8;
		case FANOFKNIVES:
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
