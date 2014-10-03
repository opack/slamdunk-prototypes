package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contrôles
		UIOverlay ui = OverlayFactory.createUIOverlay();
		// Chargement d'une interface déclarée dans un fichier externe
		ui.loadLayout("layouts/home.json");
		// Ajout de la couche à l'écran
		addOverlay(ui);
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
