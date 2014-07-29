package com.slamdunk.toolkit.world;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Le monde du jeu. Ce monde est compos� de plusieurs couches.
 * Le monde est capable de retrouver un objet parmi les couches,
 * de savoir si deux objets se touchent (m�me � travers plusieurs
 * couches)...
 */
public class SlamWorld extends Group {
	/**
	 * Acc�s rapide aux diff�rentes couches
	 */
	private List<SlamWorldLayer> layers;
	
	/**
	 * Compte le temps �coul�
	 */
	private float secondsElapsed;
	private float startTime;
	
	public SlamWorld(float posX, float posY, float worldWidth, float worldHeight) {
		super();
		setSize(worldWidth, worldHeight);
		setPosition(posX, posY);
	}
	
	public float getSecondsElapsed() {
		return secondsElapsed;
	}

	public void addLayer(SlamWorldLayer layer) {
		// Chaque couche occupe la totalit� du monde
		layer.setSize(getWidth(), getHeight());
		layer.setPosition(0, 0);
		
		layers.add(layer);
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
}
