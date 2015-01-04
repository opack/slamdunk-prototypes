package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	
	public void render(Batch drawBatch, ShapeRenderer shapeRenderer) {
		if (!visible) {
			return;
		}
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
				gameObject.render(drawBatch, shapeRenderer);
			}
		}
	}
	
	/**
	 * Retourne le GameObject le plus proche de l'écran et se trouvant
	 * aux coordonnées indiquées
	 * @param x
	 * @param y
	 * @return
	 */
	public GameObject hit(float x, float y) {
		GameObject gameObject = null;
		for (int depth = tmpDepthSortedObjects.size() - 1; depth > -1; depth--) {
			gameObject = tmpDepthSortedObjects.get(depth);
			if (gameObject.isAt(x, y)) {
				break;
			}
		}
		return gameObject;
	}

	public boolean touchDown(float x, float y, int pointer, int button) {
		// Demande à chaque GameObject, par ordre de profondeur, s'il est
		// intéressé par ce touchDown
		GameObject gameObject;
		for (int depth = tmpDepthSortedObjects.size() - 1; depth > -1; depth--) {
			gameObject = tmpDepthSortedObjects.get(depth);
			if (gameObject.isAt(x, y)
			&& gameObject.touchDown(x, y, pointer, button)) {
				scene.touchFocus = gameObject;
				return true;
			}
		}
		return false;
	}
	
	public boolean touchDragged(float x, float y, int pointer) {
		// Demande à chaque GameObject, par ordre de profondeur, s'il est
		// intéressé par ce touchUp
		GameObject gameObject;
		for (int depth = tmpDepthSortedObjects.size() - 1; depth > -1; depth--) {
			gameObject = tmpDepthSortedObjects.get(depth);
			if (gameObject.isAt(x, y)
			&& gameObject.touchDragged(x, y, pointer)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean touchUp(float x, float y, int pointer, int button) {
		// Demande à chaque GameObject, par ordre de profondeur, s'il est
		// intéressé par ce touchUp
		GameObject gameObject;
		for (int depth = tmpDepthSortedObjects.size() - 1; depth > -1; depth--) {
			gameObject = tmpDepthSortedObjects.get(depth);
			if (gameObject.isAt(x, y)
			&& gameObject.touchUp(x, y, pointer, button)) {
				return true;
			}
		}
		return false;
	}
}
