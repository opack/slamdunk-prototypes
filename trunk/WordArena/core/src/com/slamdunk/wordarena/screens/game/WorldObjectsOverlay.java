package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
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
	
	/**
	 * Image correspondant à zone de sélection
	 */
	private Image selectArea;
	private NinePatch patch;
	private Rectangle area;
	
	public WorldObjectsOverlay() {
		selectedUnits = new ArrayList<SimpleUnit>();
		// On crée un Stage en attendant que la méthode init() utilise le bon viewport
		createStage(new ScreenViewport());
		// Création de l'image pour la zone de sélection*
		area = new Rectangle();
		patch = processNinePatchFile("textures/select_area.9.png");
		selectArea = new Image(new NinePatchDrawable(patch));
		//selectArea.setVisible(false);
		selectArea.setPosition(0, 0);
		selectArea.setSize(1, 1);
		getStage().addActor(selectArea);
	}
	
	private static NinePatch processNinePatchFile(String fname) {
	    final Texture t = new Texture(Gdx.files.internal(fname));
	    final int width = t.getWidth() - 2;
	    final int height = t.getHeight() - 2;
	    return new NinePatch(new TextureRegion(t, 1, 1, width, height), 12, 12, 12, 12);
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
	 * Met à jour la position et la taille de la zone de sélection
	 * @param area Si null, la zone n'est plus affichée
	 */
	public void updateSelectArea(Rectangle area) {
		this.area.set(area);
		selectArea.setVisible(area != null);
		if (selectArea.isVisible()) {
			selectArea.setPosition(area.x, area.y);
			selectArea.setSize(area.width, area.height);
		}
	}

	@Override
	public void draw() {
		System.out.println("WorldObjectsOverlay.draw()"+area);
//		getStage().getBatch().begin();
//		patch.draw(getStage().getBatch(), area.x, area.y, area.width, area.height);
//		getStage().getBatch().end();
		super.draw();
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
		Collection<SimpleUnit> units = UnitManager.getInstance().getUnits(faction);
		if (units != null && !units.isEmpty()) {
			for (SimpleUnit unit : units) {
				if (area.contains(unit.getX(), unit.getY())) {
					unit.setColor(0.66f, 0.88f, 1f, 1f);
					selectedUnits.add(unit);
				}
			}
		}
		
		// Cache la zone de sélection
		selectArea.setVisible(false);
	}
}
