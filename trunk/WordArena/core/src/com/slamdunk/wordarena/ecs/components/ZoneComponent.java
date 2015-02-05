package com.slamdunk.wordarena.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Une zone de l'arène
 */
public class ZoneComponent extends Component {
	/**
	 * Le joueur qui contrôle la zone
	 */
	public String owner = "";
}
