package com.slamdunk.toolkit.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs cam�ras diff�rentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public abstract class SlamStageOverlay {
	private Stage stage;
	
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Indique si l'overlay souhaite recevoir les inputs
	 */
	public abstract boolean isProcessInputs();

	public void createStage(Viewport viewport) {
		stage = new Stage(viewport);
		//DBG TODO V�rifier si on peut supprimer car inutile stage.getCamera().position.set(SlamViewportSettings.SCREEN_W / 2, SlamViewportSettings.SCREEN_H / 2, 0);
	}
	
	public void act(float delta) {
		stage.act(delta);
	}
	
	public void draw() {
		stage.draw();
	}
	
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}
}
