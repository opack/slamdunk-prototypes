package com.slamdunk.wordarena.screens.worldmap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.lang.FileHelper;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;
import com.slamdunk.wordarena.screens.game.GameScreen;

/**
 * Gère une carte du monde qui contient plusieurs boutons à cliquer
 * pour se rendre sur une mission particulière du jeu.
 */
public class WorldMapOverlay extends UIOverlay {
	MoveCameraDragListener moveCameraListener;
	
	public void createStage(Viewport viewport) {
		super.createStage(viewport);
		moveCameraListener = new MoveCameraDragListener(getStage().getCamera());
		getStage().addListener(moveCameraListener);
	}

	/**
	 * Initialise la carte du monde en ajoutant un bouton pour chaque mission
	 */
	public void initWorldMap() {
		final Stage stage = getStage();
		Image image = (Image)stage.getRoot().findActor("background");
		image.setWidth(image.getPrefWidth());
		image.setHeight(image.getPrefHeight());
		
		// La caméra ne doit pas perdre de vue la carte. On place les limites
		// de déplacements de la caméra (donc du centre de la zone vue) dans
		// un rectangle situé à 1/4 des bords de la carte.
		Rectangle cameraBounds = new Rectangle();
		cameraBounds.x = (int)(image.getWidth() / 4);
		cameraBounds.y = (int)(image.getHeight() / 4);
		// On laisse 1/4 de marge de chaque côté, donc la largeur totale de la zone
		// de déplacement est 1 - 1/4 - 1/4 = 1/2 taille de l'image
		cameraBounds.width = (int)(image.getWidth() / 2);
		cameraBounds.height = (int)(image.getHeight() / 2);
		moveCameraListener.setBounds(cameraBounds);
		
		// Ce listener sera chargé de démarrer une nouvelle mission
		final ButtonClickListener launchMissionListener = new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				final SlamGame game = getScreen().getGame();
				String battleFieldPropertiesFile = (String)button.getUserObject();
				GameScreen gameScreen = (GameScreen)game.getScreen(GameScreen.NAME);
				gameScreen.init(battleFieldPropertiesFile);
				game.setScreen(GameScreen.NAME);
			}
		};
		
		// Parcours les fichiers descriptifs des battlefields
		// et ajoute les marqueurs sur la carte
		final Skin uiSkin = getSkin();
		TypedProperties battlefieldProperties;
		for (FileHandle file : FileHelper.getFiles("battlefields")) {
			battlefieldProperties = new TypedProperties(file);
			float x = battlefieldProperties.getFloatProperty("worldPosition.x", 0);
			float y = battlefieldProperties.getFloatProperty("worldPosition.y", 0);
			
			// Vérifie l'état de cette mission (verrouillée, déverrouillée, achevée)
			// dans les préférences pour choisir l'image adéquate et activer le bouton
			// si nécessaire
			// TODO
//					MissionStates missionState = lire dans les prefs;
			String buttonStyle = "default";
//					switch (missionState) {
//					case LOCKED: buttonStyle = "locked"; break;
//					case UNLOCKED: buttonStyle = "unlocked"; break;
//					case ACCOMPLISHED: buttonStyle = "accomplished"; break;
//					}
			
			// Crée le bouton avec comme UserObject le nom du properties
			Button startMission = new Button(uiSkin, buttonStyle);
			startMission.setPosition(x, y);
			startMission.setSize(32, 32);
			startMission.setUserObject(file.path());
			startMission.addListener(launchMissionListener);
			stage.addActor(startMission);
		}
	};
}
