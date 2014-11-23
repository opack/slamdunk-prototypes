package com.slamdunk.toolkit.world.pathfinder;

import java.util.List;

import com.slamdunk.toolkit.world.point.Point;

/**
 * Stocke les différentes positions d'un chemin. Ce chemin peut être utilisé par plusieurs
 * objets. Il faut alors utiliser un PathCursor pour déterminer la position de l'objet
 * qui suit le chemin.
 */
public class Path implements Comparable<Path> {
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
	 * Indique s'il n'y a aucune position dans ce chemin
	 * @return
	 */
	public boolean isEmpty() {
		return positions.isEmpty();
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
	
	/**
	 * Retourne l'indice de la position spécifiée sur le chemin
	 * @param position
	 * @return -1 si la position n'est pas sur le chemin
	 */
	public int indexOf(Point position) {
		return positions.indexOf(position);
	}

	/**
	 * Indique si le chemin s'arrête sur la position indiquée
	 * @param tilePosition
	 * @return
	 */
	public boolean endsAt(Point position) {
		return position != null
			&& !positions.isEmpty()
			&& positions.get(positions.size() - 1).equals(position);
	}

	@Override
	public int compareTo(Path other) {
		// On compare la taille des chemins
		return positions.size() - other.positions.size();
	}

	/**
	 * Retourne le nombre de positions entre le début du chemin et la position
	 * indiquée. Si 0, cela indique que la position est le début du chemin.
	 * @param position
	 * @return -1 si la position n'est pas sur le chemin
	 */
	public int distanceTo(Point position) {
		return positions.indexOf(position);
	}
}
