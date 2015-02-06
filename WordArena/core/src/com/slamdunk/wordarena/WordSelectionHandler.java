package com.slamdunk.wordarena;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Gère le mot actuellement sélectionné et détermine si une cellule
 * peut être ajoutée au mot.
 */
public class WordSelectionHandler {
	private static WordSelectionHandler instance = new WordSelectionHandler();
	
	private List<ArenaCell> selectedCells;
	
	private WordSelectionHandler() {
		selectedCells = new ArrayList<ArenaCell>();
	}
	
	public static WordSelectionHandler getInstance() {
		return instance;
	}
	
	/**
	 * Ajoute la cellule indiquée au mot si elle peut l'être
	 * @return true si la cellule a pu être ajoutée au mot
	 */
	public boolean addCell(ArenaCell cell) {
		// Vérifie que la cellule n'est pas déjà sélectionnée
		if (cell.getData().state == CellStates.SELECTED
		|| selectedCells.contains(cell)) {
			return false;
		}
		
		// Vérifie que la cellule est bien voisine de la dernière sélectionnée
		if (!selectedCells.isEmpty()) {
			ArenaCell last = selectedCells.get(selectedCells.size() - 1);
			double distance = last.getData().position.distance(cell.getData().position);
			if (distance >= 2) {
				return false;
			}
		}
		
		// Tout est bon, on sélectionne la cellule puis on l'ajoute au mot
		cell.select();
		selectedCells.add(cell);
		return true;
	}
	
	/**
	 * Réinitialise le mot sélectionné, supprimant toutes les lettres
	 */
	public void reset() {
		for (ArenaCell cell : selectedCells) {
			cell.unselect();
		}
		selectedCells.clear();
	}
}
