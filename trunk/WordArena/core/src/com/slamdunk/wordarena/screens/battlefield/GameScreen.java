package com.slamdunk.wordarena.screens.battlefield;

import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.ai.AI;
import com.slamdunk.wordarena.ai.BasicAI;
import com.slamdunk.wordarena.screens.battlefield.goals.GoalManager;
import com.slamdunk.wordarena.screens.battlefield.goals.GoalManagerFactory;
import com.slamdunk.wordarena.screens.battlefield.goals.Goals;
import com.slamdunk.wordarena.units.Units;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldObjectsOverlay objectsOverlay;
	private InGameUIOverlay uiOverlay;
	
	private GoalManager objectiveManager;
	private AI enemyAI;
	
	public GameScreen(SlamGame game) {
		super(game);
		
		// Crée la couche qui contient les objets du monde
		objectsOverlay = new WorldObjectsOverlay(this);
		addOverlay(objectsOverlay);
		
		// Crée la couche qui contient l'UI
		uiOverlay = new InGameUIOverlay();
		addOverlay(uiOverlay);
	}
	
	public InGameUIOverlay getUiOverlay() {
		return uiOverlay;
	}

	public WorldObjectsOverlay getObjectsOverlay() {
		return objectsOverlay;
	}
	
	public GoalManager getObjectiveManager() {
		return objectiveManager;
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
		objectsOverlay.setBackgroundMap(battlefieldProperties.getStringProperty("background", ""));
		
		// Chargement du SVG contenant les données additionnelles
		objectsOverlay.loadObjects(battlefieldProperties.getStringProperty("data", ""));
		
		// Création de l'IA adverse
		enemyAI = new BasicAI(this, objectsOverlay.getPaths());
		
		// Création du gestionnaire d'objectif
		Goals objective = Goals.valueOf(battlefieldProperties.getStringProperty("objective", ""));
		objectiveManager = GoalManagerFactory.create(objective);
		
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
