package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.enums.Owners;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class Player {
	public String name;
	public int score;
	public Owners owner;
	public int nbRoundsWon;
	public int nbZonesOwned;
	public int nbWordsPlayed;
}
