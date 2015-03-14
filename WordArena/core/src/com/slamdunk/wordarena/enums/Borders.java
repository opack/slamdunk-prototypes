package com.slamdunk.wordarena.enums;

/**
 * Détaille les côtés d'une cellule
 */
public enum Borders {
	TOP,
	RIGHT,
	BOTTOM,
	LEFT;
	
	public boolean isVertical() {
		return this == LEFT || this == RIGHT;
	}
	
	public boolean isHorizontal() {
		return this == TOP || this == BOTTOM;
	}
}
