package com.slamdunk.wordarena.gameparts.creators;

import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.gameparts.creators.SVGLoader;
import com.slamdunk.toolkit.gameparts.creators.SVGLoader.SVGLoadListener;
import com.slamdunk.toolkit.svg.converters.SVGPathToBezier;
import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGElementPath;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.PathUtils;

public class CustomSVGLoader extends SVGLoader implements SVGLoadListener {
	private static final String SVG_PREFIX_PATH = "PATH";
//	private static final String SVG_PREFIX_CASTLE = "CASTLE";
	
	private SVGPathToBezier pathConverter;
	public Array<ComplexPath> paths;
	
	public CustomSVGLoader(String svgFile) {
		addListener(this);
		
		load(svgFile);
	}
	

	@Override
	public void svgRootLoaded(SVGRootElement root) {
		pathConverter = new SVGPathToBezier(root.height);
		paths = new Array<ComplexPath>();
	}
	
	@Override
	public void svgElementLoaded(SVGElement element) {
		String id = element.getId();
		if (id.startsWith(SVG_PREFIX_PATH)) {
			createPathFromSVG((SVGElementPath)element, pathConverter);
//		} else if (id.startsWith(SVG_PREFIX_CASTLE)) {
//			createCastleFromSVG((SVGElementRect)element);
		}
	}
	
//	private void createCastleFromSVG(SVGElementRect element) {
//	worldObjectsOverlay.spawnUnit(
//		Units.CASTLE,
//		Factions.valueOf(element.getExtraAttribute("faction")),
//		element.x + element.width / 2,
//		height - (element.y + element.height / 2));
//}

	private void createPathFromSVG(SVGElementPath element, SVGPathToBezier pathConverter) {
		ComplexPath path = PathUtils.parseSVG(element, pathConverter);
		paths.add(path);
	}
}
