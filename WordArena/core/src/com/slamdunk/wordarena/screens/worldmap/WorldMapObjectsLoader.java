package com.slamdunk.wordarena.screens.worldmap;

import java.util.ArrayList;

import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGElementRect;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;

public class WorldMapObjectsLoader {
	private static final String SVG_PREFIX_MISSION = "MISSION";
	
	private WorldMapOverlay worldMapOverlay;
	private int height;
	
	public WorldMapObjectsLoader(WorldMapOverlay worldMapOverlay) {
		this.worldMapOverlay = worldMapOverlay;
	}
	
	public void load(SVGRootElement root) {
		// Variables de travail
		height = root.height;

		// Itère sur les objets et s'arrête sur ceux ayant un type connu
		loadElements(root);
	}
	
	private void loadElements(SVGElement element) {
		createFromSVG(element);

		ArrayList<SVGElement> children = element.getChildren();
		if (children != null) {
			for (SVGElement child : children) {
				// Pour une raison qui m'échappe, il se peut qu'un élément
				// ait comme enfant... lui-même ! Cela résulte dans une
				// StackOverflowException donc on évite ça
				if (child != element) {
					loadElements(child);
				}
			}
		}
	}
	
	private void createFromSVG(SVGElement element) {
		String id = element.getId();
		if (id.startsWith(SVG_PREFIX_MISSION)) {
			createMissionFromSVG((SVGElementRect)element);
		}
	}

	private void createMissionFromSVG(SVGElementRect element) {
		worldMapOverlay.createMissionButton(
			element.x + element.width / 2,
			height - (element.y + element.height / 2),
			element.getExtraAttribute("properties"));
	}
}
