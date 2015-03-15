package com.slamdunk.toolkit.svg;

import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.svg.elements.SVGElement;

public class SVGUtils {

	/**
	 * Retourne le vecteur de translation combinant les transformations de
	 * translation de tous les ancêtres de l'objet indiqué
	 * @param svg
	 * @return
	 */
	public static Vector2 computeAncestorsTranslations(SVGElement svg) {
		Vector2 translate = new Vector2(0, 0);
		SVGElement parent = svg;
		SVGTransform[] transforms;
		do {
			transforms = parent.getTransforms();
			if (transforms != null) {
				for (SVGTransform transform : transforms) {
					if (SVGConstants.ATTRIBUTE_TRANSFORM_VALUE_TRANSLATE.equals(transform.name)) {
						translate.add(transform.values[0], transform.values[1]);
					}
				}
			}
			parent = parent.getParent();
		} while (parent != null);
		return translate;
	}

}
