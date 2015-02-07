package com.slamdunk.wordarena_ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Définit une zone de collision qui peut être utilisée pour des
 * besoins variés (détection de touche, collision entre objets du
 * jeu...).
 * Une zone de collision est composée de bornes dont on définit la
 * taille et dont la position sera ajustée par le ColliderSystem
 * en fonction :
 *   - du TransformComponent de l'entité
 *   - de la relativeOrigin définie dans le collider et permettant
 *   de positionner la zone de collision par rapport à la position
 *   de l'entité (comme un offset)
 */
public class ColliderComponent extends Component {
	/**
	 * Décalage par rapport au x;y de l'entité
	 */
	public final Vector2 relativeOrigin = new Vector2();
	public final Rectangle bounds = new Rectangle();
	// TODO Ajouter des listeners lorsqu'il y a collision
}
