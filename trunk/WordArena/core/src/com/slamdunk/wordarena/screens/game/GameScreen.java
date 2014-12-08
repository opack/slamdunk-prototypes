package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.PathUtils;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldObjectsOverlay objectsOverlay;
	//private TiledMapActor mapActor;
	private Array<ComplexPath> paths;
	
	public GameScreen(SlamGame game) {
		super(game);
		objectsOverlay = new WorldObjectsOverlay();
		objectsOverlay.getStage().addListener(new MoveCameraDragListener(objectsOverlay.getStage().getCamera()));
		addOverlay(objectsOverlay);
		
		//mapActor = new TiledMapActor();
		//objectsOverlay.getWorld().addActor(mapActor);
		
		paths = new Array<ComplexPath>();
	}
	
	/**
	 * Initialise l'écran de jeu avec les données du fichier de propriété spécifié
	 */
	public void init(String propertiesFile) {
		// Chargement du fichier de propriétés
		TypedProperties battlefieldProperties = new TypedProperties(propertiesFile);
		
		// Chargement de l'image de fond
		//mapActor.load(battlefieldProperties.getStringProperty("map", ""));
		objectsOverlay.setBackgroundMap(battlefieldProperties.getStringProperty("map", ""));
		
		// Chargement des chemins
		paths = PathUtils.parseSVG("battlefields/battlefield0.svg", "paths");
		objectsOverlay.setPaths(paths);
	}
	
	public WorldObjectsOverlay getObjectsOverlay() {
		return objectsOverlay;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
