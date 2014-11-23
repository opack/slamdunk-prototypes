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
	
	/**
	 * Emplacement courant du curseur
	 */
	private Point current;
	
	public PathCursor(Path path) {
		this(path, 0);
	}
	
	public PathCursor(Path path, int startIndexInPath) {
		this.path = path;
		this.index = startIndexInPath;
		current = path.getPosition(startIndexInPath);
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
		current = path.getPosition(index);
		return current;
	}

	/**
	 * Retourne la position sans déplacer le curseur
	 * @return
	 */
	public Point current() {
		return current;
	}
}
