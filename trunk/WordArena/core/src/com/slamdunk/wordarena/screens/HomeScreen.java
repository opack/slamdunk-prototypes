package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.game.SlamScreen;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contr�les
		setupUIOverlay();
		// Chargement d'une interface d�clar�e dans un fichier externe
		getUIOverlay().loadLayout("layouts/layout.json");
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
