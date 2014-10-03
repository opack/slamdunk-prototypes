package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;

/**
 * Représente une couche qui peut être affichée par un SlamScreen. C'est dans cette couche
 * qu'on retrouvera les éléments du jeu.
 */
public interface SlamOverlay {
	/**
	 * Méthode appelée périodiquement pour mettre à jour la logique du jeu liée à cette couche
	 * (déplacement de personnages, évènements météo...)
	 * @param delta
	 */
	void act(float delta);
	
	/**
	 * Méthode appelée à chaque render pour dessinée la couche
	 */
	void draw();
	
	/**
	 * Méthode chargée de nettoyer les ressources allouées pour la couche
	 */
	void dispose();

	/**
	 * Indique si l'overlay souhaite recevoir les inputs
	 * @return
	 */
	boolean isProcessInputs();

	/**
	 * Renvoie le processor chargé de recevoir les inputs
	 * @return
	 */
	InputProcessor getInputProcessor();
}
