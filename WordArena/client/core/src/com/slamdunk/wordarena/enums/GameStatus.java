package com.slamdunk.wordarena.enums;

/**
 * Donne l'état d'une partie affichée dans la liste des parties en cours.
 */
public enum GameStatus {
	/**
	 * C'est à l'utilisateur de jouer
	 */
	USER_TURN,
	
	/**
	 * C'est à un adversaire de jouer
	 */
	OPPONENT_TURN,
	
	/**
	 * La partie est terminée
	 */
	GAME_OVER;
}
