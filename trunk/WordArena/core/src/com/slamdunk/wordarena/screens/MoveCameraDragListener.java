package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
	
	/**
	 * Rectangle dans lequel doit rester la caméra.
	 * Utile pour s'assurer que le monde reste toujours visible.
	 */
	private Rectangle bounds;
	
	public MoveCameraDragListener(Camera camera) {
		this.camera = camera;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void dragStart(InputEvent event, float x, float y, int pointer) {
		previousDragPos.x = x;
		previousDragPos.y = y;
	};
	
	public void drag(InputEvent event, float x, float y, int pointer) {
		camera.position.x += previousDragPos.x - x;
		camera.position.y += previousDragPos.y - y;
		
		if (bounds != null) {
			camera.position.x = MathUtils.clamp(camera.position.x, bounds.x, bounds.x + bounds.width);
			camera.position.y = MathUtils.clamp(camera.position.y, bounds.y, bounds.y + bounds.height);
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	};
}
