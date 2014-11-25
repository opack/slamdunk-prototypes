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
	private float[] lengths;
	
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
		for (int cur = 0; cur < points.length - 1; cur++) {
			add(new Bezier<T>(points, cur, 2));
		}
		if (closePath) {
			add(new Bezier<T>(points[points.length - 1], points[0]));
		}
		initSegmentLengths();
	}
	
	public PathList(Path<T>... paths) {
		super(paths);
		initSegmentLengths();
	}
	
	/**
	 * Initialise le tableau avec la taille des segments du chemin (donc
	 * les sous-chemins). Cette opération est fait une fois à la création
	 * du PathList pour gagner du temps et éviter d'approximer la taille
	 * des segments à chaque fois que nécessaire. On peut donc se permettre
	 * ici d'avoir une approximation assez précise.
	 */
	private void initSegmentLengths() {
		lengths = new float[size];
		for (int cur = 0; cur < size; cur++) {
			lengths[cur] = get(cur).approxLength(APPROX_LENGTH_SAMPLES);
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
			return;
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
