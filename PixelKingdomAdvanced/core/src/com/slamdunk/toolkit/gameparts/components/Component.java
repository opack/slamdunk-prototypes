package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	public GameObject gameObject;
	
	/**
	 * Indique si le composant est actif
	 */
	public boolean active;
	
	public Component() {
		// Par défaut, le gameObject est actif
		active = true;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Crée les composants dont dépend ce composant, s'ils n'existent pas
	 * encore. Cette méthode est appelée dès l'ajout du composant au
	 * GameObject
	 */
	public void createDependencies() {
	}

	/**
	 * Replace les valeurs par défaut du composant. Utilisé
	 * pour réinitialiser les variables publiques notamment
	 * avec des valeurs standards.
	 */
	public void reset() {
	}
	
	/**
	 * Méthode appelée 1 fois lors de l'initialisation du GameObject.
	 * Cette méthode est utilisée notamment si un composant doit utiliser
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
	 * Méthode appelée périodiquement pour mettre à jour le composant en
	 * fonction de la logique du jeu, 1 fois par frame
	 */
	public void update(float deltaTime) {
	}
	
	/**
	 * Méthode appelée 1 fois par frame, après que tous les composants
	 * aient effectué leur méthode update(). Cette méthode sert par exemple
	 * à mettre à jour la position d'une caméra chargée de suivre un objet :
	 * le personnage se déplace et tourne dans update(), et la caméra se
	 * déplace et tourne à son tour une fois que tout est calculé, donc dans
	 * postUpdate() ; ainsi on s'assure que le personnage s'est déplacé
	 * complètement avant que la caméra ne suive sa position.
	 */
	public void lateUpdate() {
	}
	
	/**
	 * Méthode appelée à intervalles réguliers pour mettre à jour la physique
	 * du monde (déplacements, gravité...).
	 * @param deltaTime 
	 */
	public void physics(float deltaTime) {
	}
	
	/**
	 * Méthode appelée à chaque frame pour dessiner le composant
	 * @param batch
	 * @param shapeRenderer
	 */
	public void render(Batch batch, ShapeRenderer shapeRenderer) {
	}
}
