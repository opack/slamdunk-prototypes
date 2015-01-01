package com.slamdunk.toolkit.gameparts.components.position;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

/**
 * Place les enfants du GameObject conteneur en cercle autour de leur parent
 */
public class CircleLayoutScript extends Component {
	public float radius;
	public boolean layout;
	
	@Override
	public void reset() {
		radius = 100;
		layout = true;
	}
	
	@Override
	public void init() {
		if (radius <= 0) {
			throw new IllegalArgumentException("radius must be positive");
		}
	}

	@Override
	public void update(float deltaTime) {
		if (layout) {
			final List<GameObject> children = gameObject.getChildren();
			final int nbChildren = children.size();
			final float angleBetweenEachChild = MathUtils.PI * 2 / nbChildren;
			
			GameObject child;
			for (int curChild = 0; curChild < nbChildren; curChild++) {
				child = children.get(curChild);
				child.transform.relativePosition.x = MathUtils.cos(angleBetweenEachChild * curChild) * radius;
				child.transform.relativePosition.y = MathUtils.sin(angleBetweenEachChild * curChild) * radius;
			}
			
			layout = false;
		}
	}
}
