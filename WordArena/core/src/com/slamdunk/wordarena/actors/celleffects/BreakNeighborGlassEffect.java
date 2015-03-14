package com.slamdunk.wordarena.actors.celleffects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.enums.CellTypes;

/**
 * Effet qui consiste à casser le verre des cellules voisines
 */
public class BreakNeighborGlassEffect implements CellEffect {
	/**
	 * Liste de travail qui contient les voisins à chaque appel
	 * à applyEffect. Permet d'éviter l'instanciation d'une
	 * nouvelle liste à chaque fois.
	 */
	private List<ArenaCell> tmpNeighbors;
	
	public BreakNeighborGlassEffect() {
		tmpNeighbors = new ArrayList<ArenaCell>(8);
	}
	
	@Override
	public void applyEffect(ArenaCell cell, ArenaData arena) {
		// Vide la liste des voisins
		tmpNeighbors.clear();
		arena.getNeighbors4(cell, tmpNeighbors);
		
		// Parcours chaque voisin pour voir s'il est en verre
		for (ArenaCell neighbor : tmpNeighbors) {
			if (neighbor.getData().type != CellTypes.G) {
				continue;
			}
			// TODO Petite explosion de verre
			
			// La cellule touchée change de type et devient une cellule normale
			neighbor.getData().type = CellTypes.L;
			
			// Mise à jour de l'apparence de la cellule
			neighbor.updateDisplay();
		}
	}
}
