package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.renderers.shapes.RenderableCircle;
import com.slamdunk.toolkit.gameparts.components.renderers.shapes.ShapeRendererPart;

public class CircleWaouwScript extends Component {
	
	public float minRadius;
	public float maxRadius;
	public float effectTime;
	
	private RenderableCircle circle;
	private float alpha;
	private float accuTime;
	private float direction;
	
	public CircleWaouwScript() {
		circle = new RenderableCircle(0, 0, 50);
	}
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(ShapeRendererPart.class)) {
			gameObject.addComponent(ShapeRendererPart.class);
		}
	}
	
	@Override
	public void reset() {
		minRadius = 20;
		maxRadius = 40;
		effectTime = 1;
	}
	
	@Override
	public void init() {
		gameObject.getComponent(ShapeRendererPart.class).shapes.add(circle);
		direction = 1;
	}
	
	@Override
	public void update(float deltaTime) {
		accuTime += direction * deltaTime;
		alpha = accuTime / effectTime;
		if (alpha <= 0) {
			alpha = -alpha;
			accuTime = 0;
			direction = 1;
		} else if (alpha >= 1) {
			alpha = 1 - (alpha - 1);
			accuTime = effectTime;
			direction = -1;
		}
		circle.setRadius(MathUtils.lerp(minRadius, maxRadius, alpha));
	}
}
