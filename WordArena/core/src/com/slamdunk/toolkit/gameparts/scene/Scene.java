package com.slamdunk.toolkit.gameparts.scene;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.ObservationPoint;

/**
 * Contient tous les GameObjects d'une scène du jeu
 */
public class Scene implements Screen {
	public static final float DEFAULT_PHYSICS_FIXED_STEP = 1/80f;
	public static final float DEFAULT_PHYSICS_MAX_STEP = 1/4f;
	public static final float DEFAULT_PHYSICS_TIME_SCALE = 1f;
	private static final String DEFAULT_LAYER_NAME = "default";
	
	public List<Layer> layers;
	public ObservationPoint observationPoint;
	public GameObject root;
	private Batch drawBatch;
	
	/**
	 * Interval fixe de temps auquel sont effectués les calculs
	 * de la physique
	 */
	public float physicsFixedStep;
	
	/**
	 * Temps maximal accordé à un pas de calculs physiques. Si le temps
	 * entre 2 appels physiques dépasse cette valeur, alors c'est elle
	 * qui est utilisé afin de laissé au processeur le temps de rattraper
	 * son retard. Cela signifie qu'on fera pour un instant moins d'itérations
	 * de calculs physiques qu'on aurait du. 
	 */
	public float physicsMaxStep;
	
	/**
	 * La vitesse à laquelle le temps avance pour les calculs physiques.
	 * Par exemple : 0.5 signifie que le temps avance moitié moins vite,
	 * 2 indique que le temps avance 2 fois plus vite.
	 */
	public float physicsTimeScale;
	
	private float accumulator;	
	
	public Scene(int width, int height) {
		physicsFixedStep = DEFAULT_PHYSICS_FIXED_STEP;
		physicsMaxStep = DEFAULT_PHYSICS_MAX_STEP;
		physicsTimeScale = DEFAULT_PHYSICS_TIME_SCALE;
		
		drawBatch = new SpriteBatch();
		
		// Ajoute un GameObject racine
		root = new GameObject();
		
		// Ajoute un GameObject ObservationPoint à la scène
		observationPoint = new ObservationPoint();
		observationPoint.camera.viewportWidth = width;
		observationPoint.camera.viewportHeight = height;
		root.addChild(observationPoint);
		
		// Ajoute une couche par défaut
		layers = new ArrayList<Layer>();
		addLayer(DEFAULT_LAYER_NAME);
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
		Layer layer = root.addChild(Layer.class);
		layer.name = name;
		layer.scene = this;
		layers.add(index, layer);
		return index;
	}
	
	public <T extends GameObject> T addGameObject(Class<T> gameObjectClass) {
		return addGameObject(gameObjectClass, 0);
	}
	
	public <T extends GameObject> T addGameObject(Class<T> gameObjectClass, String layerName) {
		int index = getLayerIndex(layerName);
		if (index > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer with name " + layerName + ".");
		}
		return addGameObject(gameObjectClass, index);		
	}
	
	public <T extends GameObject> T addGameObject(Class<T> gameObjectClass, int layerIndex) {
		if (layerIndex < 0
		|| layerIndex > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer at index " + layerIndex + ". Max layer index = " + (layers.size() - 1));
		}
		Layer layer = layers.get(layerIndex);
		return layer.addChild(gameObjectClass);
	}
	
	public GameObject addGameObject(GameObject gameObject) {
		return addGameObject(gameObject, 0);
	}

	public GameObject addGameObject(GameObject gameObject, String layerName) {
		int index = getLayerIndex(layerName);
		if (index > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer with name " + layerName + ".");
		}
		return addGameObject(gameObject, index);		
	}
	
	public GameObject addGameObject(GameObject gameObject, int layerIndex) {
		if (layerIndex < 0
		|| layerIndex > layers.size() - 1) {
			throw new IllegalArgumentException("There is no layer at index " + layerIndex + ". Max layer index = " + (layers.size() - 1));
		}
		Layer layer = layers.get(layerIndex);
		return layer.addChild(gameObject);
	}
	
	/**
	 * Charge une scène depuis un fichier
	 */
	public void load(String file) {
		// TODO
	}
	
	public void init() {
		root.init();
	}
	
	@Override
	public void render(float deltaTime) {
    	// Application de la logique du jeu
    	applyGameLogic(deltaTime);
    
	    // Dessin de la scène
	    renderScene();
	    
	    // Calcul de la physique
	    computePhysics(deltaTime);
	}

	private void applyGameLogic(float deltaTime) {
		root.update(deltaTime);
		root.lateUpdate();
	}

	private void renderScene() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		drawBatch.begin();
		drawBatch.setProjectionMatrix(observationPoint.camera.getProjectionMatrix());
		root.render(drawBatch);
		drawBatch.end();
	}
	
	private void computePhysics(float deltaTime) {
		// S'assure que le temps de la frame est au pire de physicsMaxStep, de façon
		// à donner une chance au CPU de rattraper son éventuel retard en faisant
		// du coup moins d'itérations de calculs de physique
		float frameTime = Math.min(deltaTime * physicsTimeScale, physicsMaxStep);
		
	    accumulator += frameTime;
	    while (accumulator >= physicsFixedStep) {
	    	// Calcul de la physique pour 1 pas
    		root.physics(physicsFixedStep);
		    
		    // On a 1 pas en moins dans le temps écoulé
		    accumulator -= physicsFixedStep;
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
