package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

public class RenderablePolygon extends Polygon implements RenderableShape {

	public ShapeType shapeType;
	public Color color;
	
	public RenderablePolygon() {
		shapeType = ShapeType.Line;
	}
	
	public RenderablePolygon (float[] vertices) {
		super(vertices);
		shapeType = ShapeType.Line;
	}
	
	@Override
	public void render(Vector3 origin, ShapeRenderer renderer) {
		if (origin.x != getOriginX()
		|| origin.y != getOriginY()) {
			setOrigin(origin.x, origin.y);
		}
		
		if (shapeType == null) {
			throw new IllegalArgumentException("Missing shapeType parameter");
		}
		renderer.set(shapeType);
		
		if (color != null) {
			renderer.setColor(color);
		}
		
		renderer.polygon(getTransformedVertices());
	}
}
