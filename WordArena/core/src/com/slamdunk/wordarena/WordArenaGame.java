package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;
	
	@Override
	public void create() {
		super.create();
		
		setClearColor(1, 1, 1, 1);
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Charge les ressources
		Assets.load();
		
		// Crée les écrans
		addScreen(new HomeScreen(this));
		addScreen(new ArenaScreen(this));
		
		// Affiche le premier écran
		setScreen(HomeScreen.NAME);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}
}
