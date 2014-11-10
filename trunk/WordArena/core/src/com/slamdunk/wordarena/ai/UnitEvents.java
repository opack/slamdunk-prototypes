package com.slamdunk.wordarena.ai;

/**
 * Evènements qui peuvent survenir au cours de la vie d'une unité
 */
public enum UnitEvents {
	/**
	 * L'unité s'est déplacée d'une position
	 */
	MOVED_ONE_POSITION,
	
	/**
	 * L'unité est arrivée au bout du chemin
	 */
	ARRIVED_AT_DESTINATION;
}
