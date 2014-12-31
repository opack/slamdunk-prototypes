package com.slamdunk.toolkit.gameparts.creators;

import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObjectManager;

public class GameObjectFactory {
	/**
	 * Crée un simple GameObject.
	 * Un tel GameObject a déjà un composant TransformComponent
	 * @return
	 */
	public static GameObject create() {
		return create(GameObject.class);
	}
	
	/**
	 * Crée un GameObject de la classe indiquée
	 * @param gameObjectClass
	 * @return
	 */
	public static <T extends GameObject> T create(Class<T> gameObjectClass) {
		// Crée le GameObject
		T gameObject = null;
		try {
			gameObject = gameObjectClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Creation of GameObject " + gameObjectClass + " is impossible due to InstantiationException", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Creation of GameObject " + gameObjectClass + " is impossible due to IllegalAccessException", e);
		}
		
		// Enregistre le GameObject auprès du manager
		GameObjectManager.getInstance().register(gameObject);
		
		// Retourne le résultat
		return gameObject;
	}
}
