package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs caméras différentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public abstract class StageOverlay implements SlamOverlay {
	private Stage stage;
	
	public Stage getStage() {
		return stage;
	}
	
	public void createStage(Viewport viewport) {
		stage = new Stage(viewport);
		//DBG TODO Vérifier si on peut supprimer car inutile stage.getCamera().position.set(SlamViewportSettings.SCREEN_W / 2, SlamViewportSettings.SCREEN_H / 2, 0);
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
