package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.utils.Array;

/**
 * Liste de chemins qu'on peut suivre pour passer de l'un à l'autre.
 * Cet objet peut être partagé entre plusieurs objets grâce à
 * l'utilisation d'un PathCursor, qui peut "se déplacer" sur le chemin.
 */
public class PathList<T> extends Array<Path<T>> {
	private static final int APPROX_LENGTH_SAMPLES = 500;
	
	/**
	 * Longueur des différents segments du chemin
	 */
	private float[] lengths;
	
	// TODO DBG Créer un constructeur qui prend une liste de T et créer des Bézier entre chaque point, afin d'éviter de créer les Bézier à la main en répétant le dernier point comme étant le début de la ligne suivante
	
	public PathList(Path<T>... paths) {
		super(paths);
		lengths = new float[paths.length];
		for (int cur = 0; cur < paths.length; cur++) {
			lengths[cur] = paths[cur].approxLength(APPROX_LENGTH_SAMPLES);
		}
	}
	
	/**
	 * Retourne le chemin sur lequel se trouve le curseur
	 * @param cursor
	 * @return
	 */
	public Path<T> getPath(PathListCursor<T> cursor) {
		final int current = cursor.getCurrent();
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
		return cursor.getCurrent() >= size;
	}
	
	/**
	 * Retourne la position correspondant au curseur.
	 * Le vecteur retourné est le vecteur de travail et ne devrait donc
	 * être utilisé qu'en lecture.
	 * @param cursor
	 */
	public void valueAt(T result, PathListCursor<T> cursor) {
		Path<T> path = getPath(cursor);
		if (path == null) {
			result = null;
		}
		path.valueAt(result, cursor.getPosition());
	}
	
	/**
	 * Retourne la position correspondant à la position t,
	 * sachant que CONTRAIREMENT à un simple Path, t N'EST PAS
	 * entre 0 et 1 mais entre 0 et getNbPaths() - 1.
	 * @param result
	 * @param t
	 */
	public void valueAt(T result, int pathIndex, float t) {
		Path<T> path = get(pathIndex);
		path.valueAt(result, t);
	}

	/**
	 * Retourne la taille (approximée) du segment indiqué
	 * @param current
	 * @return
	 */
	public float getLength(int segmentIndex) {
		return lengths[segmentIndex];
	}
}
