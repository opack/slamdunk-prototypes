package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.logic.AnimationControllerScript;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;
import com.slamdunk.toolkit.world.Directions4;

public class PlayerMoveScript extends Component {
	public Directions4 direction;
	
	public String action;
	
	/**
	 * Vitesse, en pixels par seconde
	 */
	public float speed;
	
	private AnimationControllerScript animationController;
	private TransformPart transform;
	
	@Override
	public void init() {
		animationController = gameObject.getComponent(AnimationControllerScript.class);
		transform = gameObject.transform;
	}
	
	@Override
	public void update(float deltaTime) {
		direction = null;
		action = "Moving";
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = Directions4.LEFT;
			transform.worldPosition.x -= speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = Directions4.RIGHT;
			transform.worldPosition.x += speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			direction = Directions4.UP;
			transform.worldPosition.y += speed * deltaTime;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			direction = Directions4.DOWN;
			transform.worldPosition.y -= speed * deltaTime;
		} else {
			action = "Idle";
		}
		if (direction != null) {
			animationController.parameters.put("Direction", direction);
		}
		animationController.parameters.put("Action", action);
	}
}
