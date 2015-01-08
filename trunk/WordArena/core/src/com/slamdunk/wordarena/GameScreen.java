package com.slamdunk.wordarena;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.slamdunk.wordarena.systems.BoundsSystem;
import com.slamdunk.wordarena.systems.RenderingSystem;

public class GameScreen extends ScreenAdapter {
	private Engine engine;
	
	private GameStates state;
	
	private Arena arena;
	private GameUI ui;

	public GameScreen (WordArenaGame game) {
		engine = new Engine();
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new RenderingSystem(game.batcher));
		
		input
		
		ui = new GameUI(this);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(this, ui.getStage()));
		
		loadNextLevel();
		
		changeState(GameStates.READY);
	}

	public void loadNextLevel() {
		engine.removeAllEntities();
		
		int lastScore = arena != null ? arena.score : 0;
		arena = new Arena(engine);
		arena.score = lastScore;
	}

	public void update (float deltaTime) {
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		engine.update(deltaTime);
		
		if (state == GameStates.RUNNING) {
			if (arena.state == Arena.ARENA_STATE_NEXT_LEVEL) {
				System.out.println("ARENA_STATE_NEXT_LEVEL");
				// TODO
//				game.setScreen(new WinScreen(game));
			}
			if (arena.state == Arena.ARENA_STATE_GAME_OVER) {
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
	

	private void pauseSystems() {
		engine.getSystem(BoundsSystem.class).setProcessing(false);
	}
	
	private void resumeSystems() {
		engine.getSystem(BoundsSystem.class).setProcessing(true);
	}
}