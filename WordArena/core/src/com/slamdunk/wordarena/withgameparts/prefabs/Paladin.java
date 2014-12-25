package com.slamdunk.wordarena.withgameparts.prefabs;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent.Transition;
import com.slamdunk.toolkit.gameparts.components.AnimatorComponent;
import com.slamdunk.toolkit.gameparts.components.SpriteRendererComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.withgameparts.scripts.PlayerMoveScript;

public class Paladin extends GameObject {
	
	public Paladin() {
		AnimationControllerComponent controller = new AnimationControllerComponent();
		controller.addStates(
			controller.new State("IdleUp", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0)),
			controller.new State("WalkingUp", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0, 1, 2)),
			controller.new State("IdleRight", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3)),
			controller.new State("WalkingRight", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3, 4, 5)),
			controller.new State("IdleDown", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6)),
			controller.new State("WalkingDown", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6, 7, 8)),
			controller.new State("IdleLeft", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9)),
			controller.new State("WalkingLeft", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9, 10, 11))
		);
		
		controller.globalTransitions = new Transition[] {
			controller.new Transition("IdleRight", controller.new Condition("Direction", Directions4.RIGHT), controller.new Condition("Action", "Idle")),
			controller.new Transition("IdleUp", controller.new Condition("Direction", Directions4.UP), controller.new Condition("Action", "Idle")),
			controller.new Transition("IdleLeft", controller.new Condition("Direction", Directions4.LEFT), controller.new Condition("Action", "Idle")),
			controller.new Transition("IdleDown", controller.new Condition("Direction", Directions4.DOWN), controller.new Condition("Action", "Idle")),
			
			controller.new Transition("WalkingRight", controller.new Condition("Direction", Directions4.RIGHT), controller.new Condition("Action", "Moving")),
			controller.new Transition("WalkingUp", controller.new Condition("Direction", Directions4.UP), controller.new Condition("Action", "Moving")),
			controller.new Transition("WalkingLeft", controller.new Condition("Direction", Directions4.LEFT), controller.new Condition("Action", "Moving")),
			controller.new Transition("WalkingDown", controller.new Condition("Direction", Directions4.DOWN), controller.new Condition("Action", "Moving")),
		};
		controller.defaultState = "IdleDown";	
		addComponent(controller);
		addComponent(new AnimatorComponent());
		addComponent(new SpriteRendererComponent());
		addComponent(new PlayerMoveScript());
	}
}
