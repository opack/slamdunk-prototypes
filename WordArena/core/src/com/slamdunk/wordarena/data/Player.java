package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.enums.CellOwners;

/**
 * Contient les informations sur le joueur participant à la partie
 */
public class Player {
	public String name;
	public int score;
	public CellOwners owner;
	public int nbRoundsWon;
}
