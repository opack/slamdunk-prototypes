package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.game.SlamScreen;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contr�les
		setupUIOverlay();
		// Ajout de boutons
		getUIOverlay().addTextButton("DBG");
		// Chargement d'une interface d�finie � l'ext�rieur
		getUIOverlay().loadLayout("layouts/layout.json");
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
