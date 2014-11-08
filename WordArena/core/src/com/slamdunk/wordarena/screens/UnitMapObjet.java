package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slamdunk.toolkit.world.SlamActor;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.pathfinder.PathCursor;
import com.slamdunk.toolkit.world.point.Point;

/**
 * Une unité lambda
 */
public class UnitMapObjet extends SlamActor {

	private float speed;
	private PathCursor pathCursor;
	
	public UnitMapObjet() {
		speed = 1;
	}

	public float getSpeed() {
		return speed;
	}

	/**
	 * Définit la vitesse de l'acteur, en unités du monde par seconde
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Fait suivre le chemin. Si path == null , alors
	 * l'acteur ne suit plus aucun chemin.
	 * @param path
	 */
	public void follow(Path path) {
		if (path == null) {
			pathCursor = null;
		} else {
			pathCursor = new PathCursor(path);
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		// S'il reste un chemin à suivre, on le suit
		if (pathCursor != null && getActions().size == 0 && !pathCursor.isArrivalReached()) {
			Point nextPosition = pathCursor.next();
			addAction(Actions.moveTo(nextPosition.getX(), nextPosition.getY(), 1 / speed));
		}
	}

}
