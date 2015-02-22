package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class RenderableCircle extends Circle implements RenderableShape {
	private static final long serialVersionUID = 3877869291562048077L;

	public ShapeType shapeType;
	public Color color;
	
	public RenderableCircle() {
		shapeType = ShapeType.Line;
	}
	
	public RenderableCircle(float x, float y, float radius) {
		super(x, y, radius);
		shapeType = ShapeType.Line;
	}
	
	@Override
	public void render(Vector3 origin, ShapeRenderer renderer) {
		if (shapeType == null) {
			throw new IllegalArgumentException("Missing shapeType parameter");
		}
		renderer.set(shapeType);
		
		if (color != null) {
			renderer.setColor(color);
		}
		
		renderer.circle(origin.x + x, origin.y + y, radius);
	}
}
