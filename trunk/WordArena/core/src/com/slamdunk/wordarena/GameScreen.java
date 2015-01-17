package com.slamdunk.wordarena;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.slamdunk.wordarena.systems.BoundsSystem;
import com.slamdunk.wordarena.systems.ColliderSystem;
import com.slamdunk.wordarena.systems.RenderingSystem;
import com.slamdunk.wordarena.systems.WordSelectionHandler;

public class GameScreen extends ScreenAdapter {
	private Engine engine;
	
	private GameStates state;
	
	private Arena arena;
	private GameUI ui;
	
	private WordSelectionHandler wordSelectionHandler;

	public GameScreen (WordArenaGame game) {
		RenderingSystem renderingSystem = new RenderingSystem(game.batcher);
		engine = new Engine();
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new ColliderSystem());
		engine.addSystem(renderingSystem);
				
		ui = new GameUI(this);

		wordSelectionHandler = new WordSelectionHandler(renderingSystem.getCamera());
		Gdx.input.setInputProcessor(new InputMultiplexer(ui.getStage(), wordSelectionHandler));
		
		loadNextLevel();
		
		changeState(GameStates.READY);
	}

	public void loadNextLevel() {
		engine.removeAllEntities();
		
		int lastScore = arena != null ? arena.score : 0;
		arena = new Arena(engine, 25, 15);
		arena.score = lastScore;
		
		wordSelectionHandler.setArena(arena);
	}

	public void update (float deltaTime) {
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		engine.update(deltaTime);
		
		if (state == GameStates.RUNNING) {
			if (arena.state == GameStates.LEVEL_END) {
				System.out.println("ARENA_STATE_NEXT_LEVEL");
				// TODO
//				game.setScreen(new WinScreen(game));
			}
			if (arena.state == GameStates.OVER) {
				changeState(GameStates.OVER);
			}
		}
	}

	/**
	 * Change l'état actuel du jeu et met à jour l'IHM
	 * @param newState
	 */
	public void changeState(GameStates newState) {
		state = newState;
		ui.present(newState);
		
		// Démarre ou stoppe les systèmes en fonction de l'état
		switch (newState) {
		case READY:
		case PAUSED:
		case OVER:
			pauseSystems();
			break;
		case RUNNING:
			resumeSystems();
			break;
		case LEVEL_END:
			break;
		}
	}

	@Override
	public void render (float delta) {
		update(delta);
		ui.update(delta);
	}

	@Override
	public void pause () {
		if (state == GameStates.RUNNING) {
			changeState(GameStates.PAUSED);
		}
	}
	
	/**
	 * Met tous les systèmes en pause sauf le RenderingSystem
	 */
	private void pauseSystems() {
		for (EntitySystem system : engine.getSystems()) {
			system.setProcessing(false);
		}
		engine.getSystem(RenderingSystem.class).setProcessing(true);
	}
	
	/**
	 * Démarre tous les systèmes
	 */
	private void resumeSystems() {
		for (EntitySystem system : engine.getSystems()) {
			system.setProcessing(true);
		}
	}

	/**
	 * Vérifie si le mot est valide, ajoute des points au score
	 * le cas échéant et choisit d'autres lettres sur le mot
	 * sélectionné.
	 */
	public void validateWord() {
		List<Entity> selectedLetters = wordSelectionHandler.getSelectedEntities();
		if (!selectedLetters.isEmpty()) {
			arena.validateWord(selectedLetters);
		}
		
		cancelWord();
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.resetSelection();
	}
}