package com.slamdunk.wordarena.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ColliderComponent extends Component {
	/**
	 * Décalage par rapport au x;y de l'entité
	 */
	public final Vector2 relativeOrigin = new Vector2();
	public final Rectangle bounds = new Rectangle();
	// TODO Ajouter des listeners lorsqu'il y a collision
}
