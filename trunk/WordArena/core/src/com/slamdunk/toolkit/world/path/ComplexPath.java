package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Liste de chemins qu'on peut suivre pour passer de l'un à l'autre.
 * Cet objet peut être partagé entre plusieurs objets grâce à
 * l'utilisation d'un PathCursor, qui peut "se déplacer" sur le chemin.
 * 
 * Ce chemin complexe est séparé en plusieurs chemins, que l'on appelle
 * des segments.
 * Chaque segment peut être traité indépendemment et les valeurs qui
 * le composent peuvent être lues grâce au paramètre t, variant entre
 * 0 et 1, comme expliqué dans Path.
 * Ce paramètre t a aussi un sens pour l'ensemble du ComplexPath,
 * et la valeur entre 0 et 1 peut donc adresser un segment différent.
 * Lorsque t est utilisée pour un segment, on parlera de "t local" (localT),
 * en opposition au "t global" (globalT) qui est un indice pour la globalité
 * du ComplexePath.
 */
public class ComplexPath extends Array<Path<Vector2>> {
	private static final int APPROX_LENGTH_SAMPLES = 500;
	
	private class SegmentData {
		/**
		 * Longueur du segment
		 */
		float length;
		
		/**
		 * Valeur min de globalT pour ce segment. C'est
		 * donc la valeur de globalT pour localT = 0.
		 */
		float globalTmin;
		
		/**
		 * Valeur max de globalT pour ce segment. C'est
		 * donc la valeur de globalT pour localT = 1.
		 */
		float globalTmax;
	}
	
	private Array<SegmentData> segmentsData;
	
	/**
	 * Longueur totale du ComplexPath
	 */
	private float totalLength;
	
	/**
	 * Flag qui indique s'il faut recalculer les intervals
	 * de valeurs de globalT pour chaque segment
	 */
	private boolean updateGlobalTIntervals;
	
	
	public ComplexPath() {
		segmentsData = new Array<SegmentData>();
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
		this();
		
		if (points.length < 2) {
			throw new IllegalArgumentException("The points list must contain at least 2 values.");
		}
		// Crée les chemins puis les ajoute en mettant à jour le tableau des longueurs
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
		this();
		
		for (Vector2[] pointsArray : pointsArrays) {
			add(new Bezier<Vector2>(pointsArray));
		}
	}
	
	/**
	 * Crée une liste de chemins en utilisant les chemins fournis
	 * @param paths
	 */
	public ComplexPath(Path<Vector2>... paths) {
		super(paths);
		
		// Mise à jour du tableau de longueurs
		segmentsData = new Array<SegmentData>(size);
		SegmentData data;
		for (int cur = 0; cur < size; cur++) {
			data = new SegmentData();
			data.length = get(cur).approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.set(cur, data);
			totalLength += data.length;
		}
		
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
	}
	
	/**
	 * Crée une liste de chemins en utilisant les chemins fournis
	 * @param paths
	 */
	public ComplexPath(Array<Path<Vector2>> paths) {
		super(paths);
		
		// Mise à jour du tableau de longueurs
		segmentsData = new Array<SegmentData>(size);
		SegmentData data;
		for (int cur = 0; cur < size; cur++) {
			data = new SegmentData();
			data.length = get(cur).approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.set(cur, data);
			totalLength += data.length;
		}
		
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
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
		SegmentData data = new SegmentData();
		data.length = path.approxLength(APPROX_LENGTH_SAMPLES);
		segmentsData.add(data);
		totalLength += data.length;
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
	}
	
	@Override
	public void addAll(Array<? extends Path<Vector2>> array) {
		super.addAll(array);
		
		// Calcule la longueur des chemins
		for (Path<Vector2> path : array) {
			SegmentData data = new SegmentData();
			data.length = path.approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.add(data);
			totalLength += data.length;
		}
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
	}
	
	/**
	 * Ajoute de nouveaux chemins à la liste et met à jour le
	 * tableau des longueurs
	 * @param path
	 */
	@Override
	public void addAll(Array<? extends Path<Vector2>> array, int offset, int length) {
		super.addAll(array, offset, length);
		
		// Calcule la longueur des chemins
		final int lastPath = offset + length;
		Path<Vector2> path;
		for (int curPath = offset; curPath < lastPath; curPath++) {
			path = array.get(curPath);
			
			SegmentData data = new SegmentData();
			data.length = path.approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.add(data);
			totalLength += data.length;
		}
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
	}
	
	/**
	 * Ajoute de nouveaux chemins à la liste et met à jour le
	 * tableau des longueurs
	 * @param path
	 */
	@Override
	public void addAll(Path<Vector2>... array) {
		super.addAll(array);
		
		// Calcule la longueur des chemins
		for (Path<Vector2> path : array) {
			SegmentData data = new SegmentData();
			data.length = path.approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.add(data);
			totalLength += data.length;
		}
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
	}
	
	/**
	 * Ajoute de nouveaux chemins à la liste et met à jour le
	 * tableau des longueurs
	 * @param path
	 */
	@Override
	public void addAll(Path<Vector2>[] array, int offset, int length) {
		super.addAll(array, offset, length);
		
		// Calcule la longueur des chemins
		final int lastPath = offset + length;
		Path<Vector2> path;
		for (int curPath = offset; curPath < lastPath; curPath++) {
			path = array[curPath];
			
			SegmentData data = new SegmentData();
			data.length = path.approxLength(APPROX_LENGTH_SAMPLES);
			segmentsData.add(data);
			totalLength += data.length;
		}
		// Il faudra mettre à jour le tableau des intervals de globalT
		updateGlobalTIntervals = true;
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
	 * Remplit result avec la position correspondant à 
	 * la position t. Ici, t est global, c'est-à-dire 
	 * que t fait référence non pas à un segment mais
	 * à l'ensemble du chemin complexe
	 * @param result
	 * @param t
	 */
	public void valueAt(float globalT, Vector2 result) {
		// Récupère l'indice du segment correspondant à ce t
		int segmentIndex = getSegmentIndexFromGlobalT(globalT);
		
		// Récupère la valeur de t localisée à ce segment
		float localT = convertToLocalT(globalT, segmentIndex);
		
		// Récupère le segment correspondant et la valeur demandée
		Path<Vector2> path = get(segmentIndex);
		path.valueAt(result, localT);
	}

	/**
	 * Retourne une valeur localT (propre au chemin d'indice segmentIndex)
	 * à partir d'une valeur globalT
	 * @param globalT
	 * @param segmentIndex
	 * @return
	 */
	public float convertToLocalT(float globalT, int segmentIndex) {
		// Recalcule les intervals de globalT si nécessaire
		if (updateGlobalTIntervals) {
			updateGlobalTIntervals();
		}
		
		// On replace l'interval à partir de 0 au lieu de min,
		// puis on fait un bête calcul de pourcentage pour savoir
		// ou se situe globalT (décalée de -min aussi) par rapport
		// au max (décalé de -min).
		// c'est donc notre valeur de localT, entre 0 et 1.
		final SegmentData data = segmentsData.get(segmentIndex);
		return (globalT - data.globalTmin) / (data.globalTmax - data.globalTmin);
	}

	/**
	 * Retourne l'indice du segment qui se trouve à la position
	 * globalT indiquée
	 * @param globalT
	 * @return
	 */
	public int getSegmentIndexFromGlobalT(float globalT) {
		// Recalcule les intervals de globalT si nécessaire
		if (updateGlobalTIntervals) {
			updateGlobalTIntervals();
		}
		
		int segmentIndex = 0;
		for (; segmentIndex < size; segmentIndex++) {
			if (globalT < segmentsData.get(segmentIndex).globalTmax) {
				break;
			}
		}
		// Si globalT==1, alors c'est forcément le dernier segment
		// qui sera choisi, ce qui nous va très bien
		return segmentIndex;
	}
	
	/**
	 * Recalcule le tableau des intervals de valeurs de globalT
	 * pour chaque segment
	 */
	private void updateGlobalTIntervals() {
		float globalTmax = 0;
		SegmentData data;
		for (int segmentIndex = 0; segmentIndex < size; segmentIndex++) {
			data = segmentsData.get(segmentIndex);
			
			// Pour ce segment, le min est le précédent max
			data.globalTmin = globalTmax;
			// On déplace le max vers la fin du segment
			globalTmax += 1 / totalLength * data.length;
			data.globalTmax = globalTmax;
		}
		
		// La mise à jour est termnée !
		updateGlobalTIntervals = false;
	}

	/**
	 * Retourne la taille (approximée) du segment indiqué
	 * @param current
	 * @return
	 */
	public float getLength(int segmentIndex) {
		return segmentsData.get(segmentIndex).length;
	}
	
	/**
	 * Retourne la taille total (approximée) du ComplexPath entier
	 * @param current
	 * @return
	 */
	public float getLength() {
		return totalLength;
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
