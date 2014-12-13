package com.slamdunk.toolkit.world.pathfinder;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.world.point.Point;

public enum Directions {
	UP,
	DOWN,
	LEFT,
	RIGHT;
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position.
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions getDirection(Point currentPosition, Point nextPosition) {
		if (currentPosition.getY() < nextPosition.getY()) {
			return UP;
		} else if (currentPosition.getY() > nextPosition.getY()) {
			return DOWN;
		}
		if (currentPosition.getX() < nextPosition.getX()) {
			return RIGHT;
		} else if (currentPosition.getX() > nextPosition.getX()) {
			return LEFT;
		}
		return null;
	}
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position.
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions getDirection(Vector2 currentPosition, Vector2 nextPosition) {
//DBG		if (currentPosition.y < nextPosition.y) {
//			return UP;
//		} else if (currentPosition.y > nextPosition.y) {
//			return DOWN;
//		}
		if (currentPosition.x < nextPosition.x) {
			return RIGHT;
		} else if (currentPosition.x > nextPosition.x) {
			return LEFT;
		}
		return null;
	}

	/**
	 * Retourne la direction oppoosée
	 * @param direction
	 * @return
	 */
	public static Directions flip(Directions direction) {
		Directions flipped = direction;
		switch (direction) {
		case DOWN:
			flipped = UP;
			break;
		case LEFT:
			flipped = RIGHT;
			break;
		case RIGHT:
			flipped = LEFT;
			break;
		case UP:
			flipped = DOWN;
			break;
		}
		return flipped;
	}
}
