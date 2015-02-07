package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	private WordSelectionHandler wordSelectionHandler;
	
	private List<Player> players;
	private int curPlayer;
	
	public ArenaScreen(SlamGame game) {
		super(game);
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI();
		addOverlay(ui);
		
		wordSelectionHandler = new WordSelectionHandler(this);
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	@Override
	public void pause () {
		if (state == GameStates.RUNNING) {
			changeState(GameStates.PAUSED);
		}
	}
	
	public void prepareGame(List<Player> players) {
		this.players = players;
		curPlayer = 0;
		loadNextLevel();
	}

	public void loadNextLevel() {
		arena.buildArena(10, 10);
		arena.setVisible(false);
		wordSelectionHandler.reset();
		changeState(GameStates.READY);
	}

	/**
	 * Change l'état actuel du jeu et met à jour l'IHM
	 * @param newState
	 */
	public void changeState(GameStates newState) {
		if (newState == state) {
			return;
		}
		state = newState;
		ui.present(newState);
		
		// Si on démarre la partie, alors on affiche l'arène
		if (newState == GameStates.RUNNING) {
			arena.setVisible(true);
		} else if (newState == GameStates.PAUSED) {
			arena.setVisible(false);
		}
	}
	
	/**
	 * Vérifie si le mot est valide, ajoute des points au score
	 * le cas échéant et choisit d'autres lettres sur le mot
	 * sélectionné.
	 */
	public void validateWord() {
		if (wordSelectionHandler.validate()) {
			System.out.println("ArenaScreen.validateWord() Mot valide B-)");
			// Toutes les cellules passent sous la domination du joueur
			arena.setOwner(wordSelectionHandler.getSelectedCells(), players.get(curPlayer).owner);
			// Fin du tour de ce joueur
			endTurn();
		} else {
			System.out.println("ArenaScreen.validateWord() Mot invalide :(");
		}
		
		cancelWord();
	}

	/**
	 * Termine le tour du joueur actuel et passe au joueur suivant
	 */
	private void endTurn() {
		// Le joueur suivant est celui juste après
		curPlayer = (curPlayer + 1) % players.size();
	}
	
	public Player getCurrentPlayer() {
		return players.get(curPlayer);
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.reset();
	}
}
