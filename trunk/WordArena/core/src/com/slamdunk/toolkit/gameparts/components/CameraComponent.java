package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.slamdunk.toolkit.gameparts.components.position.TransformComponent;

/**
 * Représente l'oeil à travers lequel on voit la scène.
 * Doit ABSOLUMENT être le premier composant ajouté au GameObject.
 */
public class CameraComponent extends Component {
	public static final int DEFAULT_VIEWPORT_WIDTH = 800;
	public static final int DEFAULT_VIEWPORT_HEIGHT = 480;
	
	public float zoom;
	
	public int viewportWidth;
	
	public int viewportHeight;
	
	private OrthographicCamera orthoCam;
	
	private TransformComponent transform;
	
	@Override
	public void reset() {
		zoom = 1f;
		viewportWidth = DEFAULT_VIEWPORT_WIDTH;
		viewportHeight = DEFAULT_VIEWPORT_HEIGHT;
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformComponent.class);
		
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
}
