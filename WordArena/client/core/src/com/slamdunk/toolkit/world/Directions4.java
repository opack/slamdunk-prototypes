package com.slamdunk.toolkit.world;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.world.point.Point;

public enum Directions4 {
	RIGHT,
	UP,
	LEFT,
	DOWN;
	
	private static Vector2 tmp = new Vector2();
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position.
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions4 getDirection(Point currentPosition, Point nextPosition) {
		int deltaX = nextPosition.getX() - currentPosition.getX();
		if (deltaX < 0) {
			return LEFT;
		} else if (deltaX > 0) {
			return RIGHT;
		}
		
		int deltaY = nextPosition.getY() - currentPosition.getY();
		if (deltaY < 0) {
			return DOWN;
		} else if (deltaY > 0) {
			return UP;
		}
		
		return null;
	}
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position, en utilisant l'angle que forment les 2 vecteurs. On retourne alors
	 * la direction correspondant au quartier dans lequel se trouve l'angle
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions4 getDirection(Vector2 currentPosition, Vector2 nextPosition) {
		tmp.set(nextPosition);
		float angle = tmp.sub(currentPosition).angle();
		if (angle > 315 || angle <= 45) {
			return Directions4.RIGHT;
		} else if (45 < angle && angle <= 135) {
			return Directions4.UP;
		} else if (135 < angle && angle <= 225) {
			return Directions4.LEFT;
		} else if (225 < angle && angle <= 315) {
			return Directions4.DOWN;
		}
		return null;
	}

	/**
	 * Retourne la direction oppoosée
	 * @param direction
	 * @return
	 */
	public static Directions4 flip(Directions4 direction) {
		Directions4 flipped = direction;
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
		default:
			break;
		}
		return flipped;
	}
}
