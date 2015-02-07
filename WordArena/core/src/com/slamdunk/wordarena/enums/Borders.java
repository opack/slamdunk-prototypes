package com.slamdunk.wordarena.enums;

/**
 * Détaille les côtés d'une cellule
 */
public enum Borders {
	TOP(Orientation.HORIZONTAL),
	RIGHT(Orientation.VERTICAL),
	BOTTOM(Orientation.HORIZONTAL),
	LEFT(Orientation.VERTICAL);
	
	private Orientation orientation;

	private Borders(Orientation orientation) {
		this.orientation = orientation;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}
}
