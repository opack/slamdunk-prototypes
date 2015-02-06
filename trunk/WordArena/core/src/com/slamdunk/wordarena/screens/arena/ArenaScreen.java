package com.slamdunk.wordarena.screens.arena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.enums.GameStates;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	private WordSelectionHandler wordSelectionHandler;

	public ArenaScreen(SlamGame game) {
		super(game);
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI();
		addOverlay(ui);
		
		wordSelectionHandler = new WordSelectionHandler();
		loadNextLevel();
		changeState(GameStates.READY);
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

	public void loadNextLevel() {
		arena.buildArena(10, 10);
		wordSelectionHandler.reset();
	}

	/**
	 * Change l'état actuel du jeu et met à jour l'IHM
	 * @param newState
	 */
	public void changeState(GameStates newState) {
		state = newState;
		ui.present(newState);
	}
	
	/**
	 * Vérifie si le mot est valide, ajoute des points au score
	 * le cas échéant et choisit d'autres lettres sur le mot
	 * sélectionné.
	 */
	public void validateWord() {
		if (wordSelectionHandler.validate()) {
			System.out.println("ArenaScreen.validateWord() Mot valide B-)");
		} else {
			System.out.println("ArenaScreen.validateWord() Mot invalide :(");
		}
		
		cancelWord();
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.reset();
	}
}
