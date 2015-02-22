package com.slamdunk.pixelkingdomadvanced.screens.battlefield;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slamdunk.pixelkingdomadvanced.ai.AI;
import com.slamdunk.pixelkingdomadvanced.ai.BasicAI;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.goals.GoalManager;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.goals.GoalManagerFactory;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.goals.Goals;
import com.slamdunk.pixelkingdomadvanced.screens.worldmap.WorldScreen;
import com.slamdunk.pixelkingdomadvanced.units.UnitManager;
import com.slamdunk.pixelkingdomadvanced.units.Units;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;

public class GameScreen extends SlamScreen {
	public static final String NAME = "GAME";

	private WorldObjectsOverlay objectsOverlay;
	private InGameUIOverlay uiOverlay;
	
	private GoalManager objectiveManager;
	private AI enemyAI;
	
	private boolean gameOver;
	
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
		
		// Nettoie les objets actuellements présents dans le jeu
		objectsOverlay.init(battlefieldProperties.getStringProperty("background", ""), battlefieldProperties.getStringProperty("data", ""));
		
		// Supression des unités existantes
		UnitManager.getInstance().removeAllUnits();
		
		// Création de l'IA adverse
		enemyAI = new BasicAI(this, objectsOverlay.getPaths());
		
		// Création du gestionnaire d'objectif
		Goals objective = Goals.valueOf(battlefieldProperties.getStringProperty("objective", ""));
		objectiveManager = GoalManagerFactory.create(objective);
		
		// Création de l'interface utilisateur
		// TODO Utiliser les unités autorisées du fichier properties et / ou celles choisies par l'utilisateur
		uiOverlay.init(Units.PALADIN, Units.ARCHER);
		
		// Le jeu démarre, il n'est pas fini !
		gameOver = false;
	}

	@Override
	public void render(float delta) {
		// Teste la fin de partie
		if (objectiveManager.isGameFinished()) {
			if (!gameOver) {
				// Affiche la fin du jeu
				uiOverlay.showPopup(objectiveManager.getGameState().toString(), new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						getGame().setScreen(WorldScreen.NAME);
					}
				});
				// Arrête toutes les unités
				UnitManager.getInstance().stopAllUnits();
				// Le jeu est fini !
				gameOver = true;
			}
		} else {
			// Fait agir l'adversaire
			enemyAI.act(delta);
		}
		
		// Met à jour les autres éléments du jeu
		super.render(delta);
	}
}
