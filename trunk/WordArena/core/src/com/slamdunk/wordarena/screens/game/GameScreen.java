package com.slamdunk.wordarena.screens.game;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.ai.AI;
import com.slamdunk.wordarena.ai.BasicAI;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.Units;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private BattlefieldOverlay battlefieldOverlay;
	private WorldObjectsOverlay objectsOverlay;
	private InGameUIOverlay uiOverlay;
	
	private AI enemyAI;
	
	public GameScreen(SlamGame game) {
		super(game);
		// Crée la couche qui contient la carte du champ de bataille
		battlefieldOverlay = new BattlefieldOverlay();
		addOverlay(battlefieldOverlay);
		
		// Crée la couche qui contient les objets "dynamiques" du monde
		objectsOverlay = new WorldObjectsOverlay();
		addOverlay(objectsOverlay);
		
		// Crée la couche qui contient l'UI
		uiOverlay = new InGameUIOverlay();
		addOverlay(uiOverlay);
	}
	
	/**
	 * Initialise l'écran de jeu avec les données du fichier de propriété
	 * spécifié
	 */
	public void init(String propertiesFile) {
		TypedProperties battlefieldProperties = new TypedProperties(propertiesFile);
				
		battlefieldOverlay.init(battlefieldProperties.getStringProperty("map", ""));
		objectsOverlay.init(battlefieldOverlay.getCamera(), 1 / battlefieldOverlay.getPixelsByTile());
		uiOverlay.init(Units.PALADIN, Units.ARCHER);
		
		// Initialise l'IA
		createAI(battlefieldOverlay.getEnemyPaths());
	}

	@Override
	public void render(float delta) {
		// Fait agir l'adversaire
		enemyAI.act(delta);
		
		super.render(delta);
	}
	
	/**
	 * Crée l'IA chargée de gérer le spawn des unités ennemies
	 * @param enemyPaths
	 */
	private void createAI(List<Path> enemyPaths) {
		enemyAI = new BasicAI(this, enemyPaths);
	}

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Retourne le nombre de pixels dans 1 unité du monde
	 * @return
	 */
	public float getPixelsByUnit() {
		return battlefieldOverlay.getPixelsByTile();
	}

	public BattlefieldOverlay getBattlefieldOverlay() {
		return battlefieldOverlay;
	}

	public WorldObjectsOverlay getObjectsOverlay() {
		return objectsOverlay;
	}
	
	public InGameUIOverlay getUIOverlay() {
		return uiOverlay;
	}

	/**
	 * Tente de créer une instance de l'unité sélectionnée sur la position
	 * de la tuile indiquée
	 * @param tilePosition
	 */
	public void createUnit(Point tilePosition) {
		if (!battlefieldOverlay.isWalkable(tilePosition)) {
			return;
		}
		Units selectedUnit = uiOverlay.getSelectedUnit();
		if (selectedUnit != null) {
			// Envoie l'unité sur le chemin qui contient le tile touché.
			// Si ce tile est sur plusieurs chemins, on choisit celui qui
			// offre la plus courte distance entre le départ et le tile.
			Path bestPath = null;
			int shortestDistance = -1;
			int curDistance;
			for (Path path : battlefieldOverlay.getPlayerPaths()) {
				curDistance = path.distanceTo(tilePosition);
				if (curDistance != -1
				&& (shortestDistance == -1 || shortestDistance > curDistance)) {
					shortestDistance = curDistance;
					bestPath = path;
				}
			}
			if (bestPath != null) {
				objectsOverlay.spawnUnit(selectedUnit, bestPath);
			}
		}
	}
	
	/**
	 * Met à jour la position et la taille de la zone de sélection
	 * @param area Si null, la zone n'est plus affichée
	 */
	public void updateSelectArea(Rectangle area) {
		final float worldUnitsByPixel = 1 / battlefieldOverlay.getPixelsByTile(); 
//		area.x /= worldUnitsByPixel;
//		area.y /= worldUnitsByPixel;
//		area.width /= worldUnitsByPixel;
//		area.height /= worldUnitsByPixel;
		System.out.println("GameScreen.updateSelectArea()" + area);
		objectsOverlay.updateSelectArea(area);
	}

	/**
	 * Sélectionne les unités qui se trouvent dans la zone indiquée
	 * @param selectArea
	 */
	public void selectUnitsIn(Rectangle selectArea) {
		objectsOverlay.selectUnitsIn(Factions.PLAYER, selectArea);
	}
}
