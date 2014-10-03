package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;

/**
 * Repr�sente une couche qui peut �tre affich�e par un SlamScreen. C'est dans cette couche
 * qu'on retrouvera les �l�ments du jeu.
 */
public interface SlamOverlay {
	/**
	 * M�thode appel�e p�riodiquement pour mettre � jour la logique du jeu li�e � cette couche
	 * (d�placement de personnages, �v�nements m�t�o...)
	 * @param delta
	 */
	void act(float delta);
	
	/**
	 * M�thode appel�e � chaque render pour dessin�e la couche
	 */
	void draw();
	
	/**
	 * M�thode charg�e de nettoyer les ressources allou�es pour la couche
	 */
	void dispose();

	/**
	 * Indique si l'overlay souhaite recevoir les inputs
	 * @return
	 */
	boolean isProcessInputs();

	/**
	 * Renvoie le processor charg� de recevoir les inputs
	 * @return
	 */
	InputProcessor getInputProcessor();
}
