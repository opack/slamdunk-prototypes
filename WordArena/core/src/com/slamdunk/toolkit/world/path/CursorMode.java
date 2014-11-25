package com.slamdunk.toolkit.world.path;

/**
 * Indique le comportement d'un PathListCursor une fois qu'il est
 * arrivé au bout du PathList
 */
public enum CursorMode {
	/**
	 * Le curseur va du début à la fin du chemin, puis s'arrête.
	 */
	NORMAL,
	
	/**
	 * Le curseur va de la fin au début du chemin, puis s'arrête.
	 */
	REVERSED,
	
	/**
	 * Le curseur va du début à la fin du chemin, puis reprend
	 * au début et boucle ainsi sans arrêt.
	 */
	LOOP,
	
	/**
	 * Le curseur va de la fin au début du chemin, puis reprend
	 * à la fin et boucle ainsi sans arrêt.
	 */
	LOOP_REVERSED,
	
	/**
	 * Le curseur va du début à la fin du chemin, puis repart en
	 * sens inverse à chaque fois qu'il arrive à une extrémité
	 * du chemin, et continue ainsi sans arrêt.
	 */
	LOOP_PINGPONG
}
