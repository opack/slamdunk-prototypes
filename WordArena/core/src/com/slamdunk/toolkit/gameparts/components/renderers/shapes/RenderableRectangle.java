package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class RenderableRectangle extends Rectangle implements RenderableShape {
	private static final long serialVersionUID = -288532020796589911L;
	
	public ShapeType shapeType;
	public Color color;
	
	public RenderableRectangle () {
		shapeType = ShapeType.Line;
	}

	public RenderableRectangle (float x, float y, float width, float height) {
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
		
		renderer.rect(origin.x + x, origin.y + y, width, height);
	}
}
