package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Liste de chemins qu'on peut suivre pour passer de l'un à l'autre.
 * Cet objet peut être partagé entre plusieurs objets grâce à
 * l'utilisation d'un PathCursor, qui peut "se déplacer" sur le chemin.
 */
public class ComplexPath extends Array<Path<Vector2>> {
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
	public ComplexPath(boolean closePath, Vector2... points) {
		if (points.length < 2) {
			throw new IllegalArgumentException("The points list must contain at least 2 values.");
		}
		// Crée les chemins puis les ajoute en mettant à jour le tableau des longueurs
		lengths = new Array<Float>(size);
		for (int cur = 0; cur < points.length - 1; cur++) {
			add(new Bezier<Vector2>(points, cur, 2));
		}
		if (closePath) {
			add(new Bezier<Vector2>(points[points.length - 1], points[0]));
		}
	}
	
	/**
	 * Crée une liste de chemins dont chaque chemin passe par les points
	 * d'un item du tableau pointsArrays.
	 * @param pointsArrays
	 */
	public ComplexPath(Vector2[]... pointsArrays) {
		lengths = new Array<Float>(size);
		for (Vector2[] pointsArray : pointsArrays) {
			add(new Bezier<Vector2>(pointsArray));
		}
	}
	
	/**
	 * Crée une liste de chemins de type Bezier cubic, c'est-à-dire
	 * où chaque courbe de Bézier est définie par 4 points, chaque
	 * point terminant une courbe étant le point de départ de la
	 * courbe suivante.
	 * @param pointsArray
	 */
	public static ComplexPath createCubicBezierPathList(int nbPaths, float... pointsArray) {
		if ((nbPaths > 1 && pointsArray.length < 4*nbPaths-1)
		|| (nbPaths == 1 && pointsArray.length < 4)) {
			throw new IllegalArgumentException("Le nombre de points fournis (" + pointsArray.length + ")ne permet pas de construire " + nbPaths + " chemins.");
		}
		ComplexPath pathList = new ComplexPath();
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
	public ComplexPath(Path<Vector2>... paths) {
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
	public ComplexPath(Array<Path<Vector2>> paths) {
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
	public void add(Path<Vector2> path) {
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
	public Path<Vector2> getPath(ComplexPathCursor cursor) {
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
	public boolean isFinished(ComplexPathCursor cursor) {
		return cursor.getCurrentSegmentIndex() >= size;
	}
	
	/**
	 * Remplit result avec la valeur du segment correspondant
	 * à la position curseur.
	 * @param cursor
	 */
	public void valueAt(ComplexPathCursor cursor, Vector2 result) {
		Path<Vector2> path = getPath(cursor);
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
	public void valueAt(int segmentIndex, float t, Vector2 result) {
		Path<Vector2> path = get(segmentIndex);
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
	public float locate(Vector2 coordinates, ComplexPathCursor result) {
		return locate(coordinates, result, null);
	}
	
	/**
	 * Place le curseur sur le chemin à la position la plus proche
	 * des coordonnées indiquées
	 * @param coordinates
	 * @param result
	 * @return distance entre le point demandé et le point du chemin le plus proche trouvé
	 */
	public float locate(Vector2 coordinates, ComplexPathCursor result, Vector2 nearestCoordsOnPath) {
		Vector2 tmpNearestCoords = new Vector2();
		float minDistance = -1;
		float distance;
		Path<Vector2> path;
		float curT;
		for (int pathIndex = 0; pathIndex < size; pathIndex++) {
			// Récupère la position la plus proche des
			// coordonnées indiquées sur le segment actuel
			path = get(pathIndex);
			curT = path.locate(coordinates);			
			path.valueAt(tmpNearestCoords, curT);
			
			// Vérifie si la distance est plus faible que des segments précédents
			distance = tmpNearestCoords.dst(coordinates);
			if (minDistance == -1
			|| distance < minDistance) {
				// Mise à jour de la distance entre le point recherché
				// et le point trouvé sur le segment
				minDistance = distance;
				
				// Mise à jour du curseur avec la position la plus proche
				result.setPosition(curT);
				result.setCurrentSegmentIndex(pathIndex);
				
				// Si on souhaite récupérer la position sur le chemin qui est 
				// la plus proche du point cliqué, alors on met à jour ce Vector2
				if (nearestCoordsOnPath != null) {
					nearestCoordsOnPath.set(tmpNearestCoords);
				}
			}
		}
		return minDistance;
	}
}
