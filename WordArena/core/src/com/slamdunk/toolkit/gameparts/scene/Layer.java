package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class Layer {
	public String name;
	
	public boolean active;
	
	/**
	 * Indique si la couche est visible, donc si render() doit être appelé
	 * sur les GameObjects de cette couche
	 */
	public boolean visible;
	
	public List<GameObject> gameObjects;
	
	public Layer(String name) {
		this.name = name;
		gameObjects = new ArrayList<GameObject>();
		
		// Par défaut, la couche est active et visible
		active = true;
		visible = true;
	}
	
	public void addGameObject(GameObject gameObject) {
		gameObject.layer = this;
		gameObjects.add(gameObject);
	}
	
	public void init() {
		for (GameObject gameObject : gameObjects) {
			gameObject.init();
		}
	}
	
	public void update(float deltaTime) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.active) {
				gameObject.update(deltaTime);
			}
		}
	}

	public void render(Batch drawBatch) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.active) {
				gameObject.render(drawBatch);
			}
		}
	}

	public boolean containsClass(Class<? extends GameObject> clazz) {
		for (GameObject curGameObject : gameObjects) {
			if (curGameObject.getClass() == clazz) {
				return true;
			}
		}
		return false;
	}
}
