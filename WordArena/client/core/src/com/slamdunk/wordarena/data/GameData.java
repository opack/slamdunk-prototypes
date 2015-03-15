package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.enums.GameTypes;

/**
 * Regroupe les données d'une partie en cours
 */
public class GameData {
	/**
	 * Le type de la partie
	 */
	public GameTypes gameType;
	
	/**
	 * Les adversaires
	 */
	public Player[] players;
	
	/**
	 * Indique le joueur dont c'est le tour
	 */
	public int currentPlayer;
	
	/**
	 * Indique si la partie est terminée
	 */
	public boolean gameOver;
}
