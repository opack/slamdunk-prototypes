package com.slamdunk.toolkit.world.pathfinder;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.world.point.Point;

public enum Directions {
	RIGHT,
	UP_RIGHT,
	UP,
	UP_LEFT,
	LEFT,
	DOWN_LEFT,
	DOWN,
	DOWN_RIGHT;
	
	private static Vector2 tmp = new Vector2();
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position.
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions getDirection(Point currentPosition, Point nextPosition) {
		int deltaX = nextPosition.getX() - currentPosition.getX();
		int deltaY = nextPosition.getY() - currentPosition.getY();
		Directions direction = null;

		// Même X
		if (deltaX == 0) {
			// Prochain Y plus petit
			if (deltaY < 0) {
				direction = DOWN;
			}
			// Prochain Y plus grand
			else if (deltaY > 0) {
				direction = UP;
			}
			// Si même Y : on laisse direction à null
		}
		// Prochain X plus petit
		else if (deltaX < 0) {
			// Même Y
			if (deltaY == 0) {
				direction = LEFT;
			}
			// Prochain Y plus petit
			else if (deltaY < 0) {
				direction = DOWN_LEFT;
			}
			// Prochain Y plus grand
			else {
				direction = UP_LEFT;
			}
		}
		// Prochain X plus grand
		else if (deltaX < 0) {
			// Même Y
			if (deltaY == 0) {
				direction = RIGHT;
			}
			// Prochain Y plus petit
			else if (deltaY < 0) {
				direction = DOWN_RIGHT;
			}
			// Prochain Y plus grand
			else {
				direction = UP_RIGHT;
			}
		}
		return direction;
	}
	
	/**
	 * Retourne la direction prise en fonction de la position actuelle et de la future
	 * position, en utilisant l'angle que forment les 2 vecteurs. On retourne alors
	 * la direction la plus proche de l'angle formé.
	 * @param currentPosition
	 * @param nextPosition
	 * @return null si les positions sont identiques
	 */
	public static Directions getDirection(Vector2 currentPosition, Vector2 nextPosition) {
		tmp.set(nextPosition);
		float angle = tmp.sub(currentPosition).angle();
		int dirIndex = Math.round(angle / 45);
		return Directions.values()[dirIndex % Directions.values().length];
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
		case DOWN_LEFT:
			flipped = UP_RIGHT;
			break;
		case DOWN_RIGHT:
			flipped = UP_LEFT;
			break;
		case UP_LEFT:
			flipped = DOWN_RIGHT;
			break;
		case UP_RIGHT:
			flipped = DOWN_LEFT;
			break;
		default:
			break;
		}
		return flipped;
	}
}
