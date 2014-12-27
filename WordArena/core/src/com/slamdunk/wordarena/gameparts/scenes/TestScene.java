package com.slamdunk.wordarena.gameparts.scenes;

import com.slamdunk.toolkit.gameparts.components.PathComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.gameparts.SVGLoader;
import com.slamdunk.wordarena.gameparts.prefabs.Castle;
import com.slamdunk.wordarena.gameparts.prefabs.Paladin;

public class TestScene extends Scene {
	public TestScene() {
		super(800, 480);
		// Charge le SVG contenant le level design
		SVGLoader sceneLoader = new SVGLoader();
		sceneLoader.load("battlefields/battlefield0.svg");
		
		// Place la caméra au centre de l'écran
		observationPoint.transform.position.x = observationPoint.camera.viewportWidth / 2;
		observationPoint.transform.position.y = observationPoint.camera.viewportHeight / 2;
		
		// Ajoute une couche à la scène
		addLayer("background", 0);
		
		// Ajoute un sprite à la scène
		Sprite map = new Sprite();
		map.spriteRenderer.spriteFile = "maps/battlefield0.png";
		addGameObject(map, "background");
		
		// Ajoute un château
		Castle castle = new Castle();
		addGameObject(castle);
		
		// Ajoute un paladin
		Paladin paladin = new Paladin();
		paladin.transform.origin.set(0.5f,0.5f,0); // L'origine des rotations est le centre
		paladin.transform.anchor.set(0.5f,0.5f,0); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathComponent.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathComponent.class).speed = 50;
		addGameObject(paladin);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
	}
}
