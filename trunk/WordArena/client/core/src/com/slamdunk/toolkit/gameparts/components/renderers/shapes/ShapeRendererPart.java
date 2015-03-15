package com.slamdunk.toolkit.gameparts.components.renderers.shapes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;

public class ShapeRendererPart extends Component {
	public Array<RenderableShape> shapes;
	
	private TransformPart transform;
	
	@Override
	public void reset() {
		shapes = new Array<RenderableShape>();
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformPart.class);
	}

	@Override
	public void render(Batch batch, ShapeRenderer shapeRenderer) {
		for (RenderableShape shape : shapes) {
			shape.render(transform.worldPosition, shapeRenderer);
		}
	}
}
