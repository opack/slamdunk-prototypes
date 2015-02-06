package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;
	
	@Override
	public void create() {
		super.create();
		setClearColor(1, 1, 1, 1);
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Charge les ressources
		Assets.load();
		
		// Crée les écrans
		addScreen(new ArenaScreen(this));
		
		// Affiche le premier écran
		setScreen(ArenaScreen.NAME);
	}
}
