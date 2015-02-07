package com.slamdunk.wordarena_ecs;

/**
 * Etat des missions
 */
public enum MissionStates {
	/**
	 * Mission verrouillée : le joueur n'y a pas encore accès
	 */
	LOCKED,
	
	/**
	 * Mission déverrouillée : le joueur peut la tenter
	 */
	UNLOCKED,
	
	/**
	 * Mission accomplie
	 */
	ACCOMPLISHED;
}
