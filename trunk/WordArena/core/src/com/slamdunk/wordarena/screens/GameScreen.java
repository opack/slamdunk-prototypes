package com.slamdunk.wordarena.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class GameScreen extends SlamScreen implements TiledMapInputProcessor {
	public static final String NAME = "GAME";

	private TiledMapOverlay tiledmapOverlay;
	private WorldOverlay worldOverlay;
	private UIOverlay uiOverlay;
	
	private List<Path> paths;
	
	private boolean isSpawingUnits;
	
	public GameScreen(SlamGame game) {
		super(game);
		createTiledMapOverlay();
		createWorldOverlay();
		createUIOverlay();
		
	    // Recherche les chemins depuis les points de spawn vers le château adverse
	    MapObjects spawnPoints = tiledmapOverlay.getObjects("markers", RectangleMapObject.class, "spawn", "castle1");
	    MapObject enemyCastle = tiledmapOverlay.getObject("markers", "castle2");
	    paths = new ArrayList<Path>();
	    for (MapObject spawnPoint : spawnPoints) {
	    	Path path = tiledmapOverlay.findPath(spawnPoint, enemyCastle);
	    	if (path != null) {
	    		paths.add(path);
	    	}
	    }
	}

	/**
	 * Crée et initialise la couche qui contient l'UI du jeu
	 */
	private void createUIOverlay() {
		UIOverlay ui = OverlayFactory.createUIOverlay();
		ui.loadLayout("layouts/game.json");
		
		// Création des listeners qui interprèteront les clics sur les boutons
		Map<String, EventListener> listeners = new HashMap<String, EventListener>();
		listeners.put("spawn", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				isSpawingUnits = !isSpawingUnits;
			}
		});
		ui.setListeners(listeners);
		
		addOverlay(ui);
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
		if (isSpawingUnits) {
			// Si un chemin contient le tile touché, alors on crée une nouvelle
			// unité sur ce chemin et on l'envoie en direction du château ennemi
			for (Path path : paths) {
				if (path.contains(tilePosition)) {
					UnitMapObjet unit = createUnit();
					unit.setSpeed(3);
					Point departure = path.getPosition(0);
					unit.setPosition(departure.getX(), departure.getY());
					unit.follow(path);
					break;
				}
			}
		} else {
			// Si on n'a pas cliqué pour créer une unité, alors on déplace la caméra
			worldOverlay.getStage().getCamera().position.x = worldPosition.x;
			worldOverlay.getStage().getCamera().position.y = worldPosition.y;
		}
		return true;
	}

	private UnitMapObjet createUnit() {
		final float pixelsByUnit = tiledmapOverlay.getPixelsByTile();
		TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/hero2.png")));
		UnitMapObjet unit = new UnitMapObjet();
		
		unit.createDrawers(true, false, false);
		unit.getTextureDrawer().setTextureRegion(textureRegion);
		unit.getTextureDrawer().setActive(true);
		unit.setSize(textureRegion.getRegionWidth() / pixelsByUnit, textureRegion.getRegionHeight() / pixelsByUnit);
		
		worldOverlay.getWorld().addActor(unit);
		return unit;
	}
}
