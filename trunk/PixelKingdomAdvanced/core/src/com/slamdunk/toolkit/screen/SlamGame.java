package com.slamdunk.toolkit.screen;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Un Game maintenant les liens vers un ensemble de SlamScreens
 */
public class SlamGame extends Game {
	private float[] clearColor;
	private Map<String, Screen> screens;
	
	public SlamGame() {
		clearColor = new float[4];
		setClearColor(0, 0, 0, 1);
	}
	
	@Override
	public void create() {
		// Crée la table des écrans
		screens = new HashMap<String, Screen>();
	}
	
	public float[] getClearColor() {
		return clearColor;
	}

	public void setClearColor(float red, float green, float blue, float alpha) {
		clearColor[0] = red;
		clearColor[1] = green;
		clearColor[2] = blue;
		clearColor[3] = alpha;
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
	
	public Screen getScreen(String name) {
		return screens.get(name);
	}
	
	@Override
	public void render() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
}
