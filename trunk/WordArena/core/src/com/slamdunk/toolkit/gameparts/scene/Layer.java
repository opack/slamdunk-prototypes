package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class Layer extends GameObject {
	public String name;
	
	public Scene scene;
	
	/**
	 * Indique si la couche est visible, donc si render() doit être appelé
	 * sur les GameObjects de cette couche.
	 * Cela permet d'avoir une couche invisible mais active.
	 */
	public boolean visible;
	
	private List<GameObject> tmpDepthSortedObjects;
	
	public Layer() {
		tmpDepthSortedObjects = new ArrayList<GameObject>();
		
		// Par défaut, la couche est active et visible
		visible = true;
	}
	
	@Override
	public <T extends GameObject> T addChild(T child) {
		super.addChild(child);
		tmpDepthSortedObjects.add(child);
		return child;
	}
	
	public void render(Batch drawBatch) {
		// Classe les gameObjects par z croissant pour commencer le rendu
		// par ceux qui sont le plus au fond
		Collections.sort(tmpDepthSortedObjects, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject go1, GameObject go2) {
				return Float.compare(go1.transform.worldPosition.z, go2.transform.worldPosition.z);
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
