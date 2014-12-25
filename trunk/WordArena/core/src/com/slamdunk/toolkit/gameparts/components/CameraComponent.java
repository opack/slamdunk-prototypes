package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

/**
 * Représente l'oeil à travers lequel on voit la scène.
 * Doit ABSOLUMENT être le premier composant ajouté au GameObject.
 */
public class CameraComponent extends Component {
	public float zoom;
	
	public int viewportWidth;
	
	public int viewportHeight;
	
	protected OrthographicCamera orthoCam;
	
	private TransformComponent transform;
	
	public CameraComponent() {
		orthoCam = new OrthographicCamera();
		orthoCam.setToOrtho(false);
		
		zoom = 1f;
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformComponent.class);
	}

	@Override
	public void update(float deltaTime) {
		orthoCam.position.set(transform.position);
		orthoCam.viewportWidth = viewportWidth;
		orthoCam.viewportHeight = viewportHeight;
		orthoCam.zoom = zoom;
		orthoCam.update();
	}
	
	@Override
	public void render(Batch batch) {
		batch.setProjectionMatrix(orthoCam.combined);
	}
	
	public Matrix4 getProjectionMatrix() {
		return orthoCam.combined;
	}
}
