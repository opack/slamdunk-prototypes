package com.slamdunk.toolkit.gameparts.components.position;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.CursorMode;

/**
 * Si les variables publiques sont utilisées à runtime pour piloter le script,
 * un appel doit être fait à updateCursor() pour que les modifications soient
 * prises en compte.
 */
public class PathFollowerScript extends Component {
	public boolean paused;
	public ComplexPath path;
	public float position;
	public CursorMode browseMode;
	public float speed;
	
	private ComplexPathCursor cursor;
	private Vector2 tmp;
	private Vector3 transformPosition;

	@Override
	public void reset() {
		position = 0f;
		paused = false;
		speed = 50;
		path = null;
		browseMode = CursorMode.FORWARD;
		cursor = null;
	}

	@Override
	public void init() {
		if (path == null) {
			throw new IllegalStateException(
					"PathComponent cannot work properly : path must be provided");
		}
		if (browseMode == null) {
			throw new IllegalStateException(
					"PathComponent cannot work properly : cursorMode must be provided");
		}
		if (speed < 0) {
			throw new IllegalStateException(
					"PathComponent cannot work properly : speed must be positive");
		}
		tmp = new Vector2();
		transformPosition = gameObject.getComponent(TransformPart.class).worldPosition;

		cursor = new ComplexPathCursor(path, speed, browseMode);
		cursor.setGlobalPosition(position);
	}

	@Override
	public void physics(float deltaTime) {
		if (paused) {
			return;
		}
		// Avance l'unité
		cursor.move(deltaTime, tmp);
		transformPosition.set(tmp.x, tmp.y, 0);
		
		// Met à jour les variables pour une éventuelle lecture
		position = cursor.getGlobalPosition();
	}
	
	/**
	 * Cette méthode doit être appelée immédiatement après avoir mis à jour
	 * au moins une des variables suivantes : speed, path, browseMode ou position.
	 */
	public void updateCursor() {
		// Attention ! L'ordre de ces méthodes est important !
		
		if (cursor.getMode() != browseMode) {
			cursor.setMode(browseMode);
		}
		if (cursor.getSpeed() != speed) {
			cursor.setSpeed(speed);
		}
		if (cursor.getPath() != path) {
			cursor.setPath(path);
		}
		if (cursor.getGlobalPosition() != position) {
			cursor.setGlobalPosition(position);
		}
	}
}
