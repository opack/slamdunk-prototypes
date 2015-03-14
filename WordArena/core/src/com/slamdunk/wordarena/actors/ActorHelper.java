package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slamdunk.wordarena.enums.Borders;

public class ActorHelper {
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé à cheval sur la bordure.
	 */
	public static void alignOnBorder(Borders border, Vector2 cornerPosition, Actor actor) {
		switch (border) {
		case LEFT:
		case RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth() / 2, cornerPosition.y);
			break;
		case BOTTOM:
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight() / 2);
			break;
		}
	}
	
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé le long de la bordure, à l'intérieur de la cellule
	 */
	public static void alignInsideCell(Borders border, Vector2 cornerPosition, Actor actor) {
		switch (border) {
		case BOTTOM:
			actor.setPosition(cornerPosition.x, cornerPosition.y);
			break;
		case LEFT:
			actor.setPosition(cornerPosition.x, cornerPosition.y);
			break;
		case RIGHT:
			actor.setPosition(cornerPosition.x - actor.getWidth(), cornerPosition.y);
			break;
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight());
			break;
		}
	}
	
	/**
	 * L'alignement doit être fait de façon à ce que l'Actor soit
	 * placé le long de la bordure, à l'extérieur de la cellule
	 */
	public static void alignOutsideCell(Borders border, Vector2 cornerPosition, Actor actor) {
		switch (border) {
		case BOTTOM:
			actor.setPosition(cornerPosition.x, cornerPosition.y - actor.getHeight());
			break;
		case LEFT:
			actor.setPosition(cornerPosition.x - actor.getWidth(), cornerPosition.y);
			break;
		case RIGHT:
			actor.setPosition(cornerPosition.x, cornerPosition.y);
			break;
		case TOP:
			actor.setPosition(cornerPosition.x, cornerPosition.y);
			break;
		}
	}
}
