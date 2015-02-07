package com.slamdunk.wordarena;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;

/**
 * Gère le mot actuellement sélectionné et détermine si une cellule
 * peut être ajoutée au mot.
 */
public class WordSelectionHandler {
	private static final int MIN_WORD_LENGTH = 3;

	private ArenaScreen arenaScreen;
	private List<ArenaCell> selectedCells;
	private final Set<String> words;
	
	public WordSelectionHandler(ArenaScreen screen) {
		this.arenaScreen = screen;
		selectedCells = new ArrayList<ArenaCell>();
		
		words = new HashSet<String>();
		loadWords();
	}
	
	/**
	 * Charge les mots du dictionnaire
	 */
	private void loadWords() {
		words.clear();
		FileHandle file = Gdx.files.internal("words.txt");
		BufferedReader reader = new BufferedReader(file.reader("UTF-8"));
		String extracted = null;
		try {
			while ((extracted = reader.readLine()) != null) {
				if (extracted.length() >= MIN_WORD_LENGTH) {
					words.add(extracted);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ajoute la cellule indiquée au mot si elle peut l'être
	 * @return true si la cellule a pu être ajoutée au mot
	 */
	public boolean selectCell(ArenaCell cell) {
		// Vérifie que la cellule n'est pas déjà sélectionnée
		if (cell.getData().state == CellStates.SELECTED
		|| selectedCells.contains(cell)) {
			// La cellule est déjà sélectionnée : on souhaite donc
			// la désélectionner, ainsi que les suivantes
			unselectCellsFrom(cell);
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
		// Si c'est la première lettre du mot, on s'assure qu'elle est dans
		// une zone contrôlée par le joueur ou qu'elle est elle-même
		// contrôlée par le joueur
		else {
			ArenaZone zone = cell.getData().zone;
			CellOwners player = arenaScreen.getCurrentPlayer().owner;
			if (
			// La cellule n'est pas dans une zone du joueur
			(zone == null || zone.getOwner() != player)
			// La cellule n'est pas contrôlée par le joueur
			&& cell.getData().owner != player) {
				return false;
			}
		}
		
		// Tout est bon, on sélectionne la cellule puis on l'ajoute au mot
		cell.select();
		selectedCells.add(cell);
		return true;
	}
	
	private void unselectCellsFrom(ArenaCell cell) {
		int index = selectedCells.indexOf(cell);
		if (index == -1) {
			return;
		}
		
		final int lastCellIndex = selectedCells.size() - 1;
		ArenaCell removed;
		for (int curSelected = lastCellIndex; curSelected >= index; curSelected--) {
			removed = selectedCells.remove(curSelected);
			removed.unselect();
		}
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
	
	/**
	 * Tente de valider le mot. 
	 * @param selectedLetters
	 * @return true si le mot est valide
	 */
	public boolean validate() {
		// Reconstitue le mot à partir des cellules sélectionnées
		StringBuilder wordLetters = new StringBuilder();
		for (ArenaCell cell : selectedCells) {
			wordLetters.append(cell.getData().letter.label);
		}
		
		// Vérifie si le mot est valide
		return words.contains(wordLetters.toString());
	}

	public List<ArenaCell> getSelectedCells() {
		return selectedCells;
	}
}
