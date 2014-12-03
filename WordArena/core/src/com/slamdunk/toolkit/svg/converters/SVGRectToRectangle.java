package com.slamdunk.toolkit.svg.converters;

import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.toolkit.svg.elements.SVGElementRect;

/**
 * Crée un objet Rectangle à partir des infos contenues dans un SVGElementRect
 */
public class SVGRectToRectangle {
	public Rectangle convert(SVGElementRect svg) {
		return new Rectangle((float)svg.x, (float)svg.y, (float)svg.width, (float)svg.height);
	}
}
