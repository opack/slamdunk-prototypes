package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.world.TiledMapActor;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;

public class GameScreen2 extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldOverlay objectsOverlay;
	private TiledMapActor mapActor;
	
	public GameScreen2(SlamGame game) {
		super(game);
		objectsOverlay = new WorldOverlay();
		objectsOverlay.createStage(new FitViewport(800, 480));
		objectsOverlay.getStage().addListener(new MoveCameraDragListener(objectsOverlay.getStage().getCamera()));
		addOverlay(objectsOverlay);
		
		mapActor = new TiledMapActor();
		objectsOverlay.getWorld().addActor(mapActor);
	}
	
	/**
	 * Initialise l'écran de jeu avec les données du fichier de propriété
	 * spécifié
	 */
	public void init(String propertiesFile) {
		TypedProperties battlefieldProperties = new TypedProperties(propertiesFile);
		mapActor.load(battlefieldProperties.getStringProperty("map", ""));
	}

	@Override
	public String getName() {
		return NAME;
	}
}
