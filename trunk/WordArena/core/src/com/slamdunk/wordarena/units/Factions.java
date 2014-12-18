package com.slamdunk.wordarena.units;

/**
 * Les différents camps auxquels peut appartenir une unité.
 * Permet de déterminer quelles unités sont alliées ou ennemies.
 */
public enum Factions {
	/**
	 * Le camp du joueur
	 */
	PLAYER,
	
	/**
	 * Un autre camp : CPU, autre joueur
	 */
	ENEMY;

	/**
	 * Retourne la faction ennemie de celle passée en paramètre
	 * @param faction
	 * @return
	 */
	public static Factions enemyOf(Factions faction) {
		return faction == PLAYER ? ENEMY : PLAYER;
	}
}
