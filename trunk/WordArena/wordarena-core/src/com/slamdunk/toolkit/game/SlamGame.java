package com.slamdunk.toolkit.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Un Game maintenant les liens vers un ensemble de SlamScreens
 */
public class SlamGame extends Game {

	private Map<String, Screen> screens;
	
	@Override
	public void create() {
		// Crée la table des écrans
		screens = new HashMap<String, Screen>();
	}
	
	public void addScreen(SlamScreen screen) {
		screens.put(screen.getName(), screen);
	}

	public void setScreen(String name) {
		Screen screen = screens.get(name);
		if (screen == null) {
			throw new IllegalArgumentException("There is no screen with name " + name);
		}
		setScreen(screen);
	}
}
