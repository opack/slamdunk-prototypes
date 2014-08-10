package com.slamdunk.toolkit.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Diff�rentes m�thodes utilitaires li�es au monde
 * @author didem93n
 *
 */
public class WorldUtils {

	/**
	 * Cr�e un Rectangle faisant le tour de l'acteur.
	 * @param actor
	 * @return
	 */
	public static Rectangle computeBounds(Actor actor) {
		return new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
	}
	
	/**
	 * Remplit le rectangle objectToFill avec les coordonn�es et la taille de l'acteur.
	 * @param actor
	 * @param objectToFill
	 * @return
	 */
	public static Rectangle computeBounds(Actor actor, Rectangle objectToFill) {
		return objectToFill.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
	}

}
