package com.slamdunk.wordarena;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slamdunk.wordarena.WordArenaGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "wordarena";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 800;
		
		new LwjglApplication(new WordArenaGame(), cfg);
	}
}
