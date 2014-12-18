package com.slamdunk.wordarena.screens.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.ai.AI;
import com.slamdunk.wordarena.ai.BasicAI;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;
import com.slamdunk.wordarena.screens.game.goals.GoalManager;
import com.slamdunk.wordarena.screens.game.goals.GoalManagerFactory;
import com.slamdunk.wordarena.screens.game.goals.Goals;
import com.slamdunk.wordarena.units.Units;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldObjectsOverlay objectsOverlay;
	private MoveCameraDragListener moveCameraListener;
	private InGameUIOverlay uiOverlay;
	
	private GoalManager objectiveManager;
	private AI enemyAI;
	
	public GameScreen(SlamGame game) {
		super(game);
		
		// Crée la couche qui contient les objets du monde
		objectsOverlay = new WorldObjectsOverlay(this);
		moveCameraListener = new MoveCameraDragListener(objectsOverlay.getStage().getCamera());
		objectsOverlay.getStage().addListener(moveCameraListener);
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
		initCameraBounds();
		
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
	
	/**
	 * La caméra ne doit pas perdre de vue la carte. On place les limites
	 * de déplacements de la caméra (donc du centre de la zone vue) dans
	 * un rectangle situé à 1/4 des bords de la carte.
	 */
	private void initCameraBounds() {
		Image background = (Image)objectsOverlay.getWorld().findActor("background");
		Rectangle cameraBounds = new Rectangle();
		cameraBounds.x = (int)(background.getWidth() / 4);
		cameraBounds.y = (int)(background.getHeight() / 4);
		// On laisse 1/4 de marge de chaque côté, donc la largeur totale de la zone
		// de déplacement est 1 - 1/4 - 1/4 = 1/2 taille de l'image
		cameraBounds.width = (int)(background.getWidth() / 2);
		cameraBounds.height = (int)(background.getHeight() / 2);
		moveCameraListener.setBounds(cameraBounds);
	}

	@Override
	public void render(float delta) {
		// Fait agir l'adversaire
		enemyAI.act(delta);
		
		super.render(delta);
	}
}
