package com.slamdunk.wordarena.screens.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;

/**
 * Ecran qui affiche la carte du monde
 */
public class WorldScreen extends SlamScreen {
	public static final String NAME = "WORLD";
	
	private WorldMapOverlay uiOverlay;
	
	public WorldScreen(SlamGame game) {
		super(game);
		createUIOverlay();
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Crée et initialise la couche qui contient la carte du monde
	 * et les marqueurs pour chaque champ de bataille
	 */
	private void createUIOverlay() {
		uiOverlay = new WorldMapOverlay();
		uiOverlay.setSkin(new Skin(Gdx.files.internal(UIOverlay.DEFAULT_SKIN)));
		uiOverlay.createStage(new ScreenViewport());
		uiOverlay.loadLayout("layouts/world.json");
		uiOverlay.initWorldMap();
		addOverlay(uiOverlay);
	}
}
