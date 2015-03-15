package com.slamdunk.wordarena.data;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Borders;

/**
 * Représente un côté d'une zone s'étendant entre 2 cellules
 */
public class EdgeData {
	public ArenaCell cell;
	public Borders border;
	
	/**
	 * Par convention, les points de la zone sont définis dans le sens des aiguilles
	 * d'une montre. Ainsi, selon le côté, p1 désignera :
	 * 	- border = LEFT : p1 = coin bas-gauche
	 *  - border = TOP : p1 = coin haut-gauche
	 *  - border = RIGHT : p1 = coin haut-droit
	 *  - border = BOTTOM : p1 = coin bas-droit 
	 */
	public final Vector2 p1;
	
	/**
	 * Par convention, les points de la zone sont définis dans le sens des aiguilles
	 * d'une montre. Ainsi, selon le côté, p2 désignera :
	 * 	- border = LEFT : p2 = coin haut-gauche
	 *  - border = TOP : p2 = coin haut-droit
	 *  - border = RIGHT : p2 = coin bas-droit
	 *  - border = BOTTOM : p2 = coin bas-gauche
	 */
	public final Vector2 p2;
	
	public EdgeData() {
		p1 = new Vector2(0, 0);
		p2 = new Vector2(0, 0);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof EdgeData) {
			// Identique si c'est le même côté de la même cellule
			EdgeData edge2 = (EdgeData)other;
			return edge2.border == border
				&& edge2.cell.equals(cell);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return border.hashCode() ^ cell.hashCode();
	}
}
