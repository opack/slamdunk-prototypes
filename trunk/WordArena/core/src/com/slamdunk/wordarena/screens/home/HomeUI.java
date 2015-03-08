package com.slamdunk.wordarena.screens.home;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.DoubleEntryArrayList;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.UserManager;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.GameData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStatus;
import com.slamdunk.wordarena.enums.GameTypes;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;

public class HomeUI extends UIOverlay {
	private static final String GAME_LABEL_STYLE_STATUS_HEADER = "games-status-header";
	private static final String GAME_LABEL_STYLE_OPPONENT_TURN = "games-opponent-turn";
	private static final String GAME_LABEL_STYLE_USER_TURN = "games-user-turn";
	private static final String GAME_LABEL_STYLE_GAME_OVER = "games-game-over";
	
	private HomeScreen screen;
	private Table gamesTable;
	/**
	 * Table associant une liste de parties à un statut et un type de partie
	 */
	private DoubleEntryArrayList<GameStatus, GameTypes, GameData> games;
	private String username;
	
	public HomeUI(HomeScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
		
		// Charge la liste des parties en cours
		createCurrentGamesTable();
		
		// Charge les parties en cours
		loadCurrentGames(GameTypes.CAREER);
	}

	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Home");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Editor
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnEditor", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.launchEditor();
			}
		});
		
		// Bouton Options
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnOptions", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Options");
			}
		});
		
		// Bouton Quit
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnQuit", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.promptExit();
			}
		});
		
		// Boutons de lancemant des arènes
		sceneLoader.sceneActor.getCompositeById("btnArena0").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena1").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena2").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena3").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena4").addScript(new GameLaunchScript(screen));
		
		// Boutons d'affichage des parties en cours
		sceneLoader.sceneActor.getCompositeById("btnGamesCareer").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesTrainings").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesDuels").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesLeague").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesTournaments").addScript(new ChangeCurrentGamesScript(this));
	}
	
	private void createCurrentGamesTable() {
		// Créer une table
		gamesTable = new Table();
		
		// Placer la table dans un ScrollPane pour permettre le scroll
		ScrollPane scrollPane = new ScrollPane(gamesTable, Assets.skin);
		scrollPane.setupOverscroll(15, 30, 200);
		scrollPane.setPosition(5, 5);
		scrollPane.setSize(470, 595);
		
		// Ajouter le ScrollPane au Stage
		getStage().addActor(scrollPane);
	}
	
	/**
	 * Charge et affiche les parties en cours du type spécifié
	 * @param gameType
	 */
	public void loadCurrentGames(GameTypes gameType) {
		// Vider la table et afficher la bonne ligne d'entête
		gamesTable.clear();

		username = UserManager.getInstance().getUserData().name;
		
		// Charge les parties en cours pour chaque type
		fetchGames();
		
		// Ajouter ces parties à la table
		List<GameData> gameForStatus;
		for (GameStatus status : GameStatus.values()) {
			gameForStatus = games.get(status, gameType);
			if (gameForStatus != null) {
				// Ajoute une ligne d'entête
				createHeaderRow(status);
				
				// Ajoute les parties en cours
				for (GameData gameData : gameForStatus) {
					createGameRow(gameData, status);
				}
			}
		}
		
		// Remplit le reste avec du vide
		gamesTable.add().expand();
	}

	private void fetchGames() {
		// TODO DBG DEB Triche en attendant le chargement de vraies parties
		List<GameData> fetched = new ArrayList<GameData>();
		Player p1 = new Player();
		p1.uid = 1;
		p1.name = "Alan";
		p1.cellPack = "blue";
		
		Player p2 = new Player();
		p2.uid = 2;
		p2.name = "Bob";
		p2.cellPack = "orange";
		
		Player p3 = new Player();
		p3.uid = 3;
		p3.name = "Charles";
		p3.cellPack = "green";
		
		Player p4 = new Player();
		p4.uid = 4;
		p4.name = "Dave";
		p4.cellPack = "purple";
		
		GameData game1 = new GameData();
		game1.gameType = GameTypes.DUEL;
		game1.players = new Player[]{p1, p2};
		game1.currentPlayer = 1;
		fetched.add(game1);
		
		GameData game2 = new GameData();
		game2.gameType = GameTypes.DUEL;
		game2.players = new Player[]{p1, p3};
		game2.currentPlayer = 0;
		fetched.add(game2);
		
		GameData game3 = new GameData();
		game3.gameType = GameTypes.DUEL;
		game3.players = new Player[]{p1, p4};
		game3.currentPlayer = 0;
		fetched.add(game3);
		
		GameData game4 = new GameData();
		game4.gameType = GameTypes.TOURNAMENT;
		game4.players = new Player[]{p1, p2, p3};
		game4.currentPlayer = 0;
		fetched.add(game4);
		
		GameData game5 = new GameData();
		game5.gameType = GameTypes.DUEL;
		game5.players = new Player[]{p1, p2};
		game5.currentPlayer = 0;
		game5.gameOver = true;
		fetched.add(game5);
		
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		fetched.add(game5);
		// FIN DBG
		
		// Répartit les jeux entre ceux où c'est au tour de l'utilisateur de jouer
		// et ceux où c'est à un adversaire de jouer
		games = new DoubleEntryArrayList<GameStatus, GameTypes, GameData>();
		
		Player currentPlayer;
		GameStatus status;
		for (GameData gameData : fetched) {
			// Détermine le statut de la partie
			if (gameData.gameOver) {
				status = GameStatus.GAME_OVER;
			} else {
				currentPlayer = gameData.players[gameData.currentPlayer];
				if (username.equals(currentPlayer.name)) {
					status = GameStatus.USER_TURN;
				} else {
					status = GameStatus.OPPONENT_TURN;
				}
			}
			
			// Ajoute la partie à la liste
			games.add(status, gameData.gameType, gameData);
		}
	}
	
	/**
	 * Crée une ligne d'entête précisant l'état des parties
	 * qui seront affichées à la suite
	 * @param status
	 */
	private void createHeaderRow(GameStatus status) {
		final String header = Assets.i18nBundle.get("ui.home.games.header." + status.name());
		gamesTable.add(new Label(header, Assets.skin, GAME_LABEL_STYLE_STATUS_HEADER));
		
		// Fin de la ligne
		gamesTable.row();
	}

	/**
	 * Crée une ligne dans la table des parties en cours
	 * @param gameData
	 * @param userTurn true si c'est à l'utilisateur de jouer
	 * @return
	 */
	private void createGameRow(GameData gameData, GameStatus status) {
		// Choix du style du label en fonction du joueur courant
		String labelStyle;
		switch (status) {
		case USER_TURN:
			labelStyle = GAME_LABEL_STYLE_USER_TURN;
			break;
		case OPPONENT_TURN:
			labelStyle = GAME_LABEL_STYLE_OPPONENT_TURN;
			break;
		case GAME_OVER:
		default:
			labelStyle = GAME_LABEL_STYLE_GAME_OVER;
			break;
		}
		
		// Ajout d'un label avec la liste des adversaires
		StringBuilder opponents = new StringBuilder();
		for (Player opponent : gameData.players) {
			if (opponents.length() != 0) {
				opponents.append(", ");
			}
			if (!username.equals(opponent.name)) {
				opponents.append(opponent.name);
			}
		}
		gamesTable.add(new Label(opponents.toString(), Assets.skin, labelStyle));
		
		// Fin de la ligne
		gamesTable.row();
	}
}
