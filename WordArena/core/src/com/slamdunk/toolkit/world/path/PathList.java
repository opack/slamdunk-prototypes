package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

/**
 * Liste de chemins qu'on peut suivre pour passer de l'un à l'autre.
 * Cet objet peut être partagé entre plusieurs objets grâce à
 * l'utilisation d'un PathCursor, qui peut "se déplacer" sur le chemin.
 */
public class PathList<T extends Vector<T>> extends Array<Path<T>> {
	private static final int APPROX_LENGTH_SAMPLES = 500;
	
	/**
	 * Longueur des différents segments du chemin
	 */
	private Array<Float> lengths;
	
	/**
	 * Crée une liste de chemins qui passent par les points indiqués. Le chemin
	 * global ainsi créé est un chemin continu entre tous ces points, cela signifie
	 * que chaque chemin du PathList est un chemin entre un point et le suivant
	 * dans le tableau fourni en paramètre.
	 * @param closePath Si true, un dernier segment est créé entre le premier et le
	 * dernier point
	 * @param points Les points par lesquels passe le chemin. Au moins 2 points.
	 */
	@SuppressWarnings("unchecked")
	public PathList(boolean closePath, T... points) {
		if (points.length < 2) {
			throw new IllegalArgumentException("The points list must contain at least 2 values.");
		}
		// Crée les chemins puis les ajoute en mettant à jour le tableau des longueurs
		lengths = new Array<Float>(size);
		for (int cur = 0; cur < points.length - 1; cur++) {
			addPath(new Bezier<T>(points, cur, 2));
		}
		if (closePath) {
			addPath(new Bezier<T>(points[points.length - 1], points[0]));
		}
	}
	
	/**
	 * Crée une liste de chemins dont chaque chemin passe par les points
	 * d'un item du tableau pointsArrays.
	 * @param pointsArrays
	 */
	public PathList(T[]... pointsArrays) {
		lengths = new Array<Float>(size);
		for (T[] pointsArray : pointsArrays) {
			addPath(new Bezier<T>(pointsArray));
		}
	}
	
	/**
	 * Crée une liste de chemins en utilisant les chemins fournis
	 * @param paths
	 */
	public PathList(Path<T>... paths) {
		super(paths);
		
		// Mise à jour du tableau de longueurs
		lengths = new Array<Float>(size);
		for (int cur = 0; cur < size; cur++) {
			lengths.set(cur, get(cur).approxLength(APPROX_LENGTH_SAMPLES));
		}
	}
	
	/**
	 * Ajoute une nouveau chemin à la liste et met à jour le
	 * tableau des longueurs
	 * @param path
	 */
	public void addPath(Path<T> path) {
		// Ajoute le chemin à la liste
		add(path);
		// Calcule sa longueur
		lengths.add(path.approxLength(APPROX_LENGTH_SAMPLES));
	}
	
	/**
	 * Retourne le chemin sur lequel se trouve le curseur
	 * @param cursor
	 * @return
	 */
	public Path<T> getPath(PathListCursor<T> cursor) {
		final int current = cursor.getCurrentSegmentIndex();
		if (current < 0
		|| current >= size) {
			return null;
		}
		return get(current);
	}
	
	/**
	 * Indique si le curseur a atteint la fin du chemin
	 * @param cursor
	 * @return
	 */
	public boolean isFinished(PathListCursor<T> cursor) {
		return cursor.getCurrentSegmentIndex() >= size;
	}
	
	/**
	 * Remplit result avec la valeur du segment correspondant
	 * à la position curseur.
	 * @param cursor
	 */
	public void valueAt(T result, PathListCursor<T> cursor) {
		Path<T> path = getPath(cursor);
		if (path == null) {
			result = null;
			return;
		}
		path.valueAt(result, cursor.getPosition());
	}
	
	/**
	 * Remplit result avec la position correspondant à 
	 * la position t sur le segment d'indice segmentIndex
	 * @param result
	 * @param t
	 */
	public void valueAt(T result, int segmentIndex, float t) {
		Path<T> path = get(segmentIndex);
		path.valueAt(result, t);
	}

	/**
	 * Retourne la taille (approximée) du segment indiqué
	 * @param current
	 * @return
	 */
	public float getLength(int segmentIndex) {
		return lengths.get(segmentIndex);
	}
}
