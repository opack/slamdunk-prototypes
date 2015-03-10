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
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.ReturnCodes;

/**
 * Gère le mot actuellement sélectionné et détermine si une cellule
 * peut être ajoutée au mot.
 */
public class WordSelectionHandler {
	private static final int MIN_WORD_LENGTH = 2;

	private GameManager gameManager;
	private List<ArenaCell> selectedCells;
	private final Set<String> dictionnary;
	private final Set<String> alreadyPlayed;
	private String lastValidatedWord;
	
	public WordSelectionHandler(GameManager gameManager) {
		this.gameManager = gameManager;
		selectedCells = new ArrayList<ArenaCell>();
		
		dictionnary = new HashSet<String>();
		alreadyPlayed = new HashSet<String>();
		loadWords();
	}
	
	/**
	 * Charge les mots du dictionnaire
	 */
	private void loadWords() {
		dictionnary.clear();
		FileHandle file = Gdx.files.internal("words.txt");
		BufferedReader reader = new BufferedReader(file.reader("UTF-8"));
		String extracted = null;
		try {
			while ((extracted = reader.readLine()) != null) {
				if (extracted.length() >= MIN_WORD_LENGTH) {
					dictionnary.add(extracted);
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
		if (cell.getData().selected
		|| selectedCells.contains(cell)) {
			// La cellule est déjà sélectionnée : on souhaite donc
			// la désélectionner, ainsi que les suivantes
			unselectCellsFrom(cell);
			gameManager.setCurrentWord(getCurrentWord());
			return false;
		}
		
		// Vérifie que la cellule peut être sélectionnée après la précédente
		if (!selectedCells.isEmpty()) {
			// Vérifie que la cellule est bien voisine de la dernière sélectionnée
			ArenaCell last = selectedCells.get(selectedCells.size() - 1);
			double distance = last.getData().position.distance(cell.getData().position);
			if (distance >= 2) {
				return false;
			}
			
			// Vérifie qu'il n'y a pas de mur entre les 2
			if (gameManager.hasWall(last, cell)) {
				return false;
			}
		}
		// Si c'est la première lettre du mot, on s'assure qu'elle est dans
		// une zone contrôlée par le joueur ou qu'elle est elle-même
		// contrôlée par le joueur
		else {
			ArenaZone zone = cell.getData().zone;
			Player player = gameManager.getCurrentPlayer();
			Player cellOwner = cell.getData().owner;
			// La cellule est-elle dans une zone du joueur ?
			boolean isInPlayerZone = (zone != null && player.equals(zone.getData().owner));

			// Si la cellule n'appartient pas au joueur...
			if (!player.equals(cellOwner)
			// ... et que ce n'est pas une cellule neutre dans une zone du joueur ...
			&& (!Player.NEUTRAL.equals(cellOwner) || !isInPlayerZone)) {
				// ... alors il est interdit de commencer un mot dessus
				return false;
			}
		}
		
		// Tout est bon, on sélectionne la cellule puis on l'ajoute au mot
		cell.select();
		selectedCells.add(cell);
		gameManager.setCurrentWord(getCurrentWord());
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
	 * Réinitialise le mot sélectionné, supprimant toutes les lettres,
	 * ainsi que la liste des mots joués à ce round
	 */
	public void reset() {
		cancel();
		alreadyPlayed.clear();
	}
	
	/**
	 * Tente de valider le mot. 
	 * @param selectedLetters
	 * @return true si le mot est valide
	 */
	public ReturnCodes validate() {
		// Reconstitue le mot à partir des cellules sélectionnées
		lastValidatedWord = getCurrentWord();
		
		// Vérifie si le mot est valide
		ReturnCodes result = ReturnCodes.OK;
		if (!dictionnary.contains(lastValidatedWord)) {
			result = ReturnCodes.WORD_UNKNOWN;
		}
		if (alreadyPlayed.contains(lastValidatedWord)) {
			result = ReturnCodes.WORD_ALREADY_PLAYED;
		}
		
		// Le mot est valide. Ajout à la liste des mots joués.
		if (result == ReturnCodes.OK) {
			alreadyPlayed.add(lastValidatedWord);
		}
		return result;
	}
	
	/**
	 * Réinitialise le mot sélectionné, supprimant toutes
	 * les lettres
	 */
	public void cancel() {
		for (ArenaCell cell : selectedCells) {
			cell.unselect();
		}
		selectedCells.clear();
		gameManager.setCurrentWord("");
	}

	public List<ArenaCell> getSelectedCells() {
		return selectedCells;
	}

	/**
	 * Retourne le dernier mot dont on a tenté la validation
	 * au moyen de la méthode {@link #validate()}.
	 * @return
	 */
	public String getLastValidatedWord() {
		return lastValidatedWord;
	}
	
	/**
	 * Retourne le mot actuellement sélectionné
	 * @return
	 */
	public String getCurrentWord() {
		StringBuilder word = new StringBuilder();
		for (ArenaCell cell : selectedCells) {
			word.append(cell.getData().letter.label);
		}
		return word.toString();
	}
}
