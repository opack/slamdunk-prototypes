package com.slamdunk.toolkit.gameparts.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Gère différents états et les paramètres qui autorisent les transitions entre eux.
 * Ce composant peut également changer l'animation d'un AnimatorComponent
 * Nécessite que le GameObject contienne un AnimatorComponent.
 */
public class AnimationControllerComponent extends Component {
	public class Condition {
		public String parameter;
		public Object completionValue;
		
		public Condition(String parameter, Object completionValue) {
			this.parameter = parameter;
			this.completionValue = completionValue;
		}
		
		public boolean isComplete() {
			Object parameterValue = parameters.get(parameter);
			if (parameterValue == null) {
				return completionValue == null;
			}
			return parameterValue.equals(completionValue);
		}
	}
	
	public class Transition {
		public Condition[] conditions;
		public String nextState;
		
		public Transition (String nextState, Condition... conditions) {
			this.nextState = nextState;
			this.conditions = conditions;
		}
		
		public boolean isComplete() {
			if (conditions != null) {
				for (Condition condition : conditions) {
					if (!condition.isComplete()) {
						return false;
					}
				}
			}
			return true;
		}
	}
	
	public class State {
		public String name;
		public Animation animation;
		public Transition[] transitions;
		
		public State(String name) {
			this.name = name;
		}
		
		public State(String name, Animation animation, Transition... transitions) {
			this.name = name;
			this.animation = animation;
			this.transitions = transitions;
		}
		
		public void performTransition() {
			if (transitions != null) {
				for (Transition transition : globalTransitions) {
					if (transition.isComplete()) {
						setCurrentState(transition.nextState);
						return;
					}
				}
				for (Transition transition : transitions) {
					if (transition.isComplete()) {
						setCurrentState(transition.nextState);
						return;
					}
				}
			}
		}
	}
	
	public Map<String, State> states;
	public String defaultState;
	public Map<String, Object> parameters;
	public Transition[] globalTransitions;
	
	private AnimatorComponent animator;
	private State currentState;
	
	public AnimationControllerComponent() {
		parameters = new HashMap<String, Object>();
		states = new HashMap<String, State>();
	}

	public void setCurrentState(State state) {
		if (state != currentState) {
			animator.setAnimation(state.animation);
			animator.stateTime = 0;
			currentState = state;
		}
	}
	
	public void setCurrentState(String name) {
		State newState = states.get(name);
		if (newState == null) {
			throw new IllegalArgumentException("There is no state named " + name + " !");
		}
		setCurrentState(newState);
	}
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(AnimatorComponent.class)) {
			gameObject.addComponent(AnimatorComponent.class);
		}
	}
	
	@Override
	public void reset() {
		parameters.clear();
		states.clear();
		defaultState = "";
		globalTransitions = null;
	}
	
	@Override
	public void init() {
		animator = gameObject.getComponent(AnimatorComponent.class);
		if (animator == null) {
			throw new IllegalStateException("Missing AnimatorComponent component in the GameObject. The AnimationControllerComponent component cannot work properly.");
		}
		
		if (states.size() == 0) {
			throw new IllegalStateException("No states have been defined in this AnimationControllerComponent.");
		}
		
		if (defaultState == null
		|| defaultState.isEmpty()) {
			throw new IllegalStateException("No default state have been defined in this AnimationControllerComponent.");
		}
		setCurrentState(defaultState);
	}
	
	@Override
	public void update(float deltaTime) {
		currentState.performTransition();
	}

	public void addStates(State... states) {
		for (State state : states) {
			this.states.put(state.name, state);
		}
	}
}
