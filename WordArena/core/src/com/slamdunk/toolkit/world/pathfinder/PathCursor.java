package com.slamdunk.toolkit.world.pathfinder;

import com.slamdunk.toolkit.world.point.Point;

/**
 * Représente l'index d'une position sur un chemin. Le cursor
 * est lié à un chemin et peut déterminer si la fin du chemin
 * est atteinte.
 */
public class PathCursor {
	/**
	 * Chemin associé à ce curseur
	 */
	private Path path;
	
	/**
	 * Position actuelle du curseur sur le chemin
	 */
	private int index;
	
	public PathCursor(Path path) {
		this.path = path;
	}
	
	public Path getPath() {
		return path;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Indique si la fin du chemin est atteinte
	 * @return
	 */
	public boolean isArrivalReached() {
		return index >= path.length() - 1;
	}
	
	/**
	 * Avance le curseur d'une position et retourne cette
	 * position ou null si la fin du chemin est atteinte
	 * @return
	 */
	public Point next() {
		// Si la fin du chemin est atteinte, on retourne null
		if (isArrivalReached()) {
			return null;
		}
		// Sinon, on retourne la prochaine position
		index++;
		return path.getPosition(index);
	}
}
