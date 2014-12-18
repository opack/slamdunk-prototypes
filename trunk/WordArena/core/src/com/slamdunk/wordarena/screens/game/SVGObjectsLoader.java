package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;

import com.slamdunk.toolkit.svg.converters.SVGPathToBezier;
import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGElementPath;
import com.slamdunk.toolkit.svg.elements.SVGElementRect;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.PathUtils;
import com.slamdunk.wordarena.units.Units;

public class SVGObjectsLoader {
	private static final String SVG_PREFIX_PATH = "PATH";
	private static final String SVG_PREFIX_CASTLE = "CASTLE";
	
	private final WorldObjectsOverlay worldObjectsOverlay;
	private SVGPathToBezier pathConverter;
	private int height;
	
	public SVGObjectsLoader(WorldObjectsOverlay worldObjectsOverlay) {
		this.worldObjectsOverlay = worldObjectsOverlay;
	}

	public void load(SVGRootElement root) {
		// Variables de travail
		height = root.height;
		pathConverter = new SVGPathToBezier(root.height);

		// Itère sur les objets et s'arrête sur ceux ayant un type connu
		loadElements(root);
	}
	
	private void loadElements(SVGElement element) {
		createFromSVG(element);

		ArrayList<SVGElement> children = element.getChildren();
		if (children != null) {
			for (SVGElement child : children) {
				loadElements(child);
			}
		}
	}
	
	private void createFromSVG(SVGElement element) {
		String id = element.getId();
		if (id.startsWith(SVG_PREFIX_PATH)) {
			createPathFromSVG((SVGElementPath)element, pathConverter);
		} else if (id.startsWith(SVG_PREFIX_CASTLE)) {
			createCastleFromSVG((SVGElementRect)element);
		}
	}

	private void createCastleFromSVG(SVGElementRect element) {
		worldObjectsOverlay.spawnUnit(Units.CASTLE, element.x + element.width / 2, height - (element.y + element.height / 2));
	}

	private void createPathFromSVG(SVGElementPath element, SVGPathToBezier pathConverter) {
		ComplexPath path = PathUtils.parseSVG(element, pathConverter);
		worldObjectsOverlay.getPaths().add(path);
	}
}
