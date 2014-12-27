package com.slamdunk.toolkit.gameparts.gameobjects;

import com.slamdunk.toolkit.gameparts.components.CameraComponent;

/**
 * Point depuis lequel on observe la scène. Cela influe donc sur le rendu
 * visuel mais aussi sonore.
 * Doit ABSOLUMENT être le premier composant ajouté à la scène.
 */
public class ObservationPoint extends GameObject {
	public CameraComponent camera;
	
	public ObservationPoint() {
		super();
		name = "ObservationPoint";
		
		// On a toujours un composant CameraComponent, qui indique
		// quelle portion du monde est vu
		camera = new CameraComponent();
		addComponent(camera);
	}
}
