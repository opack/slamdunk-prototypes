package com.slamdunk.wordarena.withgameparts.scenes;

import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.withgameparts.prefabs.Castle;
import com.slamdunk.wordarena.withgameparts.prefabs.Paladin;

public class TestScene extends Scene {
	public TestScene() {
		super(800, 480);
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
		addGameObject(paladin);
	}
}
