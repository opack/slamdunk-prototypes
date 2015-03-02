package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.enums.ReturnCodes;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.screens.arena.ArenaUI;
import com.slamdunk.wordarena.utils.MaxValueFinder;

/**
 * Gère la partie
 */
public class GameManager {
	private static final int TURNS_PER_ROUND = 5;
	private static final int WINNING_ROUNDS_PER_GAME = 2;
	
	/**
	 * Nombre de rounds maximum. Si on arrive à ce dernier round, on fait tout pour
	 * éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
	 * cellules, ou de score.
	 */
	private static final int MAX_NB_ROUNDS_PER_GAME = 3;
	
	private static final int BONUS1_MIN_LENGTH = 5;
	private static final int BONUS1_POINTS = 2;
	private static final int BONUS2_MIN_LENGTH = 8;
	private static final int BONUS2_POINTS = 3;
	private static final int BONUS3_MIN_LENGTH = 10;
	private static final int BONUS3_POINTS = 5;
	
	private static final int SCORE_ZONE_STEALED = 5;
	private static final int SCORE_ZONE_GAINED = 3;
	
	private static final int MALUS_REFRESH_STARTING_ZONE = 5;
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	private String arenaPlanFile;
	
	private List<Player> players;
	private Map<Owners, Player>playersByOwner;
	
	private int firstPlayer;
	private int curPlayer;
	
	private int curTurn;
	private int nbTurnsPerRound;
	
	private int curRound;
	private int nbWinningRoundsPerGame;
	
	private GameStates state;
	private int nbZones;
	
	private WordSelectionHandler wordSelectionHandler;
	
	public GameManager() {		
		wordSelectionHandler = new WordSelectionHandler(this);
		playersByOwner = new HashMap<Owners, Player>();
		
		nbTurnsPerRound = TURNS_PER_ROUND;
		nbWinningRoundsPerGame = WINNING_ROUNDS_PER_GAME;
	}
	
	public Map<Owners, Player> getPlayersByOwner() {
		return playersByOwner;
	}
	
	public int getNbTurnsPerRound() {
		return nbTurnsPerRound;
	}

	public void setNbTurnsPerRound(int nbTurnsPerRound) {
		this.nbTurnsPerRound = nbTurnsPerRound;
	}

	public int getNbWinningRoundsPerGame() {
		return nbWinningRoundsPerGame;
	}

	public void setNbWinningRoundsPerGame(int nbWinningRoundsPerGame) {
		this.nbWinningRoundsPerGame = nbWinningRoundsPerGame;
	}

	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	public void setCurrentPlayer(int playerIndex) {
		curPlayer = playerIndex;
		ui.setCurrentPlayer(getCurrentPlayer(), getCurrentTurn(), nbTurnsPerRound, getCurrentRound());
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
	
	public void prepareGame(ArenaScreen screen, String arenaPlanFile, List<Player> playersList) {
		arena = screen.getArena();
		ui = screen.getUI();
		
		this.arenaPlanFile = arenaPlanFile;
		
		this.players = playersList;
		for (Player player : playersList) {
			playersByOwner.put(player.owner, player);
		}
		
		firstPlayer = 0;
		setCurrentPlayer(0);
		curTurn = 0;
		curRound = 0;
		
		// Charge l'arène
		loadArena();
		
		// Pas de mot et affichage des boutons en conséquence
		setCurrentWord("");
		
		// Démarre le jeu
		changeState(GameStates.READY);
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
			Player player = getCurrentPlayer();
			ui.setInfo(Assets.i18nBundle.format("ui.arena.wordPlayed", player.name, word));
			// Toutes les cellules passent sous la domination du joueur
			arena.setOwner(wordSelectionHandler.getSelectedCells(), player.owner);
			// Le score du joueur est modifié
			player.score += computeScore(wordSelectionHandler.getSelectedCells());
			// Raz du mot sélectionné
			cancelWord();
			// Le joueur a joué un coup. C'est bon à savoir pour les stats
			// et pour autoriser ou non le refresh de la zone de départ
			player.nbWordsPlayed++;
			// Fin du tour de ce joueur
			endStroke();
			break;
		case WORD_ALREADY_PLAYED:
			ui.setInfo(Assets.i18nBundle.format("ui.arena.alreadyPlayed", word));
			break;
		case WORD_UNKNOWN:
			ui.setInfo(Assets.i18nBundle.format("ui.arena.unknownWord", word));
			break;
		}
	}

	/**
	 * Donne le score pour ce mot
	 * @param list
	 * @return
	 */
	private int computeScore(List<ArenaCell> cells) {
		// Mot validé : 1pt * cell.power
		int score = 0;
		for (ArenaCell cell : cells) {
			score += cell.getData().power;
		}
		
		final int wordLength = cells.size();
		// Bonus "Grandiose"  (10+ lettres)
		if (wordLength >= BONUS3_MIN_LENGTH) {
			score += BONUS3_POINTS;
		}
		//Bonus "Sensationnel" (8-9 lettres)
		else if (wordLength >= BONUS2_MIN_LENGTH) {
			score += BONUS2_POINTS;
		}
		// Bonus "Extra" (5-7 lettres)
		else if (wordLength >= BONUS1_MIN_LENGTH) {
			score += BONUS1_POINTS;
		}

		return score;
	}
	
	/**
	 * Appelée par ArenaZone lorsqu'une zone change de propriétaire.
	 * @param oldOwner
	 * @param newOwner
	 */
	public void zoneChangedOwner(Owners oldOwner, Owners newOwner) {
		// Récupère le joueur qui a prit la zone pour lui
		// ajouter des points
		Player overtaker = playersByOwner.get(newOwner);
		if (overtaker != null) {
			// Le joueur a une zone de plus
			overtaker.nbZonesOwned++;
			
			// Ajout des points uniquement pendant la partie, et pas pendant
			// le chargement de l'arène par exemple
			if (state == GameStates.RUNNING
			&& oldOwner != newOwner) {
				// Si la zone appartenait à un adversaire, le joueur
				// gagne plus de points
				if (oldOwner != null
				&& oldOwner != Owners.NEUTRAL) {
					overtaker.score += SCORE_ZONE_STEALED;
				} else {
					overtaker.score += SCORE_ZONE_GAINED;
				}
			}
		}
		
		// Récupère le joueur qui a perdu la zone pour voir
		// s'il a perdu sa dernière zone, et donc le round.
		Player loser = playersByOwner.get(oldOwner);
		if (loser != null) {
			// Ce joueur a perdu une zone
			loser.nbZonesOwned--;
			
			// Si c'était sa dernière zone, il perd le round
			if (loser.nbZonesOwned <= 0) {
				endRound();
			}
		}
		
		// Met à jour l'UI si le jeu tourne. Sinon, on a sûrement
		// été appelé pendant la création de l'arène. On ne met
		// donc pas à jour l'UI.
		if (state == GameStates.RUNNING) {
//			ui.updateStats();
		}
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
		
		// Mise à jour de l'UI
		state = newState;
		ui.present(newState);
		arena.showLetters(newState == GameStates.RUNNING);
		
		if (newState == GameStates.RUNNING) {
			// Re-masque ou ré-affiche les boutons en fonction de l'état de la partie
			setCurrentWord(wordSelectionHandler.getCurrentWord());
		} else if (newState == GameStates.PAUSED) {
			ui.updateStats();
		}
	}
	
	public void loadArena() {
		// Réinitialise les scores
		for (Player player : players) {
			player.score = 0;
			player.nbZonesOwned = 0;
			player.nbWordsPlayed = 0;
		}
		
		// Réinitialise le sélecteur de mots
		wordSelectionHandler.reset();
		
		// Charge l'arène
		arena.buildArena(arenaPlanFile, this);
		arena.showLetters(false);
		nbZones = arena.getData().zones.size();
		
		// Met à jour l'UI
		ui.setArenaName(arena.getData().name);
		ui.setInfo("");
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
		
		// Affiche le prochain joueur à jouer
		setCurrentPlayer(curPlayer);
		
		// Affiche le bouton de rafraîchissement des lettres de la zone de départ.
		// On ne peut rafraîchir la zone de départ qu'au premier coup du round.
		ui.showRefreshStartingZoneButton(getCurrentPlayer().nbWordsPlayed == 0);
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
		
		// Teste si le jeu est fini
		if (endGame(winner)) {
			return;
		}
		
		// Fin de round
		if (winner == null) {
			// Egalité
			ui.setRoundWinner("Egalité ! Personne ne gagne ce round !");
		} else {
			// Victoire
			ui.setRoundWinner(winner.name + " gagne le round !");
		}
		changeState(GameStates.ROUND_OVER);
		
		// Cache les lettres de l'arène
		arena.showLetters(false);
	}
	
	/**
	 * Fin de la partie
	 * @param roundWinner
	 */
	private boolean endGame(Player roundWinner) {
		Player gameWinner = null;
		
		if (roundWinner != null
		&& roundWinner.nbRoundsWon >= nbWinningRoundsPerGame) {
			
			// Fin de partie car le gagnant du round a gagné assez de rounds
			gameWinner = roundWinner;
			
		} else if (curRound >= MAX_NB_ROUNDS_PER_GAME - 1) {
			
			// Fin de partie car on a joué le nombre max de rounds. Il faut
			// donc déterminer le gagnant en comptant le nombre de rounds
			// que chacun a gagné.
			MaxValueFinder<Owners> maxValueFinder = new MaxValueFinder<Owners>();
			for (Player player : players) {
				maxValueFinder.addValue(player.owner, player.nbRoundsWon);
			}
			
			Owners winnerOwner = maxValueFinder.getMaxValue();
			if (winnerOwner != null) {
				// L'un des deux joueurs a gagné plus de rounds que l'autre :
				// il est déclaré vainqueur de la partie
				gameWinner = playersByOwner.get(winnerOwner);
			}
			// else : si winnerOwner == null, c'est qu'on est dans le cas où
			// on a fait 3 rounds et que les 2 joueurs ont gagné autant de
			// rounds l'un que l'autre. On a donc une égalité.
		} else {
			// La partie n'est pas terminée
			return false;
		}

		// Affichage du gagnant
		if (gameWinner == null) {
			ui.setGameWinner("Egalité parfaite ! Personne ne gagne cette partie !");
		} else {
			ui.setGameWinner(roundWinner.name + " gagne la partie !");
		}
		changeState(GameStates.GAME_OVER);
		
		// Cache les lettres de l'arène
		arena.showLetters(false);
		
		// La partie est terminée
		return true;
	}
	
	/**
	 * Passe au round suivant
	 */
	public void nextRound() {
		// On passe au round suivant s'il n'y a pas de vainqueur
		curRound ++;
		// On commence le premier tour de jeu
		curTurn = 0;
		
		// Le joueur qui débute le nouveau round n'est pas le même que celui
		// du round précédent.
		firstPlayer = (firstPlayer + 1) % players.size();
		setCurrentPlayer(firstPlayer);
		
		// Réinitialise l'arène
		loadArena();
		// Démarre le jeu
		changeState(GameStates.RUNNING);
	}
	
	/**
	 * Détermine le gagnant du round
	 * @return
	 */
	private Player computeRoundWinner() {
		ArenaData arenaData = arena.getData();
		MaxValueFinder<Owners> maxValueFinder = new MaxValueFinder<Owners>();
		maxValueFinder.setIgnoredValue(Owners.NEUTRAL);
		
		// Recherche l'owner ayant le plus de zones
		for (ArenaZone zone : arenaData.zones) {
			maxValueFinder.addValue(zone.getOwner());
		}
		Owners winner = maxValueFinder.getMaxValue();
		
		// Si on arrive à ce dernier round, on fait tout pour
		// éviter une égalité : le gagnant est alors celui qui a le plus de zones, ou de
		// cellules, ou de score.
		if (winner == null
		&& curRound == MAX_NB_ROUNDS_PER_GAME - 1) {
			// S'il y a égalité, le gagnant est celui occupant le plus de terrain,
			// en tenant compte de la puissance de chaque cellule
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
			
			// Si on entre ici, c'est qu'on a une égalité parfaite : même nombre de zones
			// et même nombre de cases occupées. On départage les joueurs au score.
			if (winner == null) {
				maxValueFinder.reset();
				for (Player player : players) {
					maxValueFinder.addValue(player.owner, player.score);
				}
				winner = maxValueFinder.getMaxValue();
			}
		}
		
		// Toujours pas de gagnant ? On a une égalité parfaite (très improbable)
		if (winner == null) {
			return null;
		}
		
		// Récupère le joueur gagnant et lui ajoute 1 round
		Player player = playersByOwner.get(winner);
		player.nbRoundsWon++;
		return player;
	}

	public int getNbZones() {
		return nbZones;
	}

	public void refreshStartingZone() {
		// Change les lettres de la zone de départ
		arena.refreshStartingZone(getCurrentPlayer().owner);
		// Supprime le mot éventuellement sélectionné
		wordSelectionHandler.cancel();
		// Affiche un message de confirmation
		ui.setInfo(Assets.i18nBundle.format("ui.arena.redrewLetters", getCurrentPlayer().name));
		// Le score du joueur est modifié
		getCurrentPlayer().score -= MALUS_REFRESH_STARTING_ZONE;
		// Fin du tour de ce joueur
		endStroke();
	}
	
	/**
	 * Met à jour l'affichage du mot actuellement sélectionné
	 */
	public void setCurrentWord(String word) {
		boolean emptyWord = word.isEmpty();
		
		ui.setCurrentWord(word);
		ui.showWordValidationButtons(!emptyWord);
		ui.showRefreshStartingZoneButton(getCurrentPlayer().nbWordsPlayed == 0 && emptyWord);
	}

	public void requestBack() {
		switch (state) {
		case RUNNING:
			changeState(GameStates.PAUSED);
			break;
		case PAUSED:
			changeState(GameStates.RUNNING);
			break;
		case READY:
		case GAME_OVER:
		case ROUND_OVER:
			// On ne fait rien dans ces cas-là : l'utilisateur doit choisir une action
			break;
		}
	}
}
