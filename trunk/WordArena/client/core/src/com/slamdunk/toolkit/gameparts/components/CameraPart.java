package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;

/**
 * Représente l'oeil à travers lequel on voit la scène.
 * Doit ABSOLUMENT être le premier composant ajouté au GameObject.
 */
public class CameraPart extends Component {
	public static final int DEFAULT_VIEWPORT_WIDTH = 800;
	public static final int DEFAULT_VIEWPORT_HEIGHT = 480;
	
	public float zoom;
	
	public int viewportWidth;
	
	public int viewportHeight;
	
	private OrthographicCamera orthoCam;
	
	private TransformPart transform;
	
	private Vector3 tmp;
	
	public CameraPart() {
		tmp = new Vector3();
	}
	
	@Override
	public void reset() {
		zoom = 1f;
		viewportWidth = DEFAULT_VIEWPORT_WIDTH;
		viewportHeight = DEFAULT_VIEWPORT_HEIGHT;
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformPart.class);
		
		orthoCam = new OrthographicCamera();
		orthoCam.setToOrtho(false);
		lateUpdate();
	}

	@Override
	public void lateUpdate() {
		orthoCam.position.set(transform.worldPosition);
		orthoCam.viewportWidth = viewportWidth;
		orthoCam.viewportHeight = viewportHeight;
		orthoCam.zoom = zoom;
		orthoCam.update();
	}
	
	public Matrix4 getProjectionMatrix() {
		return orthoCam.combined;
	}

	/**
	 * Transforme les coordonnées écran en coordonnées du monde.
	 * @param screenCoords
	 */
	public void unproject(Vector2 screenCoords) {
		tmp.set(screenCoords.x, screenCoords.y, 1);
		orthoCam.unproject(tmp);
		screenCoords.set(tmp.x, tmp.y);
	}
}
