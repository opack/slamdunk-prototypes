package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contr�les
		UIOverlay ui = OverlayFactory.createUIOverlay();
		// Chargement d'une interface d�clar�e dans un fichier externe
		ui.loadLayout("layouts/home.json");
		// Ajout de la couche � l'�cran
		addOverlay(ui);
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
