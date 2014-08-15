package com.slamdunk.wordarena.old;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.wordarena.old.letters.LetterGenerator;
import com.slamdunk.wordarena.old.letters.Letters;

public class WordArenaGame implements ApplicationListener {
	private static final int ARENA_WIDTH = 9;
	private static final int ARENA_HEIGHT = 9;
	
	private Stage stage;
	private Skin skin;
	
	private LetterCell[][] cells;
	private Table arena;
	
	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("skins/default-skin/default-skin.json"));
		cells = new LetterCell[ARENA_WIDTH][ARENA_HEIGHT];
		
		stage = new Stage();
		arena = new Table();
		stage.getRoot().addActor(arena);
		
		initArena();
		initInput();
	}
	
	private void initArena() {
		List<Letters> letters = LetterGenerator.generate(ARENA_WIDTH * ARENA_HEIGHT);
		
		for (int col = 0; col < ARENA_WIDTH; col++) {
			for (int row = 0; row < ARENA_HEIGHT; row++) {
				LetterCell cell = new LetterCell();
				cell.letter = letters.remove(0);
				cell.button = new TextButton(cell.letter.toString(), skin, "grid-number-1L");
				
				cells[col][row] = cell;
				arena.add(cell.button).size(50);
			}
			arena.row();
		}
		
		arena.pack();
	}

	private void initInput() {
		InputProcessor proc = new InputAdapter() {
			private Vector2 screenCoords = new Vector2();
			private Vector2 stageCoords;
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// Calcul des coordonnées pour le stage
				screenCoords.set(screenX, screenY);
				stageCoords = stage.screenToStageCoordinates(screenCoords);
				
				// Récupération de l'éventuel bouton
				Actor hit = stage.hit(stageCoords.x, stageCoords.y, true);
				if (hit != null && (hit.getParent() instanceof TextButton)) {
					// Récupère la cellule associée et sélectionne la lettre
					LetterCell cell = getCell((TextButton)hit.getParent());
					if (cell != null) {
						select(cell, true);
						return true;
					}
				}
				return false;
			}
		};
		Gdx.input.setInputProcessor(new InputMultiplexer(proc, stage));
	}

	public LetterCell getCell(Button button) {
		if (button != null && cells != null) {
			for (LetterCell[] line : cells) {
				for (LetterCell cell : line) {
					if (button.equals(cell.button)) {
						return cell;
					}
				}
			}
		}
		return null;
	}

	private void select(LetterCell cell, boolean swiping) {
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ScreenViewport());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
