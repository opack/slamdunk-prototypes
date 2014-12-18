package com.slamdunk.wordarena.screens.game.objectives;

import com.slamdunk.wordarena.units.Units;

/**
 * Cette classe est notifiée de tous les évènements qui pourraient conduire à une fin de jeu.
 * Suivant le scénario, elle décide alors si le jeu est terminé, et dans ce cas si le joueur
 * a gagné ou perdu.
 */
public class EndGameManager {
	private GameState gameState;
	
	/**
	 * Appelée lorsqu'un bâtiment vient de subir des dégâts
	 * @param building Le bâtiment qui vient d'être attaqué
	 * @param attacker L'unité qui a attaqué le bâtiment
	 */
	public void onBuildingAttacked(Units building, Units attacker) {
	}

	/**
	 * Appelée lorsqu'une unité vient de mourir.
	 * @param dead
	 */
	public void onUnitDead(Units dead) {
	}
	
	/**
	 * Indique si le jeu est terminé, donc que le joueur a perdu
	 * ou gagné
	 * @return
	 */
	public boolean isGameFinished() {
		return gameState != GameState.RUNNING;
	}
	
	/**
	 * Retourne l'état de la partie, càd si elle est en cours
	 * ou si le joueur a gagné ou perdu (fin de partie)
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * Définit l'état de la partie, càd si elle est en cours
	 * ou si le joueur a gagné ou perdu (fin de partie)
	 */
	protected void setGameState(GameState gameState) {
		if (gameState != null) {
			this.gameState = gameState;
		}
	}
}
