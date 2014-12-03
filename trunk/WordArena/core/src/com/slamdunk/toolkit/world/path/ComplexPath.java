package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Liste de chemins qu'on peut suivre pour passer de l'un à l'autre.
 * Cet objet peut être partagé entre plusieurs objets grâce à
 * l'utilisation d'un PathCursor, qui peut "se déplacer" sur le chemin.
 */
public class ComplexPath<T extends Vector<T>> extends Array<Path<T>> {
	private static final int APPROX_LENGTH_SAMPLES = 500;
	
	/**
	 * Longueur des différents segments du chemin
	 */
	private Array<Float> lengths;
	
	public ComplexPath() {
		lengths = new Array<Float>(size);
	}
	
	/**
	 * Crée une liste de chemins qui passent par les points indiqués. Le chemin
	 * global ainsi créé est un chemin continu entre tous ces points, cela signifie
	 * que chaque chemin du ComplexPath est un chemin entre un point et le suivant
	 * dans le tableau fourni en paramètre.
	 * @param closePath Si true, un dernier segment est créé entre le premier et le
	 * dernier point
	 * @param points Les points par lesquels passe le chemin. Au moins 2 points.
	 */
	@SuppressWarnings("unchecked")
	public ComplexPath(boolean closePath, T... points) {
		if (points.length < 2) {
			throw new IllegalArgumentException("The points list must contain at least 2 values.");
		}
		// Crée les chemins puis les ajoute en mettant à jour le tableau des longueurs
		lengths = new Array<Float>(size);
		for (int cur = 0; cur < points.length - 1; cur++) {
			add(new Bezier<T>(points, cur, 2));
		}
		if (closePath) {
			add(new Bezier<T>(points[points.length - 1], points[0]));
		}
	}
	
	/**
	 * Crée une liste de chemins dont chaque chemin passe par les points
	 * d'un item du tableau pointsArrays.
	 * @param pointsArrays
	 */
	public ComplexPath(T[]... pointsArrays) {
		lengths = new Array<Float>(size);
		for (T[] pointsArray : pointsArrays) {
			add(new Bezier<T>(pointsArray));
		}
	}
	
	/**
	 * Crée une liste de chemins de type Bezier cubic, c'est-à-dire
	 * où chaque courbe de Bézier est définie par 4 points, chaque
	 * point terminant une courbe étant le point de départ de la
	 * courbe suivante.
	 * @param pointsArray
	 */
	public static ComplexPath<Vector2> createCubicBezierPathList(int nbPaths, float... pointsArray) {
		if ((nbPaths > 1 && pointsArray.length < 4*nbPaths-1)
		|| (nbPaths == 1 && pointsArray.length < 4)) {
			throw new IllegalArgumentException("Le nombre de points fournis (" + pointsArray.length + ")ne permet pas de construire " + nbPaths + " chemins.");
		}
		ComplexPath<Vector2> pathList = new ComplexPath<Vector2>();
		Vector2 start = new Vector2(pointsArray[0], pointsArray[1]);
		Vector2 cp1 = new Vector2();
		Vector2 cp2 = new Vector2();
		Vector2 end = new Vector2();
		int cp1Index;
		// 0,1 2,3 4,5 6,7 8,9 10,11 12,13 14,15 16,17 18,19
		//     |-|         |-|             |---|
		// 0 1 2 3 4 5 6 7 8 9
		//   |     |     |
		for (int curPath = 0; curPath < nbPaths; curPath++) {
			cp1Index = 3*curPath+1;
			
			cp1.set(pointsArray[cp1Index], 480 - pointsArray[cp1Index + 1]);
			cp2.set(pointsArray[cp1Index + 2], 480 - pointsArray[cp1Index + 3]);
			end.set(pointsArray[cp1Index + 4], 480 - pointsArray[cp1Index + 5]);
			
			pathList.add(new Bezier<Vector2>(start, cp1, cp2, end));
			
			start.set(end);
		}
		
		return pathList;
	}
	
	/**
	 * Crée une liste de chemins en utilisant les chemins fournis
	 * @param paths
	 */
	public ComplexPath(Path<T>... paths) {
		super(paths);
		
		// Mise à jour du tableau de longueurs
		lengths = new Array<Float>(size);
		for (int cur = 0; cur < size; cur++) {
			lengths.set(cur, get(cur).approxLength(APPROX_LENGTH_SAMPLES));
		}
	}
	
	/**
	 * Crée une liste de chemins en utilisant les chemins fournis
	 * @param paths
	 */
	public ComplexPath(Array<Path<T>> paths) {
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
	@Override
	public void add(Path<T> path) {
		// Ajoute le chemin à la liste
		super.add(path);
		// Calcule sa longueur
		lengths.add(path.approxLength(APPROX_LENGTH_SAMPLES));
	}
	
	/**
	 * Retourne le chemin sur lequel se trouve le curseur
	 * @param cursor
	 * @return
	 */
	public Path<T> getPath(ComplexPathCursor<T> cursor) {
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
	public boolean isFinished(ComplexPathCursor<T> cursor) {
		return cursor.getCurrentSegmentIndex() >= size;
	}
	
	/**
	 * Remplit result avec la valeur du segment correspondant
	 * à la position curseur.
	 * @param cursor
	 */
	public void valueAt(T result, ComplexPathCursor<T> cursor) {
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

	/**
	 * Place le curseur sur le chemin à la position la plus proche
	 * des coordonnées indiquées
	 * @param coordinates
	 * @param result
	 */
	public void locate(T coordinates, ComplexPathCursor<T> result) {
		float minT = 1;
		float curT;
		for (int pathIndex = 0; pathIndex < size; pathIndex++) {
			curT = get(pathIndex).locate(coordinates);
			if (curT < minT) {
				result.setPosition(curT);
				result.setCurrentSegmentIndex(pathIndex);
				minT = curT;
			}
		}
	}
}
