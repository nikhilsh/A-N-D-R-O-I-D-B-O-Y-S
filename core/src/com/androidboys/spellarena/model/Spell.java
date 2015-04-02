package com.androidboys.spellarena.model;

public class Spell {

	public enum Spells{
		DIVINESHIELD, 
		FORCESTAFF,
		ATOS,
		STATISTRAP,
		SPROUT,
		DARKPACT,
		MINE,
		LASER,
		ACID,
		FANOFKNIVES;
	}

	private Spells spell;
	private int index = 0;

	public Spell() {

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
			spell = Spells.STATISTRAP;
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

			break;
		case DIVINESHIELD:

			break;
		case FORCESTAFF:

			break;

		case ATOS:

			break;

		case STATISTRAP:

			break;

		case SPROUT:

			break;

		case DARKPACT:

			break;

		case MINE:

			break;

		case LASER:

			break;

		case FANOFKNIVES:

			break;

		default:
			break;
		}


	}




}
