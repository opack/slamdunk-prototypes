package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.screen.SlamScreen;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contrôles
		setupUIOverlay();
		// Chargement d'une interface déclarée dans un fichier externe
		getUIOverlay().loadLayout("layouts/layout.json");
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
