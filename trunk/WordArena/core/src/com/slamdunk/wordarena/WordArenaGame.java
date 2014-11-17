package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.screens.HomeScreen;
import com.slamdunk.wordarena.screens.WorldScreen;

public class WordArenaGame extends SlamGame {
	
	@Override
	public void create() {
		super.create();
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Crée les écrans
		HomeScreen home = new HomeScreen(this);
		addScreen(home);
		addScreen(new WorldScreen(this));
		addScreen(new GameScreen(this));
		
		// Affiche le premier écran
		setScreen(home);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
