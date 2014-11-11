package com.slamdunk.toolkit.world.pathfinder;

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
}
