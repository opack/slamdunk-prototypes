package com.slamdunk.wordarena;

import com.slamdunk.toolkit.game.SlamGame;
import com.slamdunk.wordarena.screens.HomeScreen;

public class WordArenaGame extends SlamGame {
	
	@Override
	public void create() {
		super.create();
		
		HomeScreen home = new HomeScreen();
		addScreen(home);
		
		setScreen(home);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
