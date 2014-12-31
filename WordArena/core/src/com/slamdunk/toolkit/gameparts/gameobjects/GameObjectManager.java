package com.slamdunk.toolkit.gameparts.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.gameparts.components.Component;

/**
 * Gère un ensemble de GameObject, permettant des opérations de masse (recherche...).
 * 
 * Chaque GameObject à gérer doit être enregistré au moyen de la méthode {@link #register(GameObject)}.
 * Si les GameObjects sont créés via GameObjectFactory, ils sont automatiquement enregistrés.
 * 
 * Attention ! Les méthode retournant des Listes retournent des liste immuables
 * qui exposent le contenu de listes de travail internes. Si plusieurs appels
 * consécutifs doivent être fait à ces méthodes, attention car les listes
 * retournées verront leur contenu modifier ! Penser donc à faire une copie
 * si nécessaire.
 */
public class GameObjectManager {
	private static GameObjectManager instance = new GameObjectManager();
	
	private List<GameObject> gameObjects;
	
	private GameObjectManager() {
		gameObjects = new ArrayList<GameObject>();
	}
	
	public static GameObjectManager getInstance() {
		return instance;
	}
	
	/**
	 * Retourne le premier GameObject rencontré ayant le nom spécifié
	 * @param name
	 * @return
	 */
	public GameObject find(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name must not be null !");
		}
		for (GameObject gameObject : gameObjects) {
			if (gameObject.name.equals(name)) {
				return gameObject;
			}
		}
		return null;
	}
	
	/**
	 * Retourne le premier GameObject rencontré ayant la classe et le nom spécifiés
	 * @param name
	 * @return
	 */
	public <T extends GameObject> T find(Class<T> gameObjectClass, String name) {
		if (gameObjectClass == null
		|| name == null) {
			throw new IllegalArgumentException("gameObjectClass and name must not be null !");
		}
		for (GameObject gameObject : gameObjects) {
			if (gameObject.getClass().equals(gameObjectClass)) {
				return gameObjectClass.cast(gameObject);
			}
		}
		return null;
	}
	
	/**
	 * Retourne tous les GameObjects de la classe indiquée.
	 * Attention ! La liste retournée est immuable et basée sur une liste interne
	 * permettant de stocker les résultats des requêtes. Son contenu est donc
	 * susceptible de changer au prochain appel à une méthode -All. Faire une
	 * copie si nécessaire.
	 * @param gameObjectClass
	 * @return
	 */
	public <T extends GameObject> List<T> findAll(Class<T> gameObjectClass){
		if (gameObjectClass == null) {
			throw new IllegalArgumentException("gameObjectClass must not be null !");
		}
		final List<T> result = new ArrayList<T>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject.getClass().equals(gameObjectClass)) {
				result.add(gameObjectClass.cast(gameObject));
			}
		}
		return result;
	}
	
	/**
	 * Retourne tous les GameObjects ayant au moins tous les composants indiqués.
	 * Attention ! La liste retournée est immuable et basée sur une liste interne
	 * permettant de stocker les résultats des requêtes. Son contenu est donc
	 * susceptible de changer au prochain appel à une méthode -All. Faire une
	 * copie si nécessaire.
	 * @param componentClasses
	 * @return
	 */
	public List<GameObject> findAllWithComponents(Class<? extends Component>... componentClasses) {
		if (componentClasses == null) {
			throw new IllegalArgumentException("componentClasses must not be null !");
		}
		final List<GameObject> result = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject.hasComponents(componentClasses)) {
				result.add(gameObject);
			}
		}
		return result;
	}
	
	/**
	 * Enregistre le GameObject pour qu'il soit géré par le manager
	 * @param gameObject
	 */
	public void register(GameObject gameObject) {
		gameObjects.add(gameObject);
	}
}
