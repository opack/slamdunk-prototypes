package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

/**
 * Conventions de visibilité :
 * 	- public : propriétés qui peuvent être éditées pour manipuler le composant.
 *  - protected : champs qui peuvent être nécessaires à d'autres composants,
 *  mais qui sont normalement utilisés principalement par le composant lui-même.
 *  - private : variables de travail du composant et instances vers d'autres
 *  composants
 */
public abstract class Component {
	/**
	 * Indique si le Component doit être unique dans le GameObject.
	 * Si true, une erreur sera levée si un autre Component de cette
	 * classe tente d'être ajouté.
	 */
	public static boolean unique;
	
	public GameObject gameObject;
	
	/**
	 * Indique si le composant est actif
	 */
	public boolean active;
	
	public Component() {
		// Par défaut, le gameObject est actif
		active = true;
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	/**
	 * Méthode appelée 1 fois lors de l'initialisation du GameObject.
	 * Cette méthode est utilise notamment si un composant doit utiliser
	 * d'autres composants, car à cette étape tous les composants
	 * sont déjà instanciés (sauf si d'autres sont ajoutés dynamiquement par
	 * la suite). Attention cependant car tous n'ont pas forcément été
	 * initialisés ! Cette méthode est notamment pratique pour stocker
	 * les références des autres composants pour éviter un appel à
	 * getComponent() à chaque update().
	 */
	public void init() {
	}
	
	/**
	 * Méthode appelée périodiquement pour mettre à jour le composant
	 */
	public void update(float deltaTime) {
	}
	
	/**
	 * Méthode appelée à chaque frame pour dessiner le composant
	 * @param drawBatch
	 */
	public void render(Batch batch) {
	}
}
