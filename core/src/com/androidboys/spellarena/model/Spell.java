package com.androidboys.spellarena.model;

import org.json.JSONObject;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.net.WarpController;
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
	private Vector2 position;
	private GameWorld world;
	private Bob bob;
	private Bob enemyBob;
	private float remainingSeconds = 0;
	private State state = State.START;

	public Spell(GameWorld world) {
		this.position = null;
		this.spell = null;
		this.world = world;
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
		if (index < 3){
			return;
		}
		else if (index == 3) { //QQQ
			spell = Spells.DIVINESHIELD;
		}
		else if (index == 4) { //QQW
			spell = Spells.FORCESTAFF;
		}
		else if (index == 5) { //WWQ
			spell = Spells.STASISTRAP;
		}
		else if (index == 6) {
			if (x == y && x == z){ //WWW
				spell = Spells.ATOS;
			} else { //QQE
				spell = Spells.MINE;
			}
		}
		else if (index == 7) { //QWE
			spell = Spells.SPROUT;
		}
		else if (index == 8) { //WWE
			spell = Spells.ACID;
		}
		else if (index == 9) { //EEQ
			spell = Spells.FANOFKNIVES;
		}
		else if (index == 10) { //EEW
			spell = Spells.DARKPACT;
		}
		else if (index == 12) { //EEE
			spell = Spells.LASER;
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
			position = bob.getPosition();
			remainingSeconds = 5;
			//send position and time remaining to server
			//display on UI
			//clear on screen
			break;
		case DIVINESHIELD:
			if (bob.getManaCount()>50){
				bob.decrementManaCount(50);
				bob.setState(Bob.STATE_INVULNERABLE);
				remainingSeconds = 3;
				//send time remaining and state to server
			}
			break;
		case FORCESTAFF:
			if (bob.getManaCount()>30){
				bob.decrementManaCount(30);
				switch (bob.getDirection()) {
				case EAST:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y);
					break;
				case NORTH:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y+100);
					break;
				case NORTHEAST:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y+100);
					break;
				case NORTHWEST:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y+100);
					break;
				case SOUTH:
					bob.setPosition(bob.getPosition().x, bob.getPosition().y-100);
					break;
				case SOUTHEAST:
					bob.setPosition(bob.getPosition().x+100, bob.getPosition().y-100);
					break;
				case SOUTHWEST:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y-100);
					break;
				case WEST:
					bob.setPosition(bob.getPosition().x-100, bob.getPosition().y);
					break;

				default:
					break;
				}
			}
			else {
				System.out.println("Not enough Mana!");
			}
			break;

		case ATOS:
			if (bob.getManaCount()>50){
				bob.decrementManaCount(50);
				enemyBob.setAtosSpeed();
				remainingSeconds = 3;
			}
			else {
				System.out.println("Not enough Mana!");
			}
			break;

		case STASISTRAP:
			
			//collision with sprite model (+100 radius)
			//insert mine at bob position
			//if stasis trap near enemy,
			enemyBob.setVelocity(0, 0);
			remainingSeconds = 5;
			break;

		case SPROUT:
			//collision with sprite
			//mana cost 40

			//need add new collision			
			break;

		case DARKPACT:
			if (bob.getManaCount()>80){
				bob.decrementManaCount(80);
				remainingSeconds = 3;
				//so start animation 3 seconds before,
				//then if the state is end,			
				if (enemyBob.getPosition().x+300 > bob.getPosition().x && enemyBob.getPosition().x - 300 < bob.getPosition().x){
					if (enemyBob.getPosition().y+300 > bob.getPosition().y && enemyBob.getPosition().y - 300 < bob.getPosition().y){
						System.out.println("Enemy hit by dark pact");

						//so if within radius of 300,
						//reduce enemy life
						enemyBob.decrementLifeCount();
						//flashy graphics on server
					}
				}
			}
			else {
				System.out.println("Not enough Mana!");
			}
			break;

		case MINE:
			//collision with sprite, decrement health

			//insert mine at bob position
			break;

		case LASER:
			if (bob.getManaCount()>80){
				bob.decrementManaCount(80);
				switch (bob.getDirection()) {
				//diagonal implementations are a bit off, now its checking according to a rectangle
				case EAST:
					if (enemyBob.getPosition().x-bob.getPosition().x<300 && enemyBob.getPosition().x-bob.getPosition().x>0 && Math.abs(enemyBob.getPosition().y-bob.getPosition().y)<30) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case NORTH:
					if (enemyBob.getPosition().y-bob.getPosition().y<300 && enemyBob.getPosition().y-bob.getPosition().y>0 && Math.abs(enemyBob.getPosition().x-bob.getPosition().x)<30) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case NORTHEAST:
					if (enemyBob.getPosition().x-bob.getPosition().x<300 && enemyBob.getPosition().x-bob.getPosition().x>0 && enemyBob.getPosition().y-bob.getPosition().y<300 && enemyBob.getPosition().y-bob.getPosition().y>0) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case NORTHWEST:
					if (enemyBob.getPosition().x-bob.getPosition().x>-300 && enemyBob.getPosition().x-bob.getPosition().x<0 && enemyBob.getPosition().y-bob.getPosition().y<300 && enemyBob.getPosition().y-bob.getPosition().y>0) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case SOUTH:
					if (enemyBob.getPosition().y-bob.getPosition().y>-300 && enemyBob.getPosition().y-bob.getPosition().y<0 && Math.abs(enemyBob.getPosition().x-bob.getPosition().x)<30) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}			
					break;
				case SOUTHEAST:
					if (enemyBob.getPosition().x-bob.getPosition().x<300 && enemyBob.getPosition().x-bob.getPosition().x>0 && enemyBob.getPosition().y-bob.getPosition().y>-300 && enemyBob.getPosition().y-bob.getPosition().y<0) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case SOUTHWEST:
					if (enemyBob.getPosition().x-bob.getPosition().x>-300 && enemyBob.getPosition().x-bob.getPosition().x<0 && enemyBob.getPosition().y-bob.getPosition().y>-300 && enemyBob.getPosition().y-bob.getPosition().y<0) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				case WEST:
					if (enemyBob.getPosition().x-bob.getPosition().x>-300 && enemyBob.getPosition().x-bob.getPosition().x<0 && Math.abs(enemyBob.getPosition().y-bob.getPosition().y)<30) {
						System.out.println("Enemy hit by laser");
						enemyBob.decrementLifeCount();
					}
					break;
				default:
					break;
				}
			}
			else {
				System.out.println("Not enough Mana!");
			}
			break;

		case FANOFKNIVES:
			if (bob.getManaCount()>100){
				bob.decrementManaCount(100);
				if (enemyBob.getPosition().x+100 > bob.getPosition().x && enemyBob.getPosition().x - 100 < bob.getPosition().x){
					if (enemyBob.getPosition().y+100 > bob.getPosition().y && enemyBob.getPosition().y - 100 < bob.getPosition().y){
						System.out.println("Enemy hit by fan of knives");
						//so if within radius of 100,
						//reduce enemy life
						enemyBob.decrementLifeCount();
						//flashy graphics on server
						//no remaining count. straightaway do
					}
				}
			}
			else {
				System.out.println("Not enough Mana!");
			}
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
			WarpController.getInstance().sendGameUpdate(data.toString());
		} catch (Exception e) {
		}
	}
	
	public Spells getSpell(){
		if (this.spell == null){
			return null;
		}
		return this.spell;
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
