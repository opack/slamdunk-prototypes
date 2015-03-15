package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.screen.SlamScreen;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs caméras différentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public abstract class StageOverlay implements SlamOverlay {
	private Stage stage;
	private SlamScreen screen;
	
	public Stage getStage() {
		return stage;
	}
	
	public SlamScreen getScreen() {
		return screen;
	}

	public void setScreen(SlamScreen screen) {
		this.screen = screen;
	}

	public void createStage(Viewport viewport) {
		stage = new Stage(viewport);
	}
	
	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}
	
	@Override
	public void act(float delta) {
		stage.act(delta);
	}
	
	@Override
	public void draw() {
		stage.draw();
	}

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}
}
