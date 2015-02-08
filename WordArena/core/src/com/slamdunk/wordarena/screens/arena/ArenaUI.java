package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.enums.GameStates;

public class ArenaUI extends UIOverlay {
	private List<Group> componentsGroups;
	private ArenaScreen screen;
	
	public ArenaUI() {
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Par défaut, la skin par défaut sera utilisée
		final Skin skin = new Skin(Gdx.files.internal(UIOverlay.DEFAULT_SKIN));
		setSkin(skin);

		// Création des groupes de composants suivant l'état du jeu
		componentsGroups = new ArrayList<Group>();
		createReadyGroup(skin);
		createRunningGroup(skin);
		createPausedGroup(skin);
		createLevelEndGroup(skin);
		createGameOverGroup(skin);
		
		final Stage stage = getStage();
		for (Group group : componentsGroups) {
			group.setVisible(false);
			stage.addActor(group);
		}
	}
	
	@Override
	public void setScreen(SlamScreen screen) {
		super.setScreen(screen);
		this.screen = ((ArenaScreen)getScreen());
	}

	/**
	 * Charge les composants à afficher lorsque le jeu est à l'état indiqué
	 */
	public void present(GameStates state) {
		for (Group group : componentsGroups) {
			group.setVisible(state.equals(group.getUserObject()));
		}
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_READY"
	 */
	public void createReadyGroup(Skin skin) {
		Group group = new Group();
		group.setUserObject(GameStates.READY);
		
		TextButton play = new TextButton("PLAY", skin);
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.changeState(GameStates.RUNNING);
			}
		});
		group.addActor(play);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_RUNNING"
	 */
	public void createRunningGroup(Skin skin) {
		Group group = new Group();
		group.setUserObject(GameStates.RUNNING);
		
		TextButton pause = new TextButton("PAUSE", skin);
		pause.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
				screen.changeState(GameStates.PAUSED);
			}
		});
		group.addActor(pause);
		
		TextButton validate = new TextButton("VALIDER", skin);
		validate.setPosition(WordArenaGame.SCREEN_WIDTH - validate.getWidth(), 0);
		validate.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.validateWord();
			}
		});
		group.addActor(validate);
		
		TextButton cancel = new TextButton("ANNULER", skin);
		cancel.setPosition(validate.getX() - 10 - cancel.getWidth(), 0);
		cancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.cancelWord();
			}
		});
		group.addActor(cancel);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_PAUSED"
	 */
	public void createPausedGroup(Skin skin) {
		Group group = new Group();
		group.setUserObject(GameStates.PAUSED);
		
		TextButton resume = new TextButton("RESUME", skin);
		resume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				Assets.playSound(Assets.clickSound);
				screen.changeState(GameStates.RUNNING);
			}
		});
		group.addActor(resume);
		
		TextButton quit = new TextButton("QUIT", skin);
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
	public void createLevelEndGroup(Skin skin) {
		Group group = new Group();
		group.setUserObject(GameStates.LEVEL_END);
		
		TextButton next = new TextButton("NEXT", skin);
		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.loadNextLevel();
				screen.changeState(GameStates.READY);
			}
		});
		group.addActor(next);
		
		componentsGroups.add(group);
	}

	/**
	 * Crée les composants à afficher lorsque le jeu est à l'état "GAME_OVER"
	 */
	public void createGameOverGroup(Skin skin) {
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
}