package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

public class WorldObjectsOverlay extends WorldOverlay {
	
	public void init(Camera camera, float worldUnitsPerPixel) {
		// On va utiliser une couche Stage contenant les objets du monde
		// avec un viewport qui s'appuie sur la caméra de la tiledmap
		ScreenViewport viewport = new ScreenViewport(camera);
		viewport.setWorldSize(camera.viewportWidth, camera.viewportHeight);
		viewport.setUnitsPerPixel(worldUnitsPerPixel);
		
		UnitManager.getInstance().setStageContainer(getWorld());
	}
	
	/**
	 * Crée une unité et l'envoie sur le chemin indiqué
	 * @param path
	 */
	public void spawnUnit(Units unitType, Path path) {
		// Crée l'unité
		SimpleUnit unit = unitType.create((GameScreen)getScreen());
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		Point departure = path.getPosition(0);
		unit.setPosition(departure.getX(), departure.getY());
		unit.setPath(path);
	}
}
