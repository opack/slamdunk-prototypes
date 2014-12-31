package com.slamdunk.wordarena.gameparts.prefabs;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.gameparts.components.logic.AnimationControllerScript;
import com.slamdunk.toolkit.gameparts.components.logic.DirectionUpdaterScript;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;

public class Paladin extends GameObject {
	
	public Paladin() {
		// Ajout d'un composant permettant au Paladin de suivre un chemin
		addComponent(PathFollowerScript.class);
		
		// Ajout d'un composant permettant à l'animation de changer en fonction
		// de l'action du paladin (déplacement ou oisif) et la direction vers
		// laquelle il se tourne
		AnimationControllerScript animControllerComponent = addComponent(AnimationControllerScript.class);
		animControllerComponent.addState("IdleRight", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3));
		animControllerComponent.addState("IdleUp", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0));
		animControllerComponent.addState("IdleLeft", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9));
		animControllerComponent.addState("IdleDown", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6));
		animControllerComponent.addState("WalkingUp", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 0, 1, 2));
		animControllerComponent.addState("WalkingRight", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 3, 4, 5));
		animControllerComponent.addState("WalkingLeft", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 9, 10, 11));
		animControllerComponent.addState("WalkingDown", AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, PlayMode.LOOP, 6, 7, 8));
		
		animControllerComponent.anyState.addTransition("IdleRight",
			"Direction", Directions4.RIGHT,
			"Action", States.IDLE);
		animControllerComponent.anyState.addTransition("IdleUp",
			"Direction", Directions4.UP,
			"Action", States.IDLE);
		animControllerComponent.anyState.addTransition("IdleLeft",
			"Direction", Directions4.LEFT,
			"Action", States.IDLE);
		animControllerComponent.anyState.addTransition("IdleDown",
			"Direction", Directions4.DOWN,
			"Action", States.IDLE);
		animControllerComponent.anyState.addTransition("WalkingRight",
			"Direction", Directions4.RIGHT,
			"Action", States.MOVING);
		animControllerComponent.anyState.addTransition("WalkingUp",
			"Direction", Directions4.UP,
			"Action", States.MOVING);
		animControllerComponent.anyState.addTransition("WalkingLeft",
			"Direction", Directions4.LEFT,
			"Action", States.MOVING);
		animControllerComponent.anyState.addTransition("WalkingDown",
			"Direction", Directions4.DOWN,
			"Action", States.MOVING);
		animControllerComponent.defaultState = "IdleDown";	
		
		// Ajout d'un script qui màj les paramètres de l'AnimationController
		// en fonction de l'état du paladin (en déplacement ou non) et de la
		// direction vers laquelle il se tourne
		addComponent(DirectionUpdaterScript.class);
		getComponent(DirectionUpdaterScript.class).directionParameter = "Direction";
		getComponent(DirectionUpdaterScript.class).actionParameter = "Action";
	}
}
