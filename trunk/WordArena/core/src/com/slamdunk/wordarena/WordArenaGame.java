package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.gameparts.scenes.GamePartsTestScreen;
import com.slamdunk.wordarena.gameparts.scenes.WordArenaScreen;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;
import com.slamdunk.wordarena.screens.home.HomeScreen;
import com.slamdunk.wordarena.screens.worldmap.WorldScreen;

public class WordArenaGame extends SlamGame {
	GamePartsTestScreen dbg;
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
		//setScreen(home);
//		setScreen(new GamePartsTestScreen());
		setScreen(new WordArenaScreen());
	}
}
