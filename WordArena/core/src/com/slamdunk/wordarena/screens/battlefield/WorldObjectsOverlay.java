package com.slamdunk.wordarena.screens.battlefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.svg.SVGParse;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.wordarena.screens.MoveCameraDragListener;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitFactory;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

public class WorldObjectsOverlay extends WorldOverlay {
	private Array<ComplexPath> paths;
	private MoveCameraDragListener moveCameraListener;
	
	public WorldObjectsOverlay(GameScreen gameScreen) {
		// On crée un Stage en attendant que la méthode init() utilise le bon viewport
		createStage(new FitViewport(800, 480));
		// Crée l'image contenant le fond de carte
		Image background = new Image();
		background.setName("background");
		background.addListener(new SpawnUnitListener(gameScreen));
		getWorld().addActor(background);
		// Initialisation du listener de déplacement de la caméra
		moveCameraListener = new MoveCameraDragListener(getStage().getCamera());
		getStage().addListener(moveCameraListener);
		//
		UnitManager.getInstance().setStageContainer(getWorld());
		// Initalisation de la liste des chemins
		paths = new Array<ComplexPath>();
	}

	public void init(Camera camera, float worldUnitsPerPixel) {
	}
	
	public SimpleUnit spawnUnit(Units unitType, Factions faction, float centerX, float centerY) {
		// Crée l'unité
		SimpleUnit unit = UnitFactory.create((GameScreen)getScreen(), unitType);
		unit.setFaction(faction);
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		unit.setCenterPosition(centerX, centerY);
		
		return unit;
	}
	
	public SimpleUnit spawnUnit(Units unitType, Factions faction, ComplexPath path) {
		// Crée l'unité
		SimpleUnit unit = UnitFactory.create((GameScreen)getScreen(), unitType);
		unit.setFaction(faction);
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		unit.setPath(path);
		
		return unit;
	}
	
	public SimpleUnit spawnUnit(Units unitType, Factions faction, ComplexPath path, String departureExtremity) {
		// Crée l'unité
		SimpleUnit unit = UnitFactory.create((GameScreen)getScreen(), unitType);
		unit.setFaction(faction);
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		unit.setPath(path, departureExtremity);
		
		return unit;
	}
	
	public SimpleUnit spawnUnit(Units unitType, Factions faction, ComplexPath path, float startT, CursorMode cursorMode) {
		// Crée l'unité
		SimpleUnit unit = UnitFactory.create((GameScreen)getScreen(), unitType);
		unit.setFaction(faction);
		
		// Ajoute l'unité au monde
		UnitManager.getInstance().addUnit(unit);
		
		// Envoie l'unité sur le chemin spécifié
		unit.setPath(path, startT, cursorMode);
		
		return unit;
	}

	/**
	 * Définit le fond de carte
	 * @param mapImageFile
	 */
	public void setBackgroundMap(String mapImageFile) {
		Image background = (Image)getWorld().findActor("background");
		TextureRegion region = new TextureRegion(new Texture(mapImageFile));
		background.setDrawable(new TextureRegionDrawable(region));
		background.setPosition(0, 0);
		background.setSize(region.getRegionWidth(), region.getRegionHeight());
		
		// La caméra ne doit pas perdre de vue la carte. On place les limites
		// de déplacements de la caméra (donc du centre de la zone vue)
		moveCameraListener.computeMoveBounds(getStage().getCamera(), background, 20);
	}

	public Array<ComplexPath> getPaths() {
		return paths;
	}

	public void setPaths(Array<ComplexPath> paths) {
		this.paths = paths;
	}

	/**
	 * Charge les objets définis dans le SVG et les ajoute à la carte
	 * @param string
	 */
	public void loadObjects(String svgFile) {
		// Réinitalisation de la liste des chemins
		paths.clear();
		
		// Parsing du SVG
		SVGParse parser = new SVGParse(Gdx.files.internal(svgFile));
		SVGRootElement root = new SVGRootElement();
		parser.parse(root);
		
		// Exploitation des données
		BattlefieldObjectsLoader loader = new BattlefieldObjectsLoader(this);
		loader.load(root);
	}
}