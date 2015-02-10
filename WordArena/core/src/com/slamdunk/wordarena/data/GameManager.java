package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.enums.ReturnCodes;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.utils.MaxValueFinder;

/**
 * Gère la partie
 */
public class GameManager {
	private static final int TURNS_PER_ROUND = 5;
	private static final int WINNING_ROUNDS_PER_GAME = 2;
	
	private static final int BONUS1_MIN_LENGTH = 5;
	private static final int BONUS1_POINTS = 2;
	private static final int BONUS2_MIN_LENGTH = 8;
	private static final int BONUS2_POINTS = 3;
	private static final int BONUS3_MIN_LENGTH = 10;
	private static final int BONUS3_POINTS = 5;
	
	private static final int SCORE_ZONE_STEALED = 5;
	private static final int SCORE_ZONE_GAINED = 3;
	
	private ArenaScreen screen;
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
	
	public GameManager(ArenaScreen screen) {
		this.screen = screen;
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
	
	public void prepareGame(String arenaPlanFile, List<Player> playersList) {
		this.arenaPlanFile = arenaPlanFile;
		
		this.players = playersList;
		for (Player player : playersList) {
			playersByOwner.put(player.owner, player);
		}
		
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
			// Le score du joueur est modifié
			players.get(curPlayer).score += computeScore(wordSelectionHandler.getSelectedCells());
			screen.getUI().updateStats();
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
				System.err.println(loser.name + " perd le round car il n'a plus aucune zone !");
			}
		}
		
		// Met à jour l'UI si le jeu tourne. Sinon, on a sûrement
		// été appelé pendant la création de l'arène. On ne met
		// donc pas à jour l'UI.
		if (state == GameStates.RUNNING) {
			screen.getUI().updateStats();
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
		// Réinitialise les scores
		for (Player player : players) {
			player.score = 0;
			player.nbZonesOwned = 0;
		}
		
		// Réinitialise le sélecteur de mots
		wordSelectionHandler.reset();
		
		// Charge l'arène
		screen.getArena().buildArena(arenaPlanFile, this);
		screen.getArena().setVisible(false);
		nbZones = screen.getArena().getData().zones.size();
		
		// Met à jour l'UI
		screen.getUI().updateStats();
		 
		// Démarre le jeu
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
		MaxValueFinder<Owners> maxValueFinder = new MaxValueFinder<Owners>();
		maxValueFinder.setIgnoredValue(Owners.NEUTRAL);
		
		// Recherche l'owner ayant le plus de zones
		for (ArenaZone zone : arenaData.zones) {
			maxValueFinder.addValue(zone.getOwner());
		}
		Owners winner = maxValueFinder.getMaxValue();
		
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
}
