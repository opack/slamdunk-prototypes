package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ZoomInputProcessor implements InputProcessor {

	private OrthographicCamera camera;
	
	public ZoomInputProcessor(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	@Override
	public boolean scrolled(int amount) {

		// Zoom out
		if (amount > 0 && camera.zoom < 1) {
			camera.zoom += 0.1f;
		}

		// Zoom in
		if (amount < 0 && camera.zoom > 0.4) {
			camera.zoom -= 0.1f;
		}

		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

}