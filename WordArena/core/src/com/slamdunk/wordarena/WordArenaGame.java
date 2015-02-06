package com.slamdunk.wordarena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;
	
	@Override
	public void create() {
		super.create();
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Charge les ressources
		Assets.load();
		
		// Crée les écrans
//		HomeScreen home = new HomeScreen(this);
//		addScreen(home);
//		addScreen(new WorldScreen(this));
//		addScreen(new GameScreen(this));
		addScreen(new ArenaScreen(this));
		
		// Affiche le premier écran
//		setScreen(home);
		setScreen(ArenaScreen.NAME);
	}
	
	@Override
	public void render() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
}
