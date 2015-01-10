package com.slamdunk.wordarena.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Contient les bornes de l'entité, donc sa taille et sa position (position mise à
 * jour par le BoundsSystem en utilisant le TransformComponent)
 */
public class BoundsComponent extends Component {
	public final Rectangle bounds = new Rectangle();
}
