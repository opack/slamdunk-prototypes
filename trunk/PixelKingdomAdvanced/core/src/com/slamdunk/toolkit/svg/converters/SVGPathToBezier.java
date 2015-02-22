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
public class SVGPathToBezier {
	private Array<Bezier<Vector2>> paths;
	private boolean isRelative;
	private Vector2 startSVGCoords;
	
	/**
	 * Tableau de coordonnées en cours de traitement
	 */
	private float[] coordinates;
	
	private int svgHeight;
	
	private Vector2 ancestorsTranslations;
	
	public SVGPathToBezier(int svgHeight) {
		this.svgHeight = svgHeight;
	}

	public Array<Bezier<Vector2>> convert(SVGElementPath svg) {
		paths = new Array<Bezier<Vector2>>();
		
		ancestorsTranslations = SVGUtils.computeAncestorsTranslations(svg);
		
		for (SVGPath pathData : svg.path) {
			// Si la commande est en miniscule, alors les coordonnées sont relatives à l'origine
			isRelative = pathData.command == 'm' || pathData.command == 'c' || pathData.command == 'l';
			
			// Traitement des commandes
			coordinates = pathData.coordinates;
			switch (pathData.command) {
			// moveTo
			case 'm':
			case 'M':
				handleMoveTo();
				break;
			case 'c':
			case 'C':
				handleCurveTo();
				break;
			case 'l':
			case 'L':
				handleLineTo();
				break;
			}
		}
		return paths;
	}

	private void handleMoveTo() {
		// La commande définit le début d'un nouveau sous-chemin
		startSVGCoords = new Vector2();
		extractPoint(0, ancestorsTranslations, startSVGCoords);
		
		// S'il y a encore des points, alors c'est qu'on a un lineTo implicite
		if (coordinates.length > 2) {
			handleLineTo(1);
		}
	}
	
	private void handleLineTo() {
		handleLineTo(0);
	}

	/**
	 * 
	 * @param pointOffset Nombre de points à sauter au début du tableau. Utile uniquement
	 * lorsque cette méthode est appelée depuis handleMoveTo car dans ce cas le premier
	 * point du tableau ne doit pas être lu car on est sur le tableau de coords du moveTo
	 */
	private void handleLineTo(int pointOffset) {
		final Vector2 svgFirstPoint = new Vector2(startSVGCoords);
		final Vector2 svgSecondPoint = new Vector2();
		
		Vector2 yUpFirstPoint;
		Vector2 yUpSecondPoint;
		for (int curCoord = pointOffset * 2; curCoord < coordinates.length; curCoord += 2) {
			// Extraction du second point
			extractPoint(curCoord, startSVGCoords, svgSecondPoint);
			
			// Transformation des coordonnées SVG en coordonnées y-up
			yUpFirstPoint = flipY(svgFirstPoint);
			yUpSecondPoint = flipY(svgSecondPoint);
			
			// Création de la ligne
			paths.add(new Bezier<Vector2>(yUpFirstPoint, yUpSecondPoint));
			
			// Pour la prochaine ligne, le premier point est le même que le second
			svgFirstPoint.set(svgSecondPoint);

			// Les coordonnées par rapport auxquelles doivent être relatives les autres
			// sont celles du dernier point, mais attention : dans le système du SVG !
			startSVGCoords.set(svgSecondPoint);
		}
	}
	

	private void handleCurveTo() {
		final Vector2 svgFirstPoint = new Vector2(startSVGCoords);
		final Vector2 svgControlPoint1 = new Vector2();
		final Vector2 svgControlPoint2 = new Vector2();
		final Vector2 svgSecondPoint = new Vector2();
		
		Vector2 yUpFirstPoint;
		Vector2 yUpControlPoint1;
		Vector2 yUpControlPoint2;
		Vector2 yUpSecondPoint;
		for (int curCoord = 0; curCoord < coordinates.length; curCoord += 6) {
			// Extraction des autres points
			extractPoint(curCoord, startSVGCoords, svgControlPoint1);
			extractPoint(curCoord + 2, startSVGCoords, svgControlPoint2);
			extractPoint(curCoord + 4, startSVGCoords, svgSecondPoint);
			
			// Transformation des coordonnées SVG en coordonnées y-up
			yUpFirstPoint = flipY(svgFirstPoint);
			yUpControlPoint1 = flipY(svgControlPoint1);
			yUpControlPoint2 = flipY(svgControlPoint2);
			yUpSecondPoint = flipY(svgSecondPoint);
			
			// Création de la ligne
			paths.add(new Bezier<Vector2>(yUpFirstPoint, yUpControlPoint1, yUpControlPoint2, yUpSecondPoint));
			
			// Pour la prochaine courbe, le premier point est le même que le second
			svgFirstPoint.set(svgSecondPoint);
			
			// Les coordonnées par rapport auxquelles doivent être relatives les autres
			// sont celles du dernier point, mais attention : dans le système du SVG !
			startSVGCoords.set(svgSecondPoint);
		}
	}

	/**
	 * Transforme les coordonnées du point indiqué pour le placer dans un système
	 * ou le y=0 est à l'opposé de sa position actuelle.
	 * Par exemple si on avait des coordonnées SVG, alors y=0 est en haut, et en sortie on aura
	 * une ordonnée considérant y=0 en bas.
	 * @param point
	 */
	private Vector2 flipY(Vector2 source) {
		return new Vector2(source.x, svgHeight - source.y);
	}

	/**
	 * Extrait le point correspondant au curseur de coordonnée indiqué.
	 * Si on est sur des coordonnées relatives, on ajoute la position du premier
	 * point.
	 * @param curCoord
	 * @param firstPoint
	 * @return
	 */
	private Vector2 extractPoint(int curCoord, Vector2 firstPoint, Vector2 out) {
		out.set(coordinates[curCoord], coordinates[curCoord + 1]);
		if (isRelative) {
			// Si les coordonnées sont relatives, alors on ajoute les coordonées du premier point
			out.add(firstPoint);
		}
		
		return out;
	}
	
}
