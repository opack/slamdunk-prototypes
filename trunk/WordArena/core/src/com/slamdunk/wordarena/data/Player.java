package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.Assets;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class Player {
	public static final Player NEUTRAL;
	static {
		NEUTRAL = new Player();
		NEUTRAL.uid = 0;
		NEUTRAL.name = "neutral";
		NEUTRAL.cellPack = Assets.CELL_PACK_NEUTRAL;
	}
	
	public int uid;
	public String name;
	public int score;
	public int nbRoundsWon;
	public int nbZonesOwned;
	public int nbWordsPlayed;
	public String cellPack;
	
	@Override
	public int hashCode() {
		return uid + name.hashCode() * 31;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null
		&& (obj instanceof Player)) {
			return uid == ((Player)obj).uid;
		}
		return false;
	}
}
