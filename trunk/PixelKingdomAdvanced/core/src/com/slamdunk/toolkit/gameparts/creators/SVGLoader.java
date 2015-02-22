package com.slamdunk.toolkit.gameparts.creators;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.svg.SVGParse;
import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;

public class SVGLoader {
	public interface SVGLoadListener {
		void svgRootLoaded(SVGRootElement root);
		void svgElementLoaded(SVGElement element);
	}
	
	private int height;
	private Array<SVGLoadListener> listeners;
	
	public SVGLoader() {
		listeners = new Array<SVGLoadListener>();
	}
	
	public void load(String file) {
		// Parsing du SVG
		SVGParse parser = new SVGParse(Gdx.files.internal(file));
		SVGRootElement root = new SVGRootElement();
		parser.parse(root);
		
		// Chargement des objets
		load(root);
	}
	
	public void load(SVGRootElement root) {
		// Variables de travail
		height = root.height;
		
		for (SVGLoadListener listener : listeners) {
			listener.svgRootLoaded(root);
		}

		// Itère sur les objets et s'arrête sur ceux ayant un type connu
		loadElements(root);
	}
	
	private void loadElements(SVGElement element) {
		for (SVGLoadListener listener : listeners) {
			listener.svgElementLoaded(element);
		}

		ArrayList<SVGElement> children = element.getChildren();
		if (children != null) {
			for (SVGElement child : children) {
				loadElements(child);
			}
		}
	}

	public void addListener(SVGLoadListener listener) {
		listeners.add(listener)	;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Convertit une coordonnée Y dans un système où l'origine
	 * est en haut (ex : SVG) vers une coordonnée Y dans un système
	 * où l'origine est en bas (ex : LibGDX par défaut) 
	 * @param yDownCoordinate
	 * @return
	 */
	public float convertToYUp(float yDownCoordinate) {
		return height - yDownCoordinate;
	}
}
