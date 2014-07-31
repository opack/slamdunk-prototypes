package com.slamdunk.toolkit.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Le monde du jeu. Ce monde est composé de plusieurs couches.
 * Le monde est capable de retrouver un objet parmi les couches,
 * de savoir si deux objets se touchent (même à travers plusieurs
 * couches)...
 */
public class SlamWorld extends Group {
	/**
	 * Accès rapide aux différentes couches
	 */
	private Map<String, SlamWorldLayer> layers;
	
	/**
	 * Compte le temps écoulé
	 */
	private float secondsElapsed;
	private float startTime;
	
	public SlamWorld() {
		layers = new LinkedHashMap<String, SlamWorldLayer>();
	}
	
	public SlamWorld(float posX, float posY, float worldWidth, float worldHeight) {
		super();
		setSize(worldWidth, worldHeight);
		setPosition(posX, posY);
	}
	
	public float getSecondsElapsed() {
		return secondsElapsed;
	}

	public void addLayer(SlamWorldLayer layer) {
		// Chaque couche occupe la totalité du monde
		layer.setSize(getWidth(), getHeight());
		layer.setPosition(0, 0);
		
		layers.put(layer.getName(), layer);
		addActor(layer);
	}
	
	@Override
	public void act(float delta) {
		if (System.nanoTime() - startTime >= 1000000000) {
			secondsElapsed++;
			startTime = System.nanoTime();
		}
		super.act(delta);
	}
	
	/**
	 * Renvoie le premier acteur de la couche spécifiée qui est
	 * en collision avec l'acteur
	 */
	public Actor resolveCollision(Actor actor, String layerName) {
		SlamWorldLayer layer = layers.get(layerName);
		if (layer == null) {
			return null;
		}
		return layer.resolveCollision(actor);
	}
	
	/**
	 * Renvoie le premier acteur du monde qui est en collision avec
	 * l'acteur. Les couches sont parcourues dans l'ordre d'ajout.
	 */
	public Actor resolveCollision(Actor actor) {
		SlamWorldLayer layer;
		Actor collided = null;
		for (Actor child : getChildren()) {
			layer = (SlamWorldLayer)child;
			collided = layer.resolveCollision(actor);
			if (collided != null) {
				break;
			}
		}
		return collided;
	}
}
