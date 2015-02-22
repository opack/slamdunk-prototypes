package com.slamdunk.wordarena.screens.arena;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.GameManager;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.screens.home.HomeScreen;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;

public class ArenaUI extends UIOverlay {
	private SceneLoader sceneLoader;
	
	private GameManager gameManager;
	private Label currentPlayer;
	private Label stats;
	
	public ArenaUI(GameManager gameManager) {
		this.gameManager = gameManager;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		loadScene();
	}
	

	/**
	 * Charge les composants définis dans Overlap2D
	 */
	private void loadScene() {
		sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Arena");
		getStage().addActor(sceneLoader.sceneActor);
		
		sceneLoader.sceneActor.setTouchable(Touchable.childrenOnly);

		initReadyLayer();
		initRunningLayer();
		initPausedLayer();
		initRoundOverLayer();
		initGameOverLayer();
	}

	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "READY"
	 */
	private void initReadyLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnStart", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.RUNNING);
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "RUNNING"
	 */
	private void initRunningLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnValidateWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.validateWord();
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCancelWord", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.cancelWord();
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnRefreshZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.refreshStartingZone();
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPause", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.PAUSED);
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCenterCamera", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				((ArenaScreen)getScreen()).centerCamera();
			}
		});
		
		currentPlayer = sceneLoader.sceneActor.getLabelById("lblCurrentPlayer");
		stats = sceneLoader.sceneActor.getLabelById("lblStats");
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "PAUSED"
	 */
	private void initPausedLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnResume", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.RUNNING);
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "ROUND_OVER"
	 */
	private void initRoundOverLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnNextRound", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.nextRound();
			}
		});
	}
	
	/**
	 * Initialise les composants à afficher lorsque le jeu est à l'état "GAME_OVER"
	 */
	private void initGameOverLayer() {
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnRetry", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
			}
		});
		
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnBackToHome", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				getScreen().getGame().setScreen(HomeScreen.NAME);
			}
		});
	}

	/**
	 * Charge les composants à afficher lorsque le jeu est à l'état indiqué
	 */
	public void present(GameStates state) {
		for (GameStates cur : GameStates.values()) {
			sceneLoader.sceneActor.setLayerVisibilty(cur.name(), cur == state);
			// Il semble y avoir un bug dans Overlap2D : si l'export est fait alors qu'une couche
			// est masquée, cette couche arrivera verrouillée et même si on l'affiche elle ne sera
			// pas réceptive aux touch. On s'assure donc ici que la couche affichée est également
			// déverrouillée.
			sceneLoader.sceneActor.setLayerLock(cur.name(), cur != state);
		}
	}
	
	public void setCurrentPlayer(Player player, int turn, int maxTurns, int round) {
		currentPlayer.setText("Round " + round + " - " + player.name + " (Coup " + turn + "/" + maxTurns + ")");
		currentPlayer.setStyle(Assets.ownerStyles.get(player.owner));
	}
	
	public void setCurrentWord(String word) {
		sceneLoader.sceneActor.getLabelById("lblCurrentWord").setText(word);
	}
	
	public void setInfo(String info) {
		sceneLoader.sceneActor.getLabelById("lblInfo").setText(info);
	}

	public void setArenaName(String arenaName) {
		sceneLoader.sceneActor.getLabelById("lblArenaName").setText(arenaName);
	}
	
	public void setRoundWinner(String winner) {
		sceneLoader.sceneActor.getLabelById("lblRoundWinner").setText(winner);
	}
	
	public void setGameWinner(String winner) {
		sceneLoader.sceneActor.getLabelById("lblGameWinner").setText(winner);
	}

	public void updateStats() {
		StringBuilder sb = new StringBuilder();
		Player player;
		for (Owners owner : Owners.values()) {
			player = gameManager.getPlayersByOwner().get(owner);
			if (player != null) {
				sb.append("== ").append(player.name).append(" ==");
				sb.append("\n\tScore : ").append(player.score);
				sb.append("\n\tZones : ").append(player.nbZonesOwned).append("/").append(gameManager.getNbZones());
				sb.append("\n\tRounds : ").append(player.nbRoundsWon).append("/").append(gameManager.getNbWinningRoundsPerGame());
				sb.append("\n");
			}
		}
		
		stats.setText(sb.toString());
	}
}
