package com.slamdunk.toolkit.gameparts.gameobjects;

import com.slamdunk.toolkit.gameparts.components.CameraPart;

/**
 * Point depuis lequel on observe la scène. Cela influe donc sur le rendu
 * visuel mais aussi sonore.
 * Doit ABSOLUMENT être le premier composant ajouté à la scène.
 */
public class ObservationPoint extends GameObject {
	public CameraPart camera;
	
	public ObservationPoint() {
		super();
		name = "ObservationPoint";
		
		// On a toujours un composant CameraComponent, qui indique
		// quelle portion du monde est vu
		camera = addComponent(CameraPart.class);
	}
}
