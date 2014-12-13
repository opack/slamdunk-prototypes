package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.PathUtils;
import com.slamdunk.wordarena.ai.AI;
import com.slamdunk.wordarena.ai.BasicAI;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;
import com.slamdunk.wordarena.units.Units;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldObjectsOverlay objectsOverlay;
	private InGameUIOverlay uiOverlay;
	private Array<ComplexPath> paths;
	
	private AI enemyAI;
	
	public GameScreen(SlamGame game) {
		super(game);
		
		// Crée la couche qui contient les objets du monde
		objectsOverlay = new WorldObjectsOverlay(this);
		objectsOverlay.getStage().addListener(new MoveCameraDragListener(objectsOverlay.getStage().getCamera()));
		addOverlay(objectsOverlay);
		
		// Crée la couche qui contient l'UI
		uiOverlay = new InGameUIOverlay();
		addOverlay(uiOverlay);
		
		paths = new Array<ComplexPath>();
	}
	
	public InGameUIOverlay getUiOverlay() {
		return uiOverlay;
	}

	public WorldObjectsOverlay getObjectsOverlay() {
		return objectsOverlay;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Initialise l'écran de jeu avec les données du fichier de propriété spécifié
	 */
	public void init(String propertiesFile) {
		// Chargement du fichier de propriétés
		TypedProperties battlefieldProperties = new TypedProperties(propertiesFile);
		
		// Chargement de l'image de fond
		objectsOverlay.setBackgroundMap(battlefieldProperties.getStringProperty("map", ""));
		
		// Chargement des chemins
		paths = PathUtils.parseSVG("battlefields/battlefield0.svg", "paths");
		objectsOverlay.setPaths(paths);
		
		// Création de l'IA adverse
		enemyAI = new BasicAI(this, paths);
		
		// Création de l'interface utilisateur
		uiOverlay.init(Units.PALADIN, Units.ARCHER);
	}
	
	@Override
	public void render(float delta) {
		// Fait agir l'adversaire
		enemyAI.act(delta);
		
		super.render(delta);
	}
}
