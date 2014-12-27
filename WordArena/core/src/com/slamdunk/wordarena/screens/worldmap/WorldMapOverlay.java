package com.slamdunk.wordarena.screens.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.svg.SVGParse;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;

/**
 * Gère une carte du monde qui contient plusieurs boutons à cliquer
 * pour se rendre sur une mission particulière du jeu.
 */
public class WorldMapOverlay extends UIOverlay {
	private MoveCameraDragListener moveCameraListener;
	private ButtonClickListener launchMissionListener;
	
	public void createStage(Viewport viewport) {
		super.createStage(viewport);
		moveCameraListener = new MoveCameraDragListener(getStage().getCamera());
		getStage().addListener(moveCameraListener);
		
		// Ce listener sera chargé de démarrer une nouvelle mission
		launchMissionListener = new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				final SlamGame game = getScreen().getGame();
				String battleFieldPropertiesFile = (String)button.getUserObject();
				GameScreen gameScreen = (GameScreen)game.getScreen(GameScreen.NAME);
				gameScreen.init(battleFieldPropertiesFile);
				game.setScreen(GameScreen.NAME);
			}
		};
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
		// de déplacements de la caméra (donc du centre de la zone vue)
		moveCameraListener.computeMoveBounds(getStage().getCamera(), image, 20);
		
//		// Parcours les fichiers descriptifs des battlefields
//		// et ajoute les marqueurs sur la carte
//		final Skin uiSkin = getSkin();
//		TypedProperties battlefieldProperties;
//		for (FileHandle file : FileHelper.getFiles("battlefields")) {
//			battlefieldProperties = new TypedProperties(file);
//			float x = battlefieldProperties.getFloatProperty("worldPosition.x", 0);
//			float y = battlefieldProperties.getFloatProperty("worldPosition.y", 0);
//			
//			// Vérifie l'état de cette mission (verrouillée, déverrouillée, achevée)
//			// dans les préférences pour choisir l'image adéquate et activer le bouton
//			// si nécessaire
//			// TODO
//					MissionStates missionState = lire dans les prefs;
//			String buttonStyle = "default";
//					switch (missionState) {
//					case LOCKED: buttonStyle = "locked"; break;
//					case UNLOCKED: buttonStyle = "unlocked"; break;
//					case ACCOMPLISHED: buttonStyle = "accomplished"; break;
//					}
//			
//			// Crée le bouton avec comme UserObject le nom du properties
//			Button startMission = new Button(uiSkin, buttonStyle);
//			startMission.setPosition(x, y);
//			startMission.setSize(32, 32);
//			startMission.setUserObject(file.path());
//			startMission.addListener(launchMissionListener);
//			stage.addActor(startMission);
//		}
		
		loadObjects("battlefields/world_map.svg");
	}
	
	/**
	 * Charge les objets définis dans le SVG et les ajoute à la carte
	 * @param string
	 */
	public void loadObjects(String svgFile) {
		// Parsing du SVG
		SVGParse parser = new SVGParse(Gdx.files.internal(svgFile));
		SVGRootElement root = new SVGRootElement();
		parser.parse(root);
		
		// Exploitation des données
		WorldMapObjectsLoader loader = new WorldMapObjectsLoader(this);
		loader.load(root);
	}

	/**
	 * Appelée depuis le chargeur SVG lorsqu'un bouton de lancement de mission doit être positionné
	 * sur la carte
	 * @param centerX
	 * @param centerY
	 * @param battlefieldPropertiesFilePath
	 */
	public void createMissionButton(float centerX, float centerY, String battlefieldPropertiesFilePath) {
		Button startMission = new Button(getSkin(), "default");
		startMission.setPosition(centerX, centerY, Align.center);
		startMission.setSize(32, 32);
		startMission.setUserObject(battlefieldPropertiesFilePath);
		startMission.addListener(launchMissionListener);
		getStage().addActor(startMission);
		
		// S'il n'y a pas de fichier properties spécifié, on désactive le bouton. Cela ne devrait pas arriver.
		if (battlefieldPropertiesFilePath == null
		|| battlefieldPropertiesFilePath.isEmpty()) {
			startMission.setDisabled(true);
		}
	};
}
