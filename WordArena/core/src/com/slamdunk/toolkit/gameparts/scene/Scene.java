package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.ObservationPoint;

/**
 * Contient tous les GameObjects d'une scène du jeu
 */
public class Scene implements Screen {
	private static final String DEFAULT_LAYER_NAME = "default";
	
	public List<Layer> layers;
	public ObservationPoint observationPoint;
	private Batch drawBatch;
	
	private PhasePlanifier planifier;
	
	public Scene(int width, int height) {
		planifier = new PhasePlanifier(80, 60);
		
		layers = new ArrayList<Layer>();
		layers.add(new Layer(DEFAULT_LAYER_NAME));
		
		drawBatch = new SpriteBatch();
		
		// Ajoute un GameObject ObservationPoint à la scène
		observationPoint = new ObservationPoint();
		observationPoint.camera.viewportWidth = width;
		observationPoint.camera.viewportHeight = height;
		addGameObject(observationPoint);
	}
	
	public int getLayerIndex(String name) {
		int index = -1;
		for (int cur = 0; cur < layers.size(); cur++) {
			if (name.equals(layers.get(cur).name)) {
				index = cur;
				break;
			}
		}
		return index;
	}
	
	public int addLayer(String name) {
		return addLayer(name, layers.size());
	}
	
	/**
	 * Ajoute la couche avec le nom name à l'indice indiqué
	 * @param name
	 * @param index
	 * @return Indice de la couche
	 */
	public int addLayer(String name, int index) {
		int found = getLayerIndex(name);
		if (found != -1) {
			throw new IllegalArgumentException("There is already a layer with name " + name + " at index " + found);
		}
		Layer layer = new Layer(name);
		layers.add(index, layer);
		layer.scene = this;
		return index;
	}
	
	public void addGameObject(GameObject gameObject) {
		addGameObject(gameObject, 0);
	}

	public void addGameObject(GameObject gameObject, String layerName) {
		int index = getLayerIndex(layerName);
		if (index > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer with name " + layerName + ".");
		}
		addGameObject(gameObject, index);		
	}
	
	public void addGameObject(GameObject gameObject, int layerIndex) {
		if (layerIndex < 0
		|| layerIndex > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer at index " + layerIndex + ". Max layer index = " + (layers.size() - 1));
		}
		Layer layer = layers.get(layerIndex);
		layer.addGameObject(gameObject);
	}
	
	/**
	 * Charge une scène depuis un fichier
	 */
	public void load(String file) {
		// TODO
	}
	
	/**
	 * Initialise les GameObjects de la scène
	 */
	public void init() {
		observationPoint.init();
		for (Layer layer : layers) {
			layer.init();
		}
	}
	
	FPSLogger fps = new FPSLogger();
	
	@Override
	public void render(float delta) {
		fps.log();
		// Récupération du temps écoulé depuis le dernier appel
		planifier.update(Gdx.graphics.getRawDeltaTime());
	    
	    // Calcul de la physique
	    if (planifier.physicsTick()) {
	    	System.out.println("PHYSICS - " + planifier.getPhysicsDeltaTime());
		    for (Layer layer : layers) {
		    	if (layer.active) {
					layer.physics(planifier.getPhysicsDeltaTime());
				}
		    }
		}
	    
	    if (planifier.frameTick()) {
	    	System.out.println("FRAME - " + planifier.getFrameDeltaTime());
	    	// Application de la logique du jeu
		    for (Layer layer : layers) {
		    	if (layer.active) {
					layer.update(planifier.getFrameDeltaTime());
				}
		    }
		    for (Layer layer : layers) {
		    	if (layer.active) {
					layer.lateUpdate();
				}
		    }
	    
		    // Dessin de la scène
		    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			drawBatch.begin();
			drawBatch.setProjectionMatrix(observationPoint.camera.getProjectionMatrix());
			for (Layer layer : layers) {
				if (layer.active
				&& layer.visible) {
					layer.render(drawBatch);
				}
			}
			drawBatch.end();
	    }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
