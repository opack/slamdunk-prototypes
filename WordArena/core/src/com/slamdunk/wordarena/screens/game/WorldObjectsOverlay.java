package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

public class WorldObjectsOverlay extends WorldOverlay {
	private Array<ComplexPath> paths;
	
	public WorldObjectsOverlay() {
		// On crée un Stage en attendant que la méthode init() utilise le bon viewport
		createStage(new FitViewport(800, 480));
		// Crée l'image contenant le fond de carte
		Image background = new Image();
		background.setName("background");
		background.addListener(new SpawnUnitListener(this));
		getWorld().addActor(background);
		//
		UnitManager.getInstance().setStageContainer(getWorld());
	}

	public void init(Camera camera, float worldUnitsPerPixel) {
		// On va utiliser une couche Stage contenant les objets du monde
		// avec un viewport qui s'appuie sur la caméra de la tiledmap
//		ScreenViewport viewport = new ScreenViewport(camera);
//		viewport.setWorldSize(camera.viewportWidth, camera.viewportHeight);
//		viewport.setUnitsPerPixel(worldUnitsPerPixel);
//		viewport.update((int)(camera.viewportWidth / worldUnitsPerPixel), (int)(camera.viewportHeight / worldUnitsPerPixel));
		FitViewport viewport = new FitViewport(800, 480);
		viewport.setWorldSize(camera.viewportWidth, camera.viewportHeight);
//		viewport.setUnitsPerPixel(worldUnitsPerPixel);
		viewport.update((int)(camera.viewportWidth / worldUnitsPerPixel), (int)(camera.viewportHeight / worldUnitsPerPixel));
		getStage().setViewport(viewport);
		
		UnitManager.getInstance().setStageContainer(getWorld());
	}
	
//DBG	/**
//	 * Crée une unité et l'envoie sur le chemin indiqué
//	 * @param path
//	 */
//	public void spawnUnit(Units unitType, Path path) {
//		// Crée l'unité
//		SimpleUnit unit = unitType.create((GameScreen)getScreen());
//		
//		// Ajoute l'unité au monde
//		UnitManager.getInstance().addUnit(unit);
//		
//		// Envoie l'unité sur le chemin spécifié
//		Point departure = path.getPosition(0);
//		unit.setPosition(departure.getX(), departure.getY());
//		unit.setPath(path);
//	}
	

	public void spawnUnit(Units unitType, ComplexPath path) {
		// Crée l'unité
		SimpleUnit unit = unitType.create((GameScreen)getScreen());
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		unit.setPath(path);
	}

	/**
	 * Définit le fond de carte
	 * @param mapImageFile
	 */
	public void setBackgroundMap(String mapImageFile) {
		Image background = (Image)getWorld().findActor("background");
		TextureRegion region = new TextureRegion(new Texture(mapImageFile));
		background.setDrawable(new TextureRegionDrawable(region));
		background.setPosition(0, 0);
		background.setSize(region.getRegionWidth(), region.getRegionHeight());
	}

	public Array<ComplexPath> getPaths() {
		return paths;
	}

	public void setPaths(Array<ComplexPath> paths) {
		this.paths = paths;
	}
}
