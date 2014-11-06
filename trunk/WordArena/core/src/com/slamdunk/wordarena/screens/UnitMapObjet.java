package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.graphics.tiled.ActingObject;
import com.slamdunk.toolkit.graphics.tiled.SpriteMapObject;
import com.slamdunk.toolkit.world.point.Point;

/**
 * Une unité lambda
 */
public class UnitMapObjet extends SpriteMapObject implements ActingObject {

	private Point destination;
	
	public UnitMapObjet(Sprite sprite, String name, int tileSize) {
		super(sprite, name, tileSize);
	}
	
	public void setDestination(Point destination) {
		this.destination = destination;
	}

	@Override
	public void act(float delta) {
		// Si on n'est pas encore arrivé à destination, on bouge
		Array<Action> actions = this.actions;
		for (int i = 0; i < actions.size; i++) {
			Action action = actions.get(i);
			if (action.act(delta) && i < actions.size) {
				Action current = actions.get(i);
				int actionIndex = current == action ? i : actions.indexOf(action, true);
				if (actionIndex != -1) {
					actions.removeIndex(actionIndex);
					action.setActor(null);
					i--;
				}
			}
		}
		MoveToAction action = Actions.moveTo(0, 0, 1.0f);
		action.setActor(this);
	}

}
