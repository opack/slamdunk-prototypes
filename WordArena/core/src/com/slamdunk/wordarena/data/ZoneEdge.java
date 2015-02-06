package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Borders;

/**
 * Représente un côté d'une zone s'étendant entre 2 cellules
 */
public class ZoneEdge {
	public ArenaCell cell;
	public Borders border;
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ZoneEdge) {
			// Identique si c'est le même côté de la même cellule
			ZoneEdge edge2 = (ZoneEdge)other;
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
