package com.slamdunk.pixelkingdomadvanced.screens.battlefield.goals;

import com.slamdunk.pixelkingdomadvanced.units.Factions;
import com.slamdunk.pixelkingdomadvanced.units.SimpleUnit;
import com.slamdunk.pixelkingdomadvanced.units.Units;

/**
 * Détermine la fin d'une partie de type Elimination, où le joueur
 * doit éliminer un nid de monstres ou un camp de bandits.
 */
public class EliminationGoal extends GoalManager {
	@Override
	public void onBuildingAttacked(SimpleUnit building, SimpleUnit attacker) {
		if (building.getType() == Units.CASTLE
		&& building.isDead()) {
			if (building.getFaction() == Factions.PLAYER) {
				setGameState(GameState.PLAYER_LOST);
			} else {
				setGameState(GameState.PLAYER_WON);
			}
		}
	}
}
