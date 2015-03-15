package com.slamdunk.toolkit.svg.converters;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.svg.SVGPath;
import com.slamdunk.toolkit.svg.SVGUtils;
import com.slamdunk.toolkit.svg.elements.SVGElementPath;

/**
 * Crée un objet Bezier à partir des infos contenues dans un SVGElementPath
 * http://www.w3.org/TR/SVG/paths.html#PathData
 */
public class SVGPathToBezierOld {
	private Array<Bezier<Vector2>> beziers;
	private Vector2[] points;
	private int curPoint;
	private boolean isRelative;
	private Vector2 relativeOrigin;
	private float svgHeight;
	
	public SVGPathToBezierOld(float svgHeight) {
		this.svgHeight = svgHeight;
	}
	
	public Array<Bezier<Vector2>> convert(SVGElementPath svg) {
		beziers = new Array<Bezier<Vector2>>();
		points = new Vector2[] {new Vector2(), new Vector2(), new Vector2(), new Vector2()};
		
		relativeOrigin = SVGUtils.computeAncestorsTranslations(svg);
		curPoint = 0;
		
		for (SVGPath path : svg.path) {
			// Si la commande est en miniscule, alors les coordonnées sont relatives à l'origine
			isRelative = path.command == 'm' || path.command == 'c' || path.command == 'l';
			
			switch (path.command) {
				// Le moveTo initialise la position de la courbe. Il peut éventuellement contenir
				// plus d'1 point : on a alors une commande lineTo implicite
				case 'm':
				case 'M':
					relativeOrigin.set(path.coordinates[0], path.coordinates[1]);
					points[curPoint].set(relativeOrigin.x, svgHeight - relativeOrigin.y);
					curPoint++;
					
					// S'il y a d'autres points dans la commande m, alors ils doivent être traités
					// comme les points d'une ligne
					if (path.coordinates.length > 2) {
						extractLines(path.coordinates);
					}
				break;
				// Le curveTo crée les points de la courbe, en relatif par rapport au point précédent ou en absolu
				case 'c':
				case 'C':
					extractCurves(path.coordinates);
					break;
				// Le lineTo crée les points d'un la polyline, en relatif par rapport au point précédent ou en absolu
				case 'l':
				case 'L':
					extractLines(path.coordinates);
					break;
			}
		}
		return beziers;
	}

	/**
	 * Extrait les lignes des coordonnées indiquées
	 * @param coords
	 */
	private void extractLines(float[] coords) {
		extractBezier(2, coords);
	}
	
	/**
	 * Extrait les courbes des coordonnées indiquées
	 * @param coords
	 */
	private void extractCurves(float[] coords) {
		extractBezier(4, coords);
	}

	/**
	 * Ajoute les points dans une courbe de Bézier puis ajoute la courbe à la liste des courbes
	 * @param isRelative Indique si les coordonnées sont absolues (false) ou relatives au premier
	 * point du segment
	 * @param nbPointsByCurve Nombre de points à mettre dans 1 courbe de Bézier
	 * @param coords Liste des coordonnées
	 */
	private void extractBezier(int nbPointsByCurve, float[] coords) {
		// Ajoute les points
		float x;
		float y;
		for (int curCoord = 0; curCoord < coords.length; curCoord += 2) {
			if (isRelative) {
				x = relativeOrigin.x + coords[curCoord];
				y = relativeOrigin.y + coords[curCoord + 1];
			} else {
				x = coords[curCoord];
				y = coords[curCoord + 1];
			}
			points[curPoint].set(x, svgHeight - y);
			curPoint++;

			// Tous les 3 points (donc toutes les 6 coordonnées)
			if (curPoint == nbPointsByCurve) {
				// On crée une courbe de Bézier
				beziers.add(new Bezier<Vector2>(points, 0, nbPointsByCurve));
				
				// On démarre une nouvelle courbe. La nouvelle origine est le dernier point de la courbe.
				// ATTENTION ! On utilise y et pas svgHeight car on relativeOrigin est dans le système
				// de coordonnées du SVG et pas de libGDX !
				relativeOrigin.set(x, y);
				// Création d'un nouveau tableau de points pour la nouvelle courbe
				points = new Vector2[nbPointsByCurve];
				for (int i = 0; i < nbPointsByCurve; i++) {
					points[i] = new Vector2();
				}
				// Le premier point est le dernier de la précédente
				points[0].set(x, svgHeight - y);
				curPoint = 1;
			}
		}
	}
}
