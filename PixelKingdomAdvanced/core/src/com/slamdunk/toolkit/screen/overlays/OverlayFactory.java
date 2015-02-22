package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Usine capable de créer tout un tas d'overlays
 * @author Didier
 *
 */
public class OverlayFactory {
	
	/**
	 * Crée un overlay destiné à afficher le monde
	 */
	public static WorldOverlay createWorldOverlay(Viewport viewport) {
		// Création de la couche
		WorldOverlay worldOverlay = new WorldOverlay();
		worldOverlay.createStage(viewport);
		return worldOverlay;
	}
	
	/**
	 * Crée un overlay destiné à afficher le monde
	 */
	public static WorldOverlay createWorldOverlay() {
		return createWorldOverlay(new ScreenViewport());
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionné à l'emplacement indiqué et aura la taille indiquée.
	 */
	public static MiniMapOverlay createMiniMapOverlay(Viewport viewport) {
		// Création de la couche
		MiniMapOverlay minimapOverlay = new MiniMapOverlay();
		minimapOverlay.createStage(viewport);
		return minimapOverlay;
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay s'étalera sur toute la surface de l'écran.
	 */
	public static MiniMapOverlay createMiniMapOverlay() {
		return createMiniMapOverlay(new ScreenViewport());
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionné à l'emplacement indiqué et aura la taille indiquée.
	 */
	public static UIOverlay createUIOverlay(Viewport viewport) {
		// Création de la couche
		UIOverlay uiOverlay = new UIOverlay();
		uiOverlay.setSkin(new Skin(Gdx.files.internal(UIOverlay.DEFAULT_SKIN)));
		uiOverlay.createStage(viewport);
		return uiOverlay;
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay s'étalera sur toute la surface de l'écran.
	 */
	public static UIOverlay createUIOverlay() {
		return createUIOverlay(new ScreenViewport());
	}
	
	/**
	 * Crée un overlay destiné à afficher une tiledmap.
	 * @param pixelsByUnit 
	 */
	public static TiledMapOverlay createTiledMapOverlay() {
		return new TiledMapOverlay();
	}
}
