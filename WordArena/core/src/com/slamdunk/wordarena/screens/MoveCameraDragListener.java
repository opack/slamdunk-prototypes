package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	private Rectangle moveBounds;
	
	public MoveCameraDragListener(Camera camera) {
		this.camera = camera;
	}
	
	public Rectangle getMoveBounds() {
		return moveBounds;
	}

	public void setMoveBounds(Rectangle bounds) {
		this.moveBounds = bounds;
	}
	
	/**
	 * Modifie les limites de déplacement de la caméra indiquée de façon à
	 * ce que l'acteur spécifié soit toujours visible.
	 * @param observationPoint
	 * @param alwaysSeenActor
	 * @param margin
	 */
	public void computeMoveBounds(Camera camera, Actor alwaysSeenActor, float margin) {
		if (moveBounds == null) {
			moveBounds = new Rectangle();
		}
		moveBounds.x = (int)(alwaysSeenActor.getX() + camera.viewportWidth / 2 - margin);
		moveBounds.y = (int)(alwaysSeenActor.getY() + camera.viewportHeight / 2 - margin);
		moveBounds.width = (int)(alwaysSeenActor.getWidth() - camera.viewportWidth + 2 * margin);
		moveBounds.height = (int)(alwaysSeenActor.getHeight() - camera.viewportHeight + 2 * margin);
	}

	public void dragStart(InputEvent event, float x, float y, int pointer) {
		previousDragPos.x = x;
		previousDragPos.y = y;
	};
	
	public void drag(InputEvent event, float x, float y, int pointer) {
		camera.position.x += previousDragPos.x - x;
		camera.position.y += previousDragPos.y - y;
		
		if (moveBounds != null) {
			camera.position.x = MathUtils.clamp(camera.position.x, moveBounds.x, moveBounds.x + moveBounds.width);
			camera.position.y = MathUtils.clamp(camera.position.y, moveBounds.y, moveBounds.y + moveBounds.height);
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	};
}
