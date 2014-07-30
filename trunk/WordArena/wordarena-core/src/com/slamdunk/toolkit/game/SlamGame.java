package com.slamdunk.toolkit.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Un Game maintenant les liens vers un ensemble de SlamScreens
 * @author Yed
 *
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

}
