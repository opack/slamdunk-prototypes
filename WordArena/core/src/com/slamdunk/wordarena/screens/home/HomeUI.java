package com.slamdunk.wordarena.screens.home;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.UserManager;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.GameData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameTypes;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;

public class HomeUI extends UIOverlay {
	private static final String GAME_LABEL_STYLE_OPPONENT_TURN = "opponent-turn";
	private static final String GAME_LABEL_STYLE_USER_TURN = "user-turn";
	
	private HomeScreen screen;
	private Table gamesTable;
	private List<GameData> games;
	
	public HomeUI(HomeScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les parties en cours pour chaque type
		loadDBGGames();
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
		
		// Charge la liste des parties en cours
		initCurrentGames();
	}

	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Home");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Play !
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPlay", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.launchGame("3_diamond");
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
				System.out.println("DBG Quit");
			}
		});
		
		// Boutons de lancemant des arènes
		sceneLoader.sceneActor.getCompositeById("btnArena0").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena1").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena2").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena3").addScript(new GameLaunchScript(screen));
		
		// Boutons d'affichage des parties en cours
		sceneLoader.sceneActor.getCompositeById("btnGamesCareer").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesTrainings").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesDuels").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesLeague").addScript(new ChangeCurrentGamesScript(this));
		sceneLoader.sceneActor.getCompositeById("btnGamesTournaments").addScript(new ChangeCurrentGamesScript(this));
	}
	
	private void initCurrentGames() {
		// Créer une table
		gamesTable = new Table();
		
		// Charge les parties en cours
		loadCurrentGames(GameTypes.CAREER);
		
		// Placer la table dans un ScrollPane pour permettre le scroll
		ScrollPane scrollPane = new ScrollPane(gamesTable, Assets.skin);
		scrollPane.setupOverscroll(15, 30, 200);
		
		// Ajouter le ScrollPane au Stage
		getStage().addActor(scrollPane);
	}
	
	/**
	 * Charge et affiche les parties en cours du type spécifié
	 * @param gameType
	 */
	public void loadCurrentGames(GameTypes gameType) {
		// Vider la table et afficher la bonne ligne d'entête
		initGamesTable(gameType);
		
		// Ajouter ces parties à la table (1 partie par ligne, 1 ligne d'en-tête par type de partie)
		String username = UserManager.getInstance().getUserData().name;
		for (GameData gameData : games) {
			if (gameData.gameType == gameType) {
				createGameRow(gameData, username);
			}
		}
		
		// Remplit le reste avec du vide
		gamesTable.add().expand();
	}

	private void loadDBGGames() {
		games = new ArrayList<GameData>();
		
		// TODO DBG Triche en attendant le chargement de vraies parties
		Player p1 = new Player();
		p1.name = "Alan";
		p1.owner = Owners.PLAYER1;
		
		Player p2 = new Player();
		p2.name = "Bob";
		p2.owner = Owners.PLAYER2;
		
		Player p3 = new Player();
		p3.name = "Charles";
		p3.owner = Owners.PLAYER3;
		
		Player p4 = new Player();
		p4.name = "Dave";
		p4.owner = Owners.PLAYER4;
		
		GameData game1 = new GameData();
		game1.gameType = GameTypes.DUEL;
		game1.players = new Player[]{p1, p2};
		game1.currentPlayer = 1;
		games.add(game1);
		
		GameData game2 = new GameData();
		game2.gameType = GameTypes.DUEL;
		game2.players = new Player[]{p1, p3};
		game2.currentPlayer = 0;
		games.add(game2);
		
		GameData game3 = new GameData();
		game3.gameType = GameTypes.DUEL;
		game3.players = new Player[]{p1, p4};
		game3.currentPlayer = 0;
		games.add(game3);
		
		GameData game4 = new GameData();
		game4.gameType = GameTypes.TOURNAMENT;
		game4.players = new Player[]{p1, p2, p3};
		game4.currentPlayer = 0;
		games.add(game4);
		
		games.add(game1);
		games.add(game1);
		games.add(game1);
		games.add(game1);
		games.add(game1);
		games.add(game1);
		games.add(game1);
	}
	
	/**
	 * Vide la table des parties en cours et la prépare pour l'affichage
	 * des parties du type indiqué
	 * @param gameType
	 */
	private void initGamesTable(GameTypes gameType) {
		gamesTable.clear();
		gamesTable.add(new Label(gameType.name(), Assets.skin)).colspan(1);
		gamesTable.row();
	}

	/**
	 * Crée une ligne dans la table des parties en cours
	 * @param gameData
	 * @return
	 */
	private void createGameRow(GameData gameData, String username) {
		// Choix du style du label en fonction du joueur courant
		String labelStyle = GAME_LABEL_STYLE_OPPONENT_TURN;
		Player currentPlayer = gameData.players[gameData.currentPlayer];
		if (username.equals(currentPlayer.name)) {
			labelStyle = GAME_LABEL_STYLE_USER_TURN;
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
