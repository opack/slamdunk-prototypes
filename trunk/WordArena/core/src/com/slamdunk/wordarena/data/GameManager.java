package com.slamdunk.wordarena.data;

import java.util.List;

import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.ReturnCodes;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.utils.MaxValueFinder;

/**
 * Gère la partie
 */
public class GameManager {
	private static final int TURNS_PER_ROUND = 2;
	private static final int WINNING_ROUNDS_PER_GAME = 2;
	
	private ArenaScreen screen;
	private String arenaPlanFile;
	
	private List<Player> players;
	
	private int firstPlayer;
	private int curPlayer;
	
	private int curTurn;
	private int nbTurnsPerRound;
	
	private int curRound;
	private int nbWinningRoundsPerGame;
	
	private GameStates state;
	
	private WordSelectionHandler wordSelectionHandler;
	
	public GameManager(ArenaScreen screen) {
		this.screen = screen;
		wordSelectionHandler = new WordSelectionHandler(this);
		
		nbTurnsPerRound = TURNS_PER_ROUND;
		nbWinningRoundsPerGame = WINNING_ROUNDS_PER_GAME;
		
	}
	
	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	public void setCurrentPlayer(int playerIndex) {
		curPlayer = playerIndex;
		screen.getUI().setCurrentPlayer(getCurrentPlayer(), getCurrentTurn(), nbTurnsPerRound, getCurrentRound());
	}

	public Player getCurrentPlayer() {
		return players.get(curPlayer);
	}
	
	/**
	 * Retourne le tour courant à afficher. Au premier tour
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentTurn() {
		return curTurn + 1;
	}
	
	/**
	 * Retourne le round courant à afficher. Au premier round
	 * cette méthode retourne donc 1 et pas 0. 
	 * @return
	 */
	public int getCurrentRound() {
		return curRound + 1;
	}
	
	public void prepareGame(String arenaPlanFile, List<Player> players) {
		this.arenaPlanFile = arenaPlanFile;
		this.players = players;
		
		loadArena();
		
		firstPlayer = 0;
		setCurrentPlayer(0);
		curTurn = 0;
		curRound = 0;
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
			screen.getArena().setOwner(wordSelectionHandler.getSelectedCells(), players.get(curPlayer).owner);
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
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.cancel();
	}
	
	/**
	 * Met le jeu en pause s'il était en train de tourner
	 */
	public void pause() {
		if (state == GameStates.RUNNING) {
			changeState(GameStates.PAUSED);
		}
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
		screen.getUI().present(newState);
		
		// Si on démarre la partie, alors on affiche l'arène
		if (newState == GameStates.RUNNING) {
			screen.getArena().setVisible(true);
		} else if (newState == GameStates.PAUSED) {
			screen.getArena().setVisible(false);
		}
	}
	
	public void loadArena() {
		screen.getArena().buildArena(arenaPlanFile, wordSelectionHandler);
		screen.getArena().setVisible(false);
		
		wordSelectionHandler.reset();
		
		changeState(GameStates.READY);
	}
	
	/**
	 * Termine le coup du joueur actuel et passe au joueur suivant
	 */
	private void endStroke() {
		// Le joueur suivant est celui juste après
		curPlayer = (curPlayer + 1) % players.size();
		
		// Si tout le monde a joué, on a fini un tour
		if (curPlayer == firstPlayer) {
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
		Player winner = computeRoundWinner();
		
		if (winner == null) {
			// Fin de round sur une égalité
			System.out.println("Egalité parfaite ! Personne ne gagne ce round !");
		} else if (winner.nbRoundsWon < nbWinningRoundsPerGame) {
			// Fin de round sur une victoire
			System.out.println(winner.name + " gagne le round !");
		} else {
			// Fin de partie
			System.out.println("Game over ! " + winner.name + " gagne la partie !");
			changeState(GameStates.OVER);
			return;
		}
		
		// On passe au round suivant s'il n'y a pas de vainqueur
		curRound ++;
		
		// Réinitialise l'arène
		loadArena();
		
		// Le joueur qui débute le nouveau round n'est pas le même que celui
		// du round précédent.
		firstPlayer = (firstPlayer + 1) % players.size();
		curPlayer = firstPlayer;
		
		// On commence le premier tour de jeu
		curTurn = 0;
	}
	
	/**
	 * Détermine le gagnant du round
	 * @return
	 */
	private Player computeRoundWinner() {
		ArenaData arenaData = screen.getArena().getData();
		MaxValueFinder<CellOwners> maxValueFinder = new MaxValueFinder<CellOwners>();
		maxValueFinder.setIgnoredValue(CellOwners.NEUTRAL);
		
		// Recherche l'owner ayant le plus de zones
		for (ArenaZone zone : arenaData.zones) {
			maxValueFinder.addValue(zone.getOwner());
		}
		CellOwners winner = maxValueFinder.getMaxValue();
		
		// S'il y a égalité, le gagnant est celui occupant le plus de terrain,
		// en tenant compte de la puissance de chaque cellule
		if (winner == null) {
			maxValueFinder.reset();
			ArenaCell[][] cells = arenaData.cells;
			CellData cellData;
			for (int y = 0; y < arenaData.height; y++) {
				for (int x = 0; x < arenaData.width; x++) {
					cellData = cells[x][y].getData();
					maxValueFinder.addValue(cellData.owner, cellData.power);
				}
			}
			winner = maxValueFinder.getMaxValue();
		}
		
		// Si on entre ici, c'est qu'on a une égalité parfaite : même nombre de zones
		// et même nombre de cases occupées. On départage les joueurs au score.
		if (winner == null) {
			maxValueFinder.reset();
			for (Player player : players) {
				maxValueFinder.addValue(player.owner, player.score);
			}
			winner = maxValueFinder.getMaxValue();
		}
		
		// Récupère le joueur gagnant
		for (Player player : players) {
			if (player.owner == winner) {
				player.nbRoundsWon++;
				return player;
			}
		}
		return null;
	}
}
