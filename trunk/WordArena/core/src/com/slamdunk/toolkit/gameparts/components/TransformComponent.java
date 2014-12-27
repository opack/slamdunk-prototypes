package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.math.Vector3;


public class TransformComponent extends Component {
	public Vector3 position;
	public Vector3 rotation;
	public Vector3 scale;
	
	/**
	 * Ancre pour indiquer par rapport à quel
	 * point la position est exprimée.
	 * En % de la taille de l'image sur chaque axe
	 */
	public Vector3 anchor;
	
	/**
	 * Origine relative par rapport au coin bas gauche,
	 * et en % de la taille de l'image sur chaque axe,
	 * utilisée pour les opérations de scale et rotation.
	 */
	public Vector3 origin;
	
	public TransformComponent() {
		position = new Vector3(0, 0, 0);
		rotation = new Vector3(0, 0, 0);
		scale = new Vector3(1, 1, 1);
		anchor = new Vector3(0, 0, 0);
		origin = new Vector3(0, 0, 0);
	}
}
