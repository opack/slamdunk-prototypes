package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.lang.FileHelper;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.ButtonClickListener;

/**
 * Gère une carte du monde qui contient plusieurs boutons à cliquer
 * pour se rendre sur une mission particulière du jeu.
 */
public class WorldMapOverlay extends UIOverlay {
	/**
	 * Listener chargé de déplacer la caméra en fonction des drags de
	 * souris/doigt
	 */
	private DragListener moveMapListener = new DragListener() {
		private Vector2 previousDragPos = new Vector2();
		private Vector3 cameraPos;
		
		public void dragStart(InputEvent event, float x, float y, int pointer) {
			previousDragPos.x = x;
			previousDragPos.y = y;
			cameraPos = getStage().getCamera().position;
		};
		
		public void drag(InputEvent event, float x, float y, int pointer) {
			cameraPos.x += previousDragPos.x - x;
			cameraPos.y += previousDragPos.y - y;
		};
	};
	
	public void createStage(Viewport viewport) {
		super.createStage(viewport);
		
		getStage().addListener(moveMapListener);
	}

	/**
	 * Initialise la carte du monde en ajoutant un bouton pour chaque mission
	 */
	public void initWorldMap() {
		final Stage stage = getStage();
		Image image = (Image)stage.getRoot().findActor("background");
		image.setWidth(image.getPrefWidth());
		image.setHeight(image.getPrefHeight());
		
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
