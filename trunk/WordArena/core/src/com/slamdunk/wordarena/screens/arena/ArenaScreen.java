package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.ReturnCodes;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	private static final int TURNS_PER_ROUND = 10;
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	private WordSelectionHandler wordSelectionHandler;
	
	private List<Player> players;
	private int curPlayer;
	private int curTurn;
	private int curRound;
	
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
		setCurrentPlayer(0);
		loadLevel();
	}

	public void loadLevel() {
		arena.buildArena("arenas/2.properties");
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
		ReturnCodes result = wordSelectionHandler.validate();
		String word = wordSelectionHandler.getLastValidatedWord();
		switch (result) {
		case OK:
			System.out.println(word + " est valide ! C'est au joueur suivant de jouer.");
			// Toutes les cellules passent sous la domination du joueur
			arena.setOwner(wordSelectionHandler.getSelectedCells(), players.get(curPlayer).owner);
			// Fin du tour de ce joueur
			endTurn();
			break;
		case WORD_ALREADY_PLAYED:
			System.out.println(word + " a déjà été joué pendant ce round. Merci de jouer un autre mot.");
			break;
		case WORD_UNKNOWN:
			System.out.println(word + " n'existe pas dans le dictionnaire de WordArena. Merci de jouer un autre mot.");
			break;
		}
		cancelWord();
	}

	/**
	 * Termine le tour du joueur actuel et passe au joueur suivant
	 */
	private void endTurn() {
		// Le joueur suivant est celui juste après
		curPlayer += 1;
		if (curPlayer == players.size()) {
			curPlayer = 0;
			
			// Le tour est terminé pour les 2 joueurs
			curTurn ++;
			if (curTurn > TURNS_PER_ROUND) {
				// Le round est terminé
//				endRound();
				// Le joueur qui débute le nouveau round n'est pas le même que celui
				// du round précédent.
				curPlayer = curRound % players.size();
			}
		}
		setCurrentPlayer(curPlayer);
	}
	
	private void setCurrentPlayer(int playerIndex) {
		curPlayer = playerIndex;
		ui.setCurrentPlayer(players.get(playerIndex), curTurn, TURNS_PER_ROUND);
	}

	public Player getCurrentPlayer() {
		return players.get(curPlayer);
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.cancel();
	}

	public void centerCamera() {
		arena.centerCamera();
	}
}
