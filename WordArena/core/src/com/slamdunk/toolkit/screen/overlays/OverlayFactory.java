package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Usine capable de cr�er tout un tas d'overlays
 * @author Didier
 *
 */
public class OverlayFactory {
	public static final String DEFAULT_SKIN = "skins/uiskin/uiskin.json";
	
	/**
	 * Cr�e un overlay destin� � afficher le monde
	 */
	public static WorldOverlay createWorldOverlay() {
		// Cr�ation de la couche
		WorldOverlay worldOverlay = new WorldOverlay();
		worldOverlay.createStage(new ScreenViewport());
		return worldOverlay;
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionn� � l'emplacement indiqu� et aura la taille indiqu�e.
	 */
	public static MiniMapOverlay createMiniMapOverlay(Viewport viewport) {
		// Cr�ation de la couche
		MiniMapOverlay minimapOverlay = new MiniMapOverlay();
		minimapOverlay.createStage(viewport);
		return minimapOverlay;
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay s'�talera sur toute la surface de l'�cran.
	 */
	public static MiniMapOverlay createMiniMapOverlay() {
		return createMiniMapOverlay(new ScreenViewport());
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionn� � l'emplacement indiqu� et aura la taille indiqu�e.
	 */
	public static UIOverlay createUIOverlay(Viewport viewport) {
		// Cr�ation de la couche
		UIOverlay uiOverlay = new UIOverlay();
		Skin uiSkin = new Skin(Gdx.files.internal(DEFAULT_SKIN));
		uiOverlay.setSkin(uiSkin);
		uiOverlay.createStage(viewport);
		return uiOverlay;
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay s'�talera sur toute la surface de l'�cran.
	 */
	public static UIOverlay createUIOverlay() {
		return createUIOverlay(new ScreenViewport());
	}
}
