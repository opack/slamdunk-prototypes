package com.slamdunk.wordarena.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay.TiledMapInputProcessor;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.ai.AI;
import com.slamdunk.wordarena.ai.BasicAI;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

public class GameScreen extends SlamScreen implements TiledMapInputProcessor {
	public static final String NAME = "GAME";

	private TiledMapOverlay tiledmapOverlay;
	private WorldOverlay worldOverlay;
	private UIOverlay uiOverlay;
	
	private List<Path> playerPaths;
	private AI enemyAI;
	
	private Units spawningUnit;
	
	public GameScreen(SlamGame game) {
		super(game);
		createTiledMapOverlay();
		createWorldOverlay();
		createUIOverlay();
		
	    // Recherche les chemins depuis les points de spawn vers le château adverse
		playerPaths = searchPaths("castle1", "castle2");
		List<Path> enemyPaths = searchPaths("castle2", "castle1");
		
		// Initialise l'IA
		createAI(enemyPaths);
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

	/**
	 * Recherche les chemins depuis les emplacements spawn du château
	 * fromCastle vers le château toCastle
	 * @param string
	 * @param string2
	 * @return
	 */
	private List<Path> searchPaths(String fromCastle, String toCastle) {
		MapObjects spawnPoints = tiledmapOverlay.getObjects("markers", RectangleMapObject.class, "spawn", fromCastle);
	    MapObject enemyCastle = tiledmapOverlay.getObject("markers", toCastle);
	    List<Path> paths = new ArrayList<Path>();
	    for (MapObject spawnPoint : spawnPoints) {
	    	Path path = tiledmapOverlay.findPath(spawnPoint, enemyCastle);
	    	if (path != null) {
	    		paths.add(path);
	    	}
	    }
		return paths;
	}

	/**
	 * Crée et initialise la couche qui contient l'UI du jeu
	 */
	private void createUIOverlay() {
		uiOverlay = OverlayFactory.createUIOverlay();
		uiOverlay.loadLayout("layouts/game.json");
		
		// Création des listeners qui interprèteront les clics sur les boutons
		Map<String, EventListener> listeners = new HashMap<String, EventListener>();
		listeners.put("move_camera", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				spawningUnit = null;
			}
		});
		listeners.put("spawn_paladin", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				spawningUnit = Units.PALADIN;
			}
		});
		listeners.put("spawn_ranger", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				spawningUnit = Units.ARCHER;
			}
		});
		uiOverlay.setListeners(listeners);
		
		addOverlay(uiOverlay);
	}

	/**
	 * Crée et initialise la couche qui contient les objets "dynamiques" du monde
	 */
	private void createWorldOverlay() {
		// On va utiliser une couche Stage contenant les objets du monde
		// avec un viewport qui s'appuie sur la caméra de la tiledmap
		ScreenViewport viewport = new ScreenViewport(tiledmapOverlay.getCamera());
		viewport.setWorldSize(tiledmapOverlay.getMapWidth(), tiledmapOverlay.getMapHeight());
		viewport.setUnitsPerPixel(1 / tiledmapOverlay.getPixelsByTile());
		
		worldOverlay = OverlayFactory.createWorldOverlay(viewport);
		addOverlay(worldOverlay);
		
		UnitManager.getInstance().setStageContainer(worldOverlay.getWorld());
	}

	/**
	 * Crée et initialise la couche qui contient la tiledmap, affichant
	 * la cartographie "statique" du monde
	 */
	private void createTiledMapOverlay() {
		// On va utiliser une couche contenant une tilemap
		tiledmapOverlay = OverlayFactory.createTiledMapOverlay();
		// Chargement d'une tiledmap
		tiledmapOverlay.load("tiledmaps/game.tmx");
		// Définit le gestionnaire des entrées utilisateur
		tiledmapOverlay.setTileInputProcessor(this);
		// Initialise le pathfinder
		tiledmapOverlay.initPathfinder(false);
		tiledmapOverlay.setWalkables("markers", RectangleMapObject.class, "type", "path");
		
		// Place la camera à l'endroit du premier château
		tiledmapOverlay.setCameraOnObject("markers", "castle1");
		
		// Ajout de la couche à l'écran
		addOverlay(tiledmapOverlay);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition) {
		return false;
	}


	@Override
	public boolean tileTouchDown(Vector3 worldPosition, Point tilePosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean tileTouchUp(Vector3 worldPosition, Point tilePosition) {
		if (spawningUnit == null) {
			// Si on n'a pas cliqué pour créer une unité, alors on déplace la caméra
			worldOverlay.getStage().getCamera().position.x = worldPosition.x;
			worldOverlay.getStage().getCamera().position.y = worldPosition.y;
		} else {
			// Si un chemin contient le tile touché, alors on crée une nouvelle
			// unité sur ce chemin et on l'envoie en direction du château ennemi
			for (Path path : playerPaths) {
				if (path.contains(tilePosition)) {
					spawnUnit(
						spawningUnit.create(this),
						path);
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Crée une unité et l'envoie sur le chemin indiqué
	 * @param path
	 */
	public void spawnUnit(SimpleUnit unit, Path path) {
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		Point departure = path.getPosition(0);
		unit.setPosition(departure.getX(), departure.getY());
		unit.setPath(path);
	}
	
	/**
	 * Retourne le nombre de pixels dans 1 unité du monde
	 * @return
	 */
	public float getPixelsByUnit() {
		return tiledmapOverlay.getPixelsByTile();
	}

	public TiledMapOverlay getTiledMap() {
		return tiledmapOverlay;
	}
}
