package com.slamdunk.wordarena.screens.arena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.MyGestureHandler;
import com.slamdunk.wordarena.actors.ZoomInputProcessor;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.ReturnCodes;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	private static final int TURNS_PER_ROUND = 10;
	private static final int ROUNDS_TO_WIN = 2;
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	private WordSelectionHandler wordSelectionHandler;
	
	private String arenaPlanFile;
	private List<Player> players;
	
	private int curPlayer;
	private int curTurn;
	private int curRound;
	
	public ArenaScreen(SlamGame game) {
		super(game);
		wordSelectionHandler = new WordSelectionHandler(this);
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI();
		addOverlay(ui);

		// Gestionnaires permettant de zoomer avec la souris ou un pinch.
		// Ces gestionnaires sont insérés après ArenaUI.
		OrthographicCamera camera = (OrthographicCamera)arena.getStage().getCamera();
		getInputMultiplexer().addProcessor(1, new GestureDetector(new MyGestureHandler(camera)));
		getInputMultiplexer().addProcessor(1, new ZoomInputProcessor(camera));
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
	
	public void prepareGame(String arenaPlanFile, List<Player> players) {
		this.arenaPlanFile = arenaPlanFile;
		this.players = players;
		
		loadArena();
		
		setCurrentPlayer(0);
		curTurn = 0;
		curRound = 0;
	}

	public void loadArena() {
		arena.buildArena(arenaPlanFile);
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
			endStroke();
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
	 * Termine le coup du joueur actuel et passe au joueur suivant
	 */
	private void endStroke() {
		// Le joueur suivant est celui juste après
		curPlayer += 1;
		
		// Si tout le monde a joué, on a fini un tour
		if (curPlayer == players.size()) {
			curPlayer = 0;
			endTurn();
		}
		setCurrentPlayer(curPlayer);
	}
	
	/**
	 * Termine le tour, c'est-à-dire que chaque joueur a joué
	 * son coup.
	 */
	private void endTurn() {
		curTurn ++;
		if (curTurn == TURNS_PER_ROUND) {
			// Le round est terminé
			endRound();
		}
	}
	
	/**
	 * Fin du round
	 */
	private void endRound() {
		// Détermine le gagnant du round
		// ...
		
		// Détermine la fin de partie
		// ...
		
		// On passe au round suivant s'il n'y a pas de vainqueur
		curRound ++;
		
		// Réinitialise l'arène
		loadArena();
		
		// Le joueur qui débute le nouveau round n'est pas le même que celui
		// du round précédent.
		curPlayer = curRound % players.size();
		
		// On commence le premier tour de jeu
		curTurn = 0;
	}
	
	private void setCurrentPlayer(int playerIndex) {
		curPlayer = playerIndex;
		ui.setCurrentPlayer(players.get(playerIndex), curTurn, TURNS_PER_ROUND, curRound);
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
