package com.slamdunk.wordarena.data;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

/**
 * Représente les données d'une cellule
 */
public class CellData {
	/**
	 * Le type de la cellule
	 */
	public CellTypes type;
	
	/**
	 * Lettre initialement contenue dans la cellule, telle qu'indiquée dans le plan
	 */
	public String planLetter;
	
	/**
	 * La lettre actuellement contenue dans la cellule
	 */
	public Letters letter;
	
	/**
	 * Indique dans quel état se trouve la cellule (contrôle, dans une zone de contrôle ou neutre)
	 */
	public CellStates state;
	
	/**
	 * Indique si la cellule est actuellement sélectionnée dans un mot
	 */
	public boolean selected;
	
	/**
	 * La position à laquelle se trouve la cellule
	 * dans l'arène
	 */
	public final Point position;
	
	/**
	 * Indique qui possède la cellule
	 */
	public Owners owner;
	
	/**
	 * Indique la puissance de cette cellule
	 */
	public int power;
	
	/**
	 * Indique dans quelle zone se trouve cette cellule
	 */
	public ArenaZone zone;
	
	public CellData() {
		letter = Letters.A;
		state = CellStates.NEUTRAL;
		selected = false;
		position = new Point(0, 0);
		owner = Owners.NEUTRAL;
		power = 1;
		zone = null;
	}
}
