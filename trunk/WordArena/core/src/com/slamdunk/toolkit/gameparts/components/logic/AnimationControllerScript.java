package com.slamdunk.toolkit.gameparts.components.logic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.gameparts.components.Component;

/**
 * Gère différents états et les paramètres qui autorisent les transitions entre eux.
 * Ce composant peut également changer l'animation d'un AnimatorComponent
 * Nécessite que le GameObject contienne un AnimatorComponent.
 */
public class AnimationControllerScript extends Component {
	public static final String ANY_STATE_NAME = "AnyState";
	
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
		public Array<Condition> conditions;
		public String nextState;
		
		public Transition (String nextState, Condition... conditions) {
			this.nextState = nextState;
			this.conditions = new Array<Condition>(conditions);
		}
		
		public Transition (String nextState, Array<Condition> conditions) {
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
		public Array<Transition> transitions;
		
		public State(String name) {
			this.name = name;
			this.transitions = new Array<Transition>();
		}
		
		public State(String name, Animation animation, Transition... transitions) {
			this(name);
			this.animation = animation;
			this.transitions.addAll(transitions);
		}
		

		public void addTransition(String nextState, Condition... conditions) {
			transitions.add(new Transition(nextState, conditions));
		}
		
		public void addTransition(String nextState, Object... conditionDatas) {
			Array<Condition> conditions = new Array<Condition>();
			String parameter;
			Object value;
			for (int curCondition = 0; curCondition < conditionDatas.length; curCondition += 2) {
				parameter = (String)conditionDatas[curCondition];
				value = conditionDatas[curCondition + 1];
				conditions.add(new Condition(parameter, value));
			}
			transitions.add(new Transition(nextState, conditions));
		}
		
		public boolean performTransition() {
			if (transitions != null) {
				for (Transition transition : transitions) {
					if (transition.isComplete()) {
						setCurrentState(transition.nextState);
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public Map<String, State> states;
	public String defaultState;
	public Map<String, Object> parameters;
	public State anyState;
	
	private State currentState;
	private AnimatorPart animator;
	
	public AnimationControllerScript() {
		parameters = new HashMap<String, Object>();
		states = new HashMap<String, State>();
	}

	public void setCurrentState(String name) {
		State newState = states.get(name);
		if (newState == null) {
			throw new IllegalArgumentException("There is no state named " + name + " !");
		}
		if (newState != currentState) {
			animator.setAnimation(newState.animation);
			animator.stateTime = 0;
			currentState = newState;
		}
	}
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(AnimatorPart.class)) {
			gameObject.addComponent(AnimatorPart.class);
		}
	}
	
	@Override
	public void reset() {
		parameters.clear();
		states.clear();
		anyState = addState(ANY_STATE_NAME, null);
		defaultState = null;
		currentState = null;
	}
	
	@Override
	public void init() {
		animator = gameObject.getComponent(AnimatorPart.class);
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
		if (!anyState.performTransition()) {
			currentState.performTransition();
		}
	}
	
	public State addState(String name, Animation animation) {
		State state = this.new State(name, animation);
		states.put(name, state);
		return state;
	}

	public void addStates(State... states) {
		for (State state : states) {
			this.states.put(state.name, state);
		}
	}
}
