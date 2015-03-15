package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector3;

public class RenderableEllipse extends Ellipse implements RenderableShape {
	private static final long serialVersionUID = -4889164532931689624L;

	public ShapeType shapeType;
	public Color color;
	
	public RenderableEllipse() {
		shapeType = ShapeType.Line;
	}
	
	public RenderableEllipse(float x, float y, float width, float height) {
		super(x, y, width, height);
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
		
		renderer.ellipse(origin.x + x, origin.y + y, width, height);
	}
}
