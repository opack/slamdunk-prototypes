package com.slamdunk.pixelkingdomadvanced.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.pixelkingdomadvanced.gameparts.prefabs.LetterCase;
import com.slamdunk.pixelkingdomadvanced.gameparts.scripts.LetterChooserScript;
import com.slamdunk.pixelkingdomadvanced.gameparts.scripts.Letters;
import com.slamdunk.toolkit.gameparts.components.position.GridLayoutScript;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.scene.Layer;
import com.slamdunk.toolkit.gameparts.scene.Scene;

public class WordArenaScreen implements Screen {
	private static final int GRID_ROWS = 9;
	private static final int GRID_COLS = 9;
	private Scene scene;
	
	public WordArenaScreen() {
		// Crée une nouvelle scène à la taille de l'écran avec une couche contenant la grille de jeu
		scene = new Scene(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		final Layer gameBoard = scene.addLayer("GameBoard");
		
		// Ajoute la grille avec les cases
		GameObject grid = gameBoard.createChild();
		grid.addComponent(GridLayoutScript.class);
		grid.getComponent(GridLayoutScript.class).fillUpward = true;
		grid.getComponent(GridLayoutScript.class).nbColumns = GRID_COLS;
		grid.getComponent(GridLayoutScript.class).nbRows = GRID_ROWS;
		grid.getComponent(GridLayoutScript.class).columnWidth = 48;
		grid.getComponent(GridLayoutScript.class).rowHeight = grid.getComponent(GridLayoutScript.class).columnWidth;
		for (int row = 0; row < GRID_ROWS; row++) {
			for (int col = 0; col < GRID_COLS; col++) {
				LetterCase letter = grid.createChild(LetterCase.class);
				letter.name = "R" + row + "C" + col;
				letter.getComponent(LetterChooserScript.class).letter = Letters.values()[MathUtils.random(25)];				
			}	
		}
		
		// Place la caméra au centre de l'écran
		scene.observationPoint.transform.relativePosition.x = scene.observationPoint.camera.viewportWidth / 2;
		scene.observationPoint.transform.relativePosition.y = scene.observationPoint.camera.viewportHeight / 2;
		
		// Initialise la scène
		scene.init();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		scene.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
