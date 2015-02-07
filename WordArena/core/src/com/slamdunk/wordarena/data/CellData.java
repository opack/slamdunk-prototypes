package com.slamdunk.wordarena.data;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;

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
	 * Indique qui possède la cellule
	 */
	public CellOwners owner;
	
	/**
	 * Indique le poids de cette cellule
	 */
	public int weight;
	
	public CellData() {
		letter = Letters.A;
		state = CellStates.NORMAL;
		position = new Point(0, 0);
		owner = CellOwners.NEUTRAL;
		weight = 1;
	}
}
