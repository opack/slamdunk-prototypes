package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.HomeScreen;

public class WordArenaGame extends SlamGame {
	
	@Override
	public void create() {
		super.create();
		
		// Initialise les r�glages
		SlamSettings.init("WordArena");
		
		// Cr�e les �crans
		HomeScreen home = new HomeScreen();
		addScreen(home);
		
		// Affiche le premier �cran
		setScreen(home);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
