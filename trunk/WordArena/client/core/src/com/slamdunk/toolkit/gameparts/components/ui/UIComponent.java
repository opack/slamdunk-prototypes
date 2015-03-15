package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.SizePart;

/**
 * Si le GameObject a un composant SizePart, celui-ci sera mis à jour
 * avec la taille du widget
 */
public class UIComponent extends Component {
	public Skin skin;
	
	public Actor actor;
	private Vector3 worldPosition;
	
	private SizePart size;
	
	@Override
	public void init() {
		gameObject.scene.ui.addActor(actor);
		worldPosition = gameObject.transform.worldPosition;
		size = gameObject.getComponent(SizePart.class);
		if (size != null) {
			size.width = actor.getWidth();
			size.height = actor.getHeight();
		}
	}
	
	@Override
	public void update(float deltaTime) {
		actor.act(deltaTime);
		if (size != null) {
			size.width = actor.getWidth();
			size.height = actor.getHeight();
		}
	}
	
	@Override
	public void physics(float deltaTime) {
		actor.setPosition(worldPosition.x, worldPosition.y);
		if (size != null) {
			actor.setSize(size.width, size.height);
		}
	}
	
	/**
	 * Méthode qui doit être appelée lorsque la modification d'une
	 * des variables publiques de ce Component doit être propagée
	 * vers le widget enveloppé.
	 */
	public void updateWidget() {
	}
}
