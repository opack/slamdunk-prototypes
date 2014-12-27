package com.slamdunk.wordarena.gameparts.prefabs;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent;
import com.slamdunk.toolkit.gameparts.components.AnimationControllerComponent.Transition;
import com.slamdunk.toolkit.gameparts.components.AnimatorComponent;
import com.slamdunk.toolkit.gameparts.components.PathComponent;
import com.slamdunk.toolkit.gameparts.components.SpriteRendererComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;

public class Paladin extends GameObject {
	
	public Paladin() {
		AnimationControllerComponent animControllerComponent = new AnimationControllerComponent();
		animControllerComponent.addStates(
			animControllerComponent.new State("IdleRight", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3)),
			animControllerComponent.new State("IdleUp", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0)),
			animControllerComponent.new State("IdleLeft", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9)),
			animControllerComponent.new State("IdleDown", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6)),
			
			animControllerComponent.new State("WalkingUp", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0, 1, 2)),
			animControllerComponent.new State("WalkingRight", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3, 4, 5)),
			animControllerComponent.new State("WalkingLeft", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9, 10, 11)),
			animControllerComponent.new State("WalkingDown", AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6, 7, 8))
		);
		animControllerComponent.globalTransitions = new Transition[] {
			animControllerComponent.new Transition("IdleRight", animControllerComponent.new Condition("Direction", Directions4.RIGHT), animControllerComponent.new Condition("Action", States.IDLE)),
			animControllerComponent.new Transition("IdleUp", animControllerComponent.new Condition("Direction", Directions4.UP), animControllerComponent.new Condition("Action", States.IDLE)),
			animControllerComponent.new Transition("IdleLeft", animControllerComponent.new Condition("Direction", Directions4.LEFT), animControllerComponent.new Condition("Action", States.IDLE)),
			animControllerComponent.new Transition("IdleDown", animControllerComponent.new Condition("Direction", Directions4.DOWN), animControllerComponent.new Condition("Action", States.IDLE)),
			
			animControllerComponent.new Transition("WalkingRight", animControllerComponent.new Condition("Direction", Directions4.RIGHT), animControllerComponent.new Condition("Action", States.MOVING)),
			animControllerComponent.new Transition("WalkingUp", animControllerComponent.new Condition("Direction", Directions4.UP), animControllerComponent.new Condition("Action", States.MOVING)),
			animControllerComponent.new Transition("WalkingLeft", animControllerComponent.new Condition("Direction", Directions4.LEFT), animControllerComponent.new Condition("Action", States.MOVING)),
			animControllerComponent.new Transition("WalkingDown", animControllerComponent.new Condition("Direction", Directions4.DOWN), animControllerComponent.new Condition("Action", States.MOVING)),
		};
		animControllerComponent.defaultState = "IdleDown";	
		
		addComponent(new PathComponent());
		addComponent(animControllerComponent);
		addComponent(new AnimatorComponent());
		addComponent(new SpriteRendererComponent());
	}
}
