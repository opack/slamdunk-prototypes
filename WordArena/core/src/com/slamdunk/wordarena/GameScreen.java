package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;

public class GameScreen extends SlamScreen {

	public GameScreen() {
		// On va utiliser une couche contenant une tilemap
		TiledMapOverlay tiledmap = OverlayFactory.createTiledMapOverlay();
		// Chargement d'une tiledmap
		tiledmap.load("tiledmaps/game.tmx", 32, 15, 15);
		// Ajout de la couche à l'écran
		addOverlay(tiledmap);
	}
	
	@Override
	public String getName() {
		return "GAME";
	}

}
