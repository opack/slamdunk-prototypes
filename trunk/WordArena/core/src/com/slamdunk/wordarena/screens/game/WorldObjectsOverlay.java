package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

public class WorldObjectsOverlay extends WorldOverlay {
	
	/**
	 * Contient les unités éventuellement sélectionnées
	 */
	private List<SimpleUnit> selectedUnits;
	
	public WorldObjectsOverlay() {
		selectedUnits = new ArrayList<SimpleUnit>();
		// On crée un Stage en attendant que la méthode init() utilise le bon viewport
		createStage(new ScreenViewport());
	}
	
	public void init(Camera camera, float worldUnitsPerPixel) {
		// On va utiliser une couche Stage contenant les objets du monde
		// avec un viewport qui s'appuie sur la caméra de la tiledmap
		ScreenViewport viewport = new ScreenViewport(camera);
		viewport.setWorldSize(camera.viewportWidth, camera.viewportHeight);
		viewport.setUnitsPerPixel(worldUnitsPerPixel);
		viewport.update((int)(camera.viewportWidth / worldUnitsPerPixel), (int)(camera.viewportHeight / worldUnitsPerPixel));
		getStage().setViewport(viewport);
		
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

	/**
	 * Sélectionne les unités présentes dans la zone
	 * indiquée
	 * @param selectArea
	 */
	public void selectUnitsIn(Factions faction, Rectangle area) {
		// Désélectionne les unités actuellement sélectionnées
		for (SimpleUnit unit : selectedUnits) {
			unit.setColor(1, 1, 1, 1);
		}
		selectedUnits.clear();
		
		// Sélectionne les unités dans la zone indiquée
		for (SimpleUnit unit : UnitManager.getInstance().getUnits(faction)) {
			if (area.contains(unit.getX(), unit.getY())) {
				unit.setColor(0.66f, 0.88f, 1f, 1f);
				selectedUnits.add(unit);
			}
		}
		System.out.println("DBG WorldObjectsOverlay.selectUnitsIn() " + selectedUnits.size() + " unités sélectionnées.");
	}
}
