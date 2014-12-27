package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class Layer {
	public String name;
	
	public Scene scene;
	
	public boolean active;
	
	/**
	 * Indique si la couche est visible, donc si render() doit être appelé
	 * sur les GameObjects de cette couche
	 */
	public boolean visible;
	
	public List<GameObject> gameObjects;
	
	private List<GameObject> tmpDepthSortedObjects;
	
	public Layer(String name) {
		this.name = name;
		gameObjects = new ArrayList<GameObject>();
		tmpDepthSortedObjects = new ArrayList<GameObject>();
		
		// Par défaut, la couche est active et visible
		active = true;
		visible = true;
	}
	
	public void addGameObject(GameObject gameObject) {
		gameObject.layer = this;
		gameObjects.add(gameObject);
		tmpDepthSortedObjects.add(gameObject);
	}
	
	public void init() {
		for (GameObject gameObject : gameObjects) {
			gameObject.init();
		}
	}
	
	public void physics(float deltaTime) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.active) {
				gameObject.physics(deltaTime);
			}
		}
	}
	
	public void update(float deltaTime) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.active) {
				gameObject.update(deltaTime);
			}
		}
	}
	
	public void lateUpdate() {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.active) {
				gameObject.lateUpdate();
			}
		}
	}

	public void render(Batch drawBatch) {
		// Classe les gameObjects par z croissant pour commencer le rendu
		// par ceux qui sont le plus au fond
		Collections.sort(tmpDepthSortedObjects, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject go1, GameObject go2) {
				return Float.compare(go1.transform.position.z, go2.transform.position.z);
			}
		});
		
		// Dessine les objets
		for (GameObject gameObject : tmpDepthSortedObjects) {
			if (gameObject.active) {
				gameObject.render(drawBatch);
			}
		}
	}
}
