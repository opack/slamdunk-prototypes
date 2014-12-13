package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.PathUtils;
import com.slamdunk.wordarena.units.Units;

/**
 * Gère le clic sur le fond de carte de façon à déterminer si un chemin
 * a été sélectionné, et, le cas échéant, créer une unité
 */
public class SpawnUnitListener extends InputListener {
	private final static int PATH_SELECT_TOLERANCE = 25;
	private ComplexPath selectedPath;
	
	private GameScreen gameScreen;
	
	public SpawnUnitListener(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		// Recherche le chemin qui est proche du clic. S'il y en a un, on déclenchera
		// une action sur le clic. S'il n'y en a pas, on n'écoute pas le touchUp()
		ComplexPathCursor cursor = PathUtils.selectNearestPath(gameScreen.getObjectsOverlay().getPaths(), new Vector2(x, y), PATH_SELECT_TOLERANCE);
		if (cursor != null) {
			selectedPath = cursor.getPath();
			return true;
		} else {
			selectedPath = null;
			return false;
		}
	}
	
	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		// Si le joueur fait un drag, alors c'est qu'il ne tentait pas de créer une unité
		selectedPath = null;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (selectedPath != null) {
			// Récupération de l'unité sélectionnée
			Units selectedUnit = gameScreen.getUiOverlay().getSelectedUnit();
			if (selectedUnit != null) {
				// Création d'une unité
				gameScreen.getObjectsOverlay().spawnUnit(selectedUnit, selectedPath);
			}
		}
	}
}
