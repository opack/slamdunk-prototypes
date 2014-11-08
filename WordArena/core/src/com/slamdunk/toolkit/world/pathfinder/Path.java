package com.slamdunk.toolkit.world.pathfinder;

import java.util.List;

import com.slamdunk.toolkit.world.point.Point;

/**
 * Stocke les différentes positions d'un chemin. Ce chemin peut être utilisé par plusieurs
 * objets. Il faut alors utiliser un PathCursor pour déterminer la position de l'objet
 * qui suit le chemin.
 */
public class Path {
	/**
	 * Positions du chemin
	 */
	private List<Point> positions;
	
	public Path(List<Point> positions) {
		this.positions = positions;
	}

	/**
	 * Retourne la longueur du chemin, en nombre de positions
	 * @return
	 */
	public int length() {
		return positions.size();
	}

	/**
	 * Retourne la position à l'indice indiqué
	 * @param index
	 * @return
	 */
	public Point getPosition(int index) {
		return positions.get(index);
	}
	
	/**
	 * Indique si la position fait partie du chemin 
	 * @param position
	 * @return
	 */
	public boolean contains(Point position) {
		return positions.contains(position);
	}
}
