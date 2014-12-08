package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Listener chargé de déplacer la caméra en fonction des drags de
 * souris/doigt
 */
public class MoveCameraDragListener extends DragListener {
	private Camera camera;
	private Vector2 previousDragPos = new Vector2();
	
	public MoveCameraDragListener(Camera camera) {
		this.camera = camera;
	}
	
	public void dragStart(InputEvent event, float x, float y, int pointer) {
		previousDragPos.x = x;
		previousDragPos.y = y;
	};
	
	public void drag(InputEvent event, float x, float y, int pointer) {
		camera.position.x += previousDragPos.x - x;
		camera.position.y += previousDragPos.y - y;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	};
}
