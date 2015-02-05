package com.slamdunk.wordarena.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;
	
	// Utilisé par tous les écrans
	public SpriteBatch batcher;
	
	@Override
	public void create() {
		super.create();
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Crée les écrans
//		HomeScreen home = new HomeScreen(this);
//		addScreen(home);
//		addScreen(new WorldScreen(this));
//		addScreen(new GameScreen(this));
		
		// Affiche le premier écran
//		setScreen(home);
		
		
//		setScreen(new GamePartsTestScreen());
//		setScreen(new WordArenaScreen());
		
		Assets.load();
		batcher = new SpriteBatch();
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void render() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
}
