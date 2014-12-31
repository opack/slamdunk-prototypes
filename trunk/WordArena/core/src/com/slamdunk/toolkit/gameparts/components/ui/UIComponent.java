package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.Component;

public class UIComponent extends Component {
	public Skin skin;
	
	public Actor actor;
	private Vector3 worldPosition;
	
	@Override
	public void init() {
		gameObject.scene.ui.addActor(actor);
		worldPosition = gameObject.transform.worldPosition;
	}
	
	@Override
	public void update(float deltaTime) {
		actor.act(deltaTime);
	}
	
	@Override
	public void physics(float deltaTime) {
		actor.setPosition(worldPosition.x, worldPosition.y);
	}
	
	/**
	 * Méthode qui doit être appelée lorsque la modification d'une
	 * des variables publiques de ce Component doit être propagée
	 * vers le widget enveloppé.
	 */
	public void updateWidget() {
	}
}
