package com.slamdunk.wordarena.withgameparts.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TransformComponent;
import com.slamdunk.toolkit.world.Directions4;

public class PlayerMoveScript extends Component {
	/**
	 * Vitesse, en pixels par seconde
	 */
	public float speed;
	
	private AnimationControllerComponent animationController;
	private TransformComponent transform;
	
	@Override
	public void init() {
		animationController = gameObject.getComponent(AnimationControllerComponent.class);
		transform = gameObject.transform;
	}
	
	@Override
	public void update(float deltaTime) {
		Directions4 direction = null;
		String action = "Moving";
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = Directions4.LEFT;
			transform.position.x -= speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = Directions4.RIGHT;
			transform.position.x += speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			direction = Directions4.UP;
			transform.position.y += speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			direction = Directions4.DOWN;
			transform.position.y -= speed * deltaTime;
		} else {
			action = "Idle";
		}
		if (direction != null) {
			animationController.parameters.put("Direction", direction);
		}
		animationController.parameters.put("Action", action);
	}
}
