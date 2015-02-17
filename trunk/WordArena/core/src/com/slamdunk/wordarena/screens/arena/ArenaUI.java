package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.GameManager;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;
import com.slamdunk.wordarena.enums.Owners;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

public class ArenaUI extends UIOverlay {
	private SceneLoader sceneLoader;
	
	private List<Group> componentsGroups;
	private GameManager gameManager;
	private Label currentPlayer;
	private Label result;
	private Label stats;
	
	public ArenaUI(GameManager gameManager) {
		this.gameManager = gameManager;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Par défaut, la skin par défaut sera utilisée
		setSkin(Assets.skin);

		// Création des groupes de composants suivant l'état du jeu
		componentsGroups = new ArrayList<Group>();
		createReadyGroup();
		createRunningGroup();
		createPausedGroup();
		createLevelEndGroup();
		createGameOverGroup();
		
		final Stage stage = getStage();
		for (Group group : componentsGroups) {
			group.setVisible(false);
			stage.addActor(group);
		}
		
		loadScene();
	}
	

	/**
	 * Charge les composants définis dans Overlap2D
	 */
	private void loadScene() {
		sceneLoader = new SceneLoader(Assets.resourceManager);
		sceneLoader.loadScene("Arena");
		getStage().addActor(sceneLoader.sceneActor);
		
		sceneLoader.sceneActor.setTouchable(Touchable.childrenOnly);
		
		SimpleButtonScript validateScript = new SimpleButtonScript();
		validateScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.validateWord();
			}
		});
		sceneLoader.sceneActor.getCompositeById("validateWord").addScript(validateScript);
		
		SimpleButtonScript cancelScript = new SimpleButtonScript();
		cancelScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.cancelWord();
			}
		});
		sceneLoader.sceneActor.getCompositeById("cancelWord").addScript(cancelScript);
		
		SimpleButtonScript refreshZoneScript = new SimpleButtonScript();
		refreshZoneScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameManager.refreshStartingZone();
			}
		});
		sceneLoader.sceneActor.getCompositeById("refreshZone").addScript(refreshZoneScript);
		
		SimpleButtonScript resumeScript = new SimpleButtonScript();
		resumeScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.RUNNING);
			}
		});
		sceneLoader.sceneActor.getCompositeById("resume").addScript(resumeScript);
		
		currentPlayer = sceneLoader.sceneActor.getLabelById("currentPlayer");
		result = sceneLoader.sceneActor.getLabelById("currentWord");
	}

	/**
	 * Charge les composants à afficher lorsque le jeu est à l'état indiqué
	 */
	public void present(GameStates state) {
		// DBG Mettre ce comportement dans un iScript qui check si le state a changé, et le cas échéant affiche la bonne couche
		for (GameStates cur : GameStates.values()) {
			sceneLoader.sceneActor.setLayerVisibilty(cur.name(), cur == state);
		}
		for (Group group : componentsGroups) {
			group.setVisible(state.equals(group.getUserObject()));
		}
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_READY"
	 */
	public void createReadyGroup() {
		Group group = new Group();
		group.setUserObject(GameStates.READY);
		
		TextButton play = new TextButton("PLAY", getSkin());
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameManager.changeState(GameStates.RUNNING);
			}
		});
		group.addActor(play);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_RUNNING"
	 */
	public void createRunningGroup() {
		final Skin skin = getSkin();
		Group group = new Group();
		group.setUserObject(GameStates.RUNNING);
		
//		// Libellé indiquant le joueur courant
//		currentPlayer = new Label("", skin);
//		currentPlayer.setAlignment(Align.center);
//		currentPlayer.setWidth(200);
//		currentPlayer.setPosition(300, 465);
//		group.addActor(currentPlayer);
		
//		// Libellé servant de résultat d'action
//		result = new Label("", skin);
//		result.setAlignment(Align.center);
//		result.setWidth(200);
//		result.setPosition(300, 440);
//		group.addActor(result);
		
		// Libellé donnant les scores
		stats = new Label("", skin);
		stats.setPosition(680, 300);
		group.addActor(stats);
		
		TextButton center = new TextButton("CENTRER CAMERA", skin);
		center.setPosition(0, 0);
		center.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				((ArenaScreen)getScreen()).centerCamera();
			}
		});
		group.addActor(center);
		
		TextButton pause = new TextButton("PAUSE", skin);
		pause.setPosition(WordArenaGame.SCREEN_WIDTH - pause.getWidth(), WordArenaGame.SCREEN_HEIGHT - pause.getHeight());
		pause.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.PAUSED);
			}
		});
		group.addActor(pause);
		
		TextButton validate = new TextButton("VALIDER", skin);
		validate.setPosition(WordArenaGame.SCREEN_WIDTH - validate.getWidth(), 0);
		validate.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameManager.validateWord();
			}
		});
		group.addActor(validate);
		
		TextButton cancel = new TextButton("ANNULER", skin);
		cancel.setPosition(validate.getX() - 10 - cancel.getWidth(), 0);
		cancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameManager.cancelWord();
			}
		});
		group.addActor(cancel);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_PAUSED"
	 */
	public void createPausedGroup() {
		final Skin skin = getSkin();
		Group group = new Group();
		group.setUserObject(GameStates.PAUSED);
		
		TextButton resume = new TextButton("REPRENDRE", skin);
		resume.setPosition(WordArenaGame.SCREEN_WIDTH - resume.getWidth(), WordArenaGame.SCREEN_HEIGHT - resume.getHeight());
		resume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
				gameManager.changeState(GameStates.RUNNING);
			}
		});
		group.addActor(resume);
		
		TextButton quit = new TextButton("QUITTER", skin);
		quit.setPosition(0, WordArenaGame.SCREEN_HEIGHT - quit.getHeight());
		quit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
//				game.setScreen(new MainMenuScreen(game));
			}
		});
		group.addActor(quit);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_LEVEL_END"
	 */
	public void createLevelEndGroup() {
		Group group = new Group();
		group.setUserObject(GameStates.LEVEL_END);
		
		TextButton next = new TextButton("NEXT", getSkin());
		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameManager.loadArena();
				gameManager.changeState(GameStates.READY);
			}
		});
		group.addActor(next);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_OVER"
	 */
	public void createGameOverGroup() {
		final Skin skin = getSkin();
		Group group = new Group();
		group.setUserObject(GameStates.OVER);
		
		group.addActor(new TextButton("RETRY", skin));
		TextButton backToMain = new TextButton("MAIN", skin);
		backToMain.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
//				game.setScreen(new MainMenuScreen(game));
			}
		});
		group.addActor(backToMain);
		
		componentsGroups.add(group);
	}
	
	public void setCurrentPlayer(Player player, int turn, int maxTurns, int round) {
		currentPlayer.setText("Round " + round + " - " + player.name + " (Coup " + turn + "/" + maxTurns + ")");
		currentPlayer.setStyle(Assets.ownerStyles.get(player.owner));
	}
	
	public void updateResult(String text) {
		result.setText(text);
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
