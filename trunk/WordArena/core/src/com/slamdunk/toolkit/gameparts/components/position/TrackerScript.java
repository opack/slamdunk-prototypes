package com.slamdunk.toolkit.gameparts.components.position;

import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

/**
 * Va sur la cible lorsque celle-ci s'éloigne d'une certaine valeur
 */
public class TrackerScript extends Component {
	public static final float DEFAULT_REACH_TIME = 1f;
	
	/**
	 * Cible
	 */
	public GameObject target;
	/**
	 * Distance qu'on autorise à la cible
	 */
	public float leech;
	
	/**
	 * Temps que met l'objet pour revenir sur la cible
	 */
	public float reachTime;

	private TransformComponent trackerTransform;
	private Vector3 targetPosition;
	
	private Vector3 startPosition;
	private Vector3 currentPosition;
	private Vector3 arrivalPosition;
	
	private float alpha;
	private float totalTime;
	
	private boolean tracking;
	
	@Override
	public void reset() {
		leech = 10;
		reachTime = DEFAULT_REACH_TIME;
		alpha = 0;
		totalTime = 0;
		tracking = false;
	}
	
	@Override
	public void init() {
		trackerTransform = gameObject.transform;
		targetPosition = target.transform.worldPosition;
		startPosition = new Vector3();
		currentPosition = new Vector3();
		arrivalPosition = new Vector3();
	}
	
	@Override
	public void update(float deltaTime) {
		if (!tracking
		&& targetPosition.dst(trackerTransform.worldPosition) > leech) {
			tracking = true;
			alpha = 0;
			totalTime = 0;
			startPosition.set(trackerTransform.worldPosition);
			arrivalPosition.set(targetPosition);
		}
		totalTime += deltaTime;
		alpha = totalTime / reachTime;
	}
	
	@Override
	public void lateUpdate() {
		if (!tracking) {
			return;
		}
		currentPosition.set(startPosition);
		currentPosition.lerp(arrivalPosition, alpha);
		trackerTransform.moveTo(currentPosition.x, currentPosition.y, currentPosition.z);

		if (alpha >= 1) {
			tracking = false;
		}
	}
}
