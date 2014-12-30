package com.slamdunk.wordarena.gameparts.scripts;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TransformComponent;
import com.slamdunk.toolkit.gameparts.components.logic.AnimationControllerComponent;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;

/**
 * Met à jour des paramètres dans un AnimationControllerComponent
 * en fonction de la direction vers laquelle se tourne le GameObject
 * et de son état (en déplacement ou à l'arrêt).
 */
public class DirectionUpdaterScript extends Component {
	/**
	 * Paramètre de l'AnimationController à définir contenant
	 * la direction (prendra une des valeurs de Directions4)
	 * Valeur par défaut : "Direction"
	 */
	public String directionParameter;
	
	/**
	 * Paramètre de l'AnimationController à définir contenant
	 * l'action en cours (prendra la valeur States.MOVING ou
	 * States.IDLE).
	 * Valeur par défaut : "Action"
	 */
	public String actionParameter;
	
	private AnimationControllerComponent animationControllerComponent;
	private Vector3 transformPosition;
	
	private Vector2 lastPosition;
	private Vector2 currentPosition;
	
	@Override
	public void reset() {
		directionParameter = "Direction";
		actionParameter = "Action";
	}
	
	@Override
	public void init() {
		animationControllerComponent = gameObject.getComponent(AnimationControllerComponent.class);
		transformPosition = gameObject.getComponent(TransformComponent.class).worldPosition;
		
		currentPosition = new Vector2();
		lastPosition = new Vector2();
	}
	
	@Override
	public void update(float deltaTime) {
		currentPosition.set(transformPosition.x, transformPosition.y);
		
		if (currentPosition.equals(lastPosition)) {
			animationControllerComponent.parameters.put(actionParameter, States.IDLE);
		} else {
			animationControllerComponent.parameters.put(directionParameter, Directions4.getDirection(lastPosition, currentPosition));
			animationControllerComponent.parameters.put(actionParameter, States.MOVING);
		}
		
		lastPosition.set(currentPosition);
	}
}
