package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.HomeScreen;
import com.slamdunk.wordarena.screens.PathTest;
import com.slamdunk.wordarena.screens.game.GameScreen2;
import com.slamdunk.wordarena.screens.worldmap.WorldScreen;

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
		addScreen(new GameScreen2(this));
		
		// Affiche le premier écran
		setScreen(new PathTest());
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
