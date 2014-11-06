package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.graphics.tiled.SpriteMapObject;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay.TiledMapInputProcessor;
import com.slamdunk.toolkit.world.point.Point;

public class GameScreen extends SlamScreen implements TiledMapInputProcessor {
	public static final String NAME = "GAME";

	private TiledMapOverlay tiledmap;
	private SpriteMapObject hero;
	
	public GameScreen(SlamGame game) {
		super(game);
		// On va utiliser une couche contenant une tilemap
		tiledmap = OverlayFactory.createTiledMapOverlay();
		// Chargement d'une tiledmap
		tiledmap.load("tiledmaps/game.tmx");
		// Définit le gestionnaire des entrées utilisateur
		tiledmap.setTileInputProcessor(this);
		// Initialise le pathfinder
		tiledmap.initPathfinder(false);
		tiledmap.setWalkables("markers", RectangleMapObject.class, "type", "path");
		// Place la camera à l'endroit du premier château
		tiledmap.setCameraOnObject("markers", "castle1");
		// Ajout de la couche à l'écran
		addOverlay(tiledmap);
		
		// Ajoute un sprite pour le héros
	    Sprite sprite = new Sprite(new Texture(Gdx.files.internal("textures/hero2.png")));
	    hero = tiledmap.addSprite(sprite, "hero");
	    MapObject spawnPoint = tiledmap.getObject("markers", "spawn11");
	    hero.setPixelPosition((Float)spawnPoint.getProperties().get("x"), (Float)spawnPoint.getProperties().get("y"));
	    tiledmap.setCameraOnObject(hero);
	    
	    // Ajoute un actor pour le héros
	    Actor
	    
//DBG	    // Envoie le héros vers le château adverse
//	    MapObject enemyCastle = tiledmap.getObject("markers", "castle2");
//	    List<Point> tiledmap.findPath((int)hero.getMapX(), (int)hero.getMapY());
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tileTouchUp(Vector3 worldPosition, Point tilePosition) {
		hero.setMapPosition(tilePosition.getX(), tilePosition.getY());
		tiledmap.setCameraOnObject(hero);
		return true;
	}

	@Override
	public boolean tileTouchDown(Vector3 worldPosition, Point tilePosition) {
		// TODO Auto-generated method stub
		return false;
	}
}
