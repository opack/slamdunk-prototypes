package com.slamdunk.wordarena.screens;

import com.slamdunk.toolkit.game.SlamScreen;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contr�les
		setupUIOverlay();
		// Ajout de boutons
		getUIOverlay().addTextButton("DBG");
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
