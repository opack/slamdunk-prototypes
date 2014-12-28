package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.wordarena.ai.States;

public class PathComponent extends Component {
	public ComplexPath path;
	
	public float startPosition = 0f;
	
	public CursorMode cursorMode = CursorMode.FORWARD;
	
	public float speed = 1f;
	
	public boolean paused = false;
	
	private ComplexPathCursor cursor;
	
	private Vector2 tmpMoveCurrent;
	
	private Vector2 tmpMoveDestination;
	
	private Vector3 transformPosition;
	
	private AnimationControllerComponent animationControllerComponent;
	
	@Override
	public void init() {
		if (path == null) {
			throw new IllegalStateException("PathComponent cannot work properly : path must be provided");
		}
		if (cursorMode == null) {
			throw new IllegalStateException("PathComponent cannot work properly : cursorMode must be provided");
		}
		if (speed == 0) {
			throw new IllegalStateException("PathComponent cannot work properly : speed must be positive");
		}
		tmpMoveCurrent = new Vector2();
		tmpMoveDestination = new Vector2();
		transformPosition = gameObject.getComponent(TransformComponent.class).position;
		animationControllerComponent = gameObject.getComponent(AnimationControllerComponent.class);
		
		// Récupère l'indice du segment correspondant à ce t
		int segmentIndex = path.getSegmentIndexFromGlobalT(startPosition);
		
		// Récupère la valeur de t localisée à ce segment
		float localT = path.convertToLocalT(startPosition, segmentIndex);
		
		// Crée le curseur et le positionne à cet endroit
		cursor = new ComplexPathCursor(path, speed, cursorMode);
		cursor.setCurrentSegmentIndex(segmentIndex);
		cursor.setPosition(localT);
	}
	
	@Override
	public void physics(float deltaTime) {
		tmpMoveCurrent.set(transformPosition.x, transformPosition.y);

		// Avance l'unité
		cursor.move(deltaTime, tmpMoveDestination);
		transformPosition.set(tmpMoveDestination.x, tmpMoveDestination.y, 0);
	}
	
	@Override
	public void update(float deltaTime) {
		// Change la direction du gameObject
		if (animationControllerComponent != null) {
			animationControllerComponent.parameters.put("Direction", Directions4.getDirection(tmpMoveCurrent, tmpMoveDestination));
			animationControllerComponent.parameters.put("Action", States.MOVING);
		}
	}
}
