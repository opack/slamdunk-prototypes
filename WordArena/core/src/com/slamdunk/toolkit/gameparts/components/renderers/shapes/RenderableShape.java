package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public interface RenderableShape {
	/**
	 * Dessine la scène en utilisant les coordonnées indiquées comme
	 * point d'origine
	 * @param origin
	 * @param renderer
	 */
	void render(Vector3 origin, ShapeRenderer renderer);
}
