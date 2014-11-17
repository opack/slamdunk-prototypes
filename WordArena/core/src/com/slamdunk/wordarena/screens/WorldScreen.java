package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.lang.FileHelper;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.ButtonClickListener;

/**
 * Ecran qui affiche la carte du monde
 */
public class WorldScreen extends SlamScreen {
	public static final String NAME = "WORLD";
	
	private UIOverlay uiOverlay;
	
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
		uiOverlay = OverlayFactory.createUIOverlay();
		uiOverlay.loadLayout("layouts/world.json");
		addOverlay(uiOverlay);
		
		// Ce listener sera chargé de démarrer une nouvelle mission
		final ButtonClickListener launchMissionListener = new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				String battleFieldPropertiesFile = (String)button.getUserObject();
				GameScreen gameScreen = (GameScreen)getGame().getScreen(GameScreen.NAME);
				gameScreen.init(battleFieldPropertiesFile);
				getGame().setScreen(GameScreen.NAME);
			}
		};
		
		// Parcours les fichiers descriptifs des battlefields
		// et ajoute les marqueurs sur la carte
		final Skin uiSkin = uiOverlay.getSkin();
		final Stage stage = uiOverlay.getStage();
		TypedProperties battlefieldProperties;
		for (FileHandle file : FileHelper.getFiles("battlefields")) {
			battlefieldProperties = new TypedProperties(file);
			float x = battlefieldProperties.getFloatProperty("worldPosition.x", 0);
			float y = battlefieldProperties.getFloatProperty("worldPosition.y", 0);
			
			// Vérifie l'état de cette mission (verrouillée, déverrouillée, achevée)
			// dans les préférences pour choisir l'image adéquate et activer le bouton
			// si nécessaire
			// TODO
//			MissionStates missionState = lire dans les prefs;
			String buttonStyle = "default";
//			switch (missionState) {
//			case LOCKED: buttonStyle = "locked"; break;
//			case UNLOCKED: buttonStyle = "unlocked"; break;
//			case ACCOMPLISHED: buttonStyle = "accomplished"; break;
//			}
			
			// Crée le bouton avec comme UserObject le nom du properties
			Button startMission = new Button(uiSkin, buttonStyle);
			startMission.setPosition(x, y);
			startMission.setSize(32, 32);
			startMission.setUserObject(file.path());
			startMission.addListener(launchMissionListener);
			stage.addActor(startMission);
		}
	}

}
