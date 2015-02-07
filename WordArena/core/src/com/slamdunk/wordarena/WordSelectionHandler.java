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
		// Si c'est la première lettre du mot, on s'assure qu'elle est dans
		// une zone contrôlée par le joueur
		else {
			ArenaZone zone = cell.getData().zone;
			if (zone == null
			|| zone.getOwner() != arenaScreen.getCurrentPlayer().owner) {
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
