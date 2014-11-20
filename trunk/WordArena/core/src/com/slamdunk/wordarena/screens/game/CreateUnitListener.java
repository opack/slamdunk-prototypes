package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.units.Units;

/**
 * Lance la création d'une unité si un bouton correspondant
 * est actif
 */
public class CreateUnitListener extends InputListener {
	private InGameUIOverlay ui;
	private WorldObjectsOverlay worldObjects;
	private BattlefieldOverlay battlefield;
	
	public void init(GameScreen gameScreen) {
		ui = gameScreen.getUIOverlay();
		battlefield = gameScreen.getBattlefieldOverlay();
		worldObjects = gameScreen.getObjectsOverlay();
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		// Si un bouton de sélection d'unité est actif, alors on attend le touchUp pour créer l'unité
		return ui.getSelectedUnit() != null;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Point tilePosition = new Point((int)x, (int)y);
		if (!battlefield.isWalkable(tilePosition)) {
			return;
		}
		Units selectedUnit = ui.getSelectedUnit();
		if (selectedUnit != null) {
			// Envoie l'unité sur le chemin qui contient le tile touché.
			// Si ce tile est sur plusieurs chemins, on choisit celui qui
			// offre la plus courte distance entre le départ et le tile.
			Path bestPath = null;
			int shortestDistance = -1;
			int curDistance;
			for (Path path : battlefield.getPlayerPaths()) {
				curDistance = path.distanceTo(tilePosition);
				if (curDistance != -1
				&& (shortestDistance == -1 || shortestDistance > curDistance)) {
					shortestDistance = curDistance;
					bestPath = path;
				}
			}
			if (bestPath != null) {
				worldObjects.spawnUnit(selectedUnit, bestPath);
			}
		}
	}
}
