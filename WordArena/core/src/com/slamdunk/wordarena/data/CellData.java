package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.Map;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.enums.Zones;

/**
 * Représente les données d'une cellule
 */
public class CellData {
	/**
	 * La lettre contenue dans la cellule
	 */
	public Letters letter;
	
	/**
	 * L'état dans lequel se trouve la cellule
	 */
	public CellStates state;
	
	/**
	 * La position à laquelle se trouve la cellule
	 * dans l'arène
	 */
	public final Point position;
	
	/**
	 * Indique quel dessin de zone doit être représenté
	 * sur le bord spécifié
	 */
	public final Map<Borders, Zones> zoneOnBorder;
	
	public CellData() {
		letter = Letters.A;
		state = CellStates.NORMAL;
		position = new Point(0, 0);
		zoneOnBorder = new HashMap<Borders, Zones>();
		for (Borders border : Borders.values()) {
			zoneOnBorder.put(border, Zones.NONE);
		}
	}
}
