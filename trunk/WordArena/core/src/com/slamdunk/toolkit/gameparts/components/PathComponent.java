package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.CursorMode;

public class PathComponent extends Component {
	public ComplexPath path;
	
	public float startPosition;
	
	public CursorMode cursorMode;
	
	public float speed;
	
	public boolean paused;
	
	private ComplexPathCursor cursor;
	
	private Vector2 tmp;
	
	private Vector3 transformPosition;
	
	@Override
	public void reset() {
		path = null;
		startPosition = 0f;
		cursorMode = CursorMode.FORWARD;
		speed = 1f;
		paused = false;
	}
	
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
		tmp = new Vector2();
		transformPosition = gameObject.getComponent(TransformComponent.class).worldPosition;
		
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
		// Avance l'unité
		cursor.move(deltaTime, tmp);
		transformPosition.set(tmp.x, tmp.y, 0);
	}
}
