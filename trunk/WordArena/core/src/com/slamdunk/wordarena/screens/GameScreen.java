package com.slamdunk.wordarena.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay.TiledMapInputProcessor;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;

public class GameScreen extends SlamScreen implements TiledMapInputProcessor {
	public static final String NAME = "GAME";

	private TiledMapOverlay tiledmapOverlay;
	private WorldOverlay worldOverlay;
	private List<Path> paths;
	
	public GameScreen(SlamGame game) {
		super(game);
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
		
		// On va utiliser une couche Stage contenant les objets du monde
		// avec un viewport qui s'appuie sur la caméra de la tiledmap
		final float pixelsByUnit = tiledmapOverlay.getPixelsByTile();
		ScreenViewport viewport = new ScreenViewport(tiledmapOverlay.getCamera());
		viewport.setWorldSize(tiledmapOverlay.getMapWidth(), tiledmapOverlay.getMapHeight());
		viewport.setUnitsPerPixel(1 / pixelsByUnit);
		worldOverlay = OverlayFactory.createWorldOverlay(viewport);
		addOverlay(worldOverlay);
	    
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

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition) {
		// Mise à jour des caméras pour que celle du WorldOverlay montre les bons
		// acteurs, et que celle du TiledMapOverlay montre la bonne partie de la carte
		worldOverlay.getStage().getCamera().position.x = worldPosition.x;
		worldOverlay.getStage().getCamera().position.y = worldPosition.y;
		System.out.println("DRAG  " + worldPosition);
		return true;
	}

	@Override
	public boolean tileTouchUp(Vector3 worldPosition, Point tilePosition) {
		// Si un chemin contient le tile touché, alors on crée une nouvelle
		// unité sur ce chemin et on l'envoie en direction du château ennemi
		for (Path path : paths) {
			if (path.contains(tilePosition)) {
				UnitMapObjet unit = createUnit();
				unit.setSpeed(3);
				Point departure = path.getPosition(0);
				unit.setPosition(departure.getX(), departure.getY());
				unit.follow(path);
			}
		}
		System.out.println("TOUCH " + worldPosition);
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

	@Override
	public boolean tileTouchDown(Vector3 worldPosition, Point tilePosition) {
		// TODO Auto-generated method stub
		return false;
	}
}
