package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MyGestureHandler implements GestureListener {

	public float initialScale = 1.0f;
	public OrthographicCamera camera;

	public MyGestureHandler(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {

		initialScale = camera.zoom;

		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {

		// Calculate pinch to zoom
		float ratio = initialDistance / distance;

		// Clamp range and set zoom
		camera.zoom = MathUtils.clamp(initialScale * ratio, 0.1f, 1.0f);

		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

}