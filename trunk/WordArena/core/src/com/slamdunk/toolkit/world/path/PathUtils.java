package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.svg.converters.SVGPathToBezier;
import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGElementPath;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;

public class PathUtils {
	/**
	 * Crée une liste de chemins de type Bezier cubic, c'est-à-dire
	 * où chaque courbe de Bézier est définie par 4 points, chaque
	 * point terminant une courbe étant le point de départ de la
	 * courbe suivante.
	 * @param pointsArray
	 */
	public static ComplexPath createCubicComplexPath(int nbPaths, float... pointsArray) {
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
	 * Simplifie la courbe de Bézier fournie en créant une liste
	 * de Path<Vector2> composée uniquement de lignes droites. 
	 * @param bezier
	 * @param samples Nombre de subdivisions de t
	 * @return
	 */
	public static Path<Vector2>[] simplify(Bezier<Vector2> bezier, int samples) {
		final float increment = 1f / samples;
		@SuppressWarnings("unchecked")
		Path<Vector2>[] simplified = new Path[samples];
		
		float curT = 0;
		float nextT = increment;
		Vector2 p1 = new Vector2();
		bezier.valueAt(p1, curT);
		Vector2 p2;
		for (int curSample = 0; curSample < samples; curSample++) {
			// Récupération du 2è point
			p2 = new Vector2();
			bezier.valueAt(p2, nextT);
			
			// Création de la courbe simplifiée (donc de la ligne)
			simplified[curSample] = new Bezier<Vector2>(p1, p2);
			
			// On passe au sample suivant. Le premier point du prochain sample
			// est le dernier du sample courant.
			curT = nextT;
			nextT += increment;
			p1 = new Vector2(p2);
		}
		return simplified;
	}
	
	/**
	 * Retourne un curseur sur le chemin (parmi la liste de chemins fournie) qui se trouve le
	 * plus proche de la position indiquée.
	 * @param paths
	 * @param testPos
	 * @return null si aucun chemin n'est suffisamment près pour être sélectionné
	 */
	public static ComplexPathCursor selectNearestPath(Array<ComplexPath> paths, Vector2 testPos) {
		return selectNearestPath(paths, testPos, -1, null);
	}
	
	/**
	 * Retourne un curseur sur le chemin (parmi la liste de chemins fournie) qui se trouve le
	 * plus proche de la position indiquée.
	 * @param paths
	 * @param testPos
	 * @param tolerance Distance maximale qu'on autorise entre le chemin sélectionné et la
	 * position testée. Si -1, la tolérance n'est pas prise en compte
	 * @return null si aucun chemin n'est suffisamment près pour être sélectionné
	 */
	public static ComplexPathCursor selectNearestPath(Array<ComplexPath> paths, Vector2 testPos, int tolerance) {
		return selectNearestPath(paths, testPos, tolerance, null);
	}

	/**
	 * Retourne un curseur sur le chemin (parmi la liste de chemins fournie) qui se trouve le
	 * plus proche de la position indiquée.
	 * @param paths
	 * @param testPos
	 * @param tolerance Distance maximale qu'on autorise entre le chemin sélectionné et la
	 * position testée. Si -1, la tolérance n'est pas prise en compte
	 * @param model Curseur actuellement utilisé. Il sert de modèle pour la création
	 * du curseur retourné afin d'obtenir la vitesse et le mode de parcours. Si null,
	 * la vitesse du curseur retourné sera 1 et le mode de parcours sera CursorMode.FORWARD.
	 * @return null si aucun chemin n'est suffisamment près pour être sélectionné
	 */
	public static ComplexPathCursor selectNearestPath(Array<ComplexPath> paths, Vector2 testPos, int tolerance, ComplexPathCursor model) {
		if (paths == null
		|| paths.size == 0) {
			return null;
		}
		
		float speed = 1f;
		CursorMode mode = CursorMode.FORWARD;
		if (model != null) {
			speed = model.getSpeed();
			mode = model.getMode();
		}
		
		float minDistance = -1;
		float distance;
		ComplexPathCursor nearestCursor = null;
		ComplexPath path;
		ComplexPathCursor probe;
		for (int pathIndex = 0; pathIndex < paths.size; pathIndex++) {
			// Récupère le curseur du point le plus proche sur ce chemin
			path = paths.get(pathIndex);
			probe = new ComplexPathCursor(path, speed, mode);
			distance = path.locate(testPos, probe);
			
			// Regarde s'il est plus près que les autres curseurs
			if (nearestCursor == null
			|| distance < minDistance) {
				minDistance = distance;
				nearestCursor = probe;
			}
		}
		
		// Si la position la plus proche d'un chemin est à une distance supérieure à la tolérance
		// qu'on s'est fixé, alors on considère qu'aucun chemin n'est sélectionnable
		if (tolerance != -1
		&& minDistance > tolerance) {
			nearestCursor = null;
		}
		return nearestCursor;
	}
	
	/**
	 * Parse la racine SVG indiquée et crée une liste de ComplexPath à partir
	 * des chemins trouvés dans la couche indiquée
	 * @param file
	 * @param layer
	 */
	public static Array<ComplexPath> parseSVG(SVGRootElement root, String layer) {
		Array<ComplexPath> parsedPaths = new Array<ComplexPath>();
		SVGElement svgPaths = root.getChildById(layer);
		SVGPathToBezier converter = new SVGPathToBezier(root.height);
		for (SVGElement child : svgPaths.getChildren()) {
			if("path".equals(child.getName())) {
				// Crée un ComplexPath à partir des infos du SVG
				ComplexPath path = parseSVG((SVGElementPath)child, converter);
				
				// Ajoute le ComplexPath à la liste
				parsedPaths.add(path);
			}
		}
		
		return parsedPaths;
	}
	
	/**
	 * Parse la racine SVG indiquée et crée une liste de ComplexPath à partir
	 * des chemins trouvés dans la couche indiquée
	 * @param file
	 * @param layer
	 */
	public static ComplexPath parseSVG(SVGElementPath svgPath, int svgHeight) {
		return parseSVG(svgPath, new SVGPathToBezier(svgHeight));
	}
	
	/**
	 * Parse la racine SVG indiquée et crée une liste de ComplexPath à partir
	 * des chemins trouvés dans la couche indiquée
	 * @param file
	 * @param layer
	 */
	public static ComplexPath parseSVG(SVGElementPath svgPath, SVGPathToBezier converter) {
		// Convertit les chemins SVG en liste de chemins de Bézier
		Array<Bezier<Vector2>> beziers = converter.convert(svgPath);
		
		// A partir de ces chemins de Bézier, on crée un ComplexPath
		ComplexPath path = new ComplexPath();
		for (Bezier<Vector2> bezier : beziers) {
			// Si le chemin contient plus de 2 points, alors c'est une courbe de Bézier
			// cubique ou quadratique. On la simplifie en liste de simples chemins
			// linéaires
			if (bezier.points.size > 2) {
				path.addAll(PathUtils.simplify(bezier, (int)(bezier.approxLength(500) / 10)));
			} else {
				path.add(bezier);
			}
		}
		
		// Nomme les extrémités pour déterminer les sens de parcours des différentes unités
		path.setExtremity0(svgPath.getExtraAttribute("end1name"));
		path.setExtremity1(svgPath.getExtraAttribute("end2name"));
		
		return path;
	}
	
	/**
	 * Retourne la valeur t de l'extrémité du chemin qui a le nom
	 * indiqué.
	 * @return -1 si aucune extrémité ne correspond, sinon 0 ou 1
	 * suivant l'extrémité 
	 */
	public static float getExtremityByName(ComplexPath path, String name) {
		float result = -1;
		
		String extremity0 = path.getExtremity0();
		String extremity1 = path.getExtremity1();
		// Si le paramètre fourni est valide...
		if (name != null && !name.isEmpty()) {
			// Si l'extremité 0 est renseignée
			if (extremity0 != null
			&& !extremity0.isEmpty()
			// et qu'elle correspond au paramètre
			&& name.equals(extremity0)) {
				// Alors l'extrémité correspondante est celle pour t=0
				result = 0;
			}
			// Si l'extrémité 1 est renseignée
			else if (extremity1 != null
			&& !extremity1.isEmpty()
			// et qu'elle correspond au paramètre
			&& name.equals(extremity1)) {
				// Alors l'extrémité correspondante est celle pour t=1
				result = 1;
			}
		}
		
		return result;
	}
}
