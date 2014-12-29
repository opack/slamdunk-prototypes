package com.slamdunk.wordarena.gameparts.scenes;

import com.slamdunk.toolkit.gameparts.components.ParticleRendererComponent;
import com.slamdunk.toolkit.gameparts.components.PathComponent;
import com.slamdunk.toolkit.gameparts.components.SpriteRendererComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
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
		observationPoint.transform.relativePosition.x = observationPoint.camera.viewportWidth / 2;
		observationPoint.transform.relativePosition.y = observationPoint.camera.viewportHeight / 2;
		
		// Ajoute une couche à la scène
		addLayer("background", 0);
		
		// Ajoute un sprite à la scène
		Sprite map = new Sprite();
		map.spriteRenderer.spriteFile = "maps/battlefield0.png";
		addGameObject(map, "background");
		
		// Ajoute un château
		addGameObject(Castle.class);
		
		// Ajoute un paladin
		Paladin paladin = addGameObject(Paladin.class);
		paladin.getComponent(SpriteRendererComponent.class).origin.set(0.5f,0.5f); // L'origine des rotations est le centre
		paladin.getComponent(SpriteRendererComponent.class).anchor.set(0.5f,0.5f); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathComponent.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathComponent.class).speed = 50;
		
		GameObject dot = paladin.addChild();
		dot.addComponent(SpriteRendererComponent.class);
		dot.getComponent(SpriteRendererComponent.class).spriteFile = "textures/dot.png";
		dot.transform.relativePosition.set(16, 16, 0);
		dot.addComponent(ParticleRendererComponent.class);
		dot.getComponent(ParticleRendererComponent.class).effectFile = "particles/firebeam.p";
		dot.getComponent(ParticleRendererComponent.class).imagesDirectory = "particles";
	}
}
