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
		tiledmap.load("tiledmaps/game.tmx", 32, 15, 15);
		// Place la camera � l'endroit du premier ch�teau
		tiledmap.setCameraOnObject("castles", "castle1");
		// Ajout de la couche � l'�cran
		addOverlay(tiledmap);
	}

	@Override
	public String getName() {
		return NAME;
	}
}
