package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private TiledMapOverlay tiledmap;
	
	public GameScreen(SlamGame game) {
		super(game);
		// On va utiliser une couche contenant une tilemap
		tiledmap = OverlayFactory.createTiledMapOverlay();
		// Chargement d'une tiledmap
		tiledmap.load("tiledmaps/game.tmx");
		// Place la camera à l'endroit du premier château
		tiledmap.setCameraOnObject("objects", "castle1");
		// Ajout de la couche à l'écran
		addOverlay(tiledmap);
		
//DBG		MapObjects paths = tiledmap.getObjects("objects", RectangleMapObject.class, "type", "path");
//DBG		MapObjects castles = tiledmap.getObjects("objects", RectangleMapObject.class, "type", "castle");
	}

	@Override
	public String getName() {
		return NAME;
	}
}
