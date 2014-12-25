package com.slamdunk.wordarena.withgameparts.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.world.Directions4;

public class PlayerMoveScript extends Component {
	private AnimationControllerComponent animationController;
	
	@Override
	public void init() {
		animationController = gameObject.getComponent(AnimationControllerComponent.class);
	}
	
	@Override
	public void update(float deltaTime) {
		Directions4 direction = null;
		String action = "Moving";
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = Directions4.LEFT;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = Directions4.RIGHT;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			direction = Directions4.UP;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			direction = Directions4.DOWN;
		} else {
			action = "Idle";
		}
		if (direction != null) {
			animationController.parameters.put("Direction", direction);
		}
		animationController.parameters.put("Action", action);
	}
}
