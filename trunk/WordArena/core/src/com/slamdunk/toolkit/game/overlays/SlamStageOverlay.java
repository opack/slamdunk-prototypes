package com.slamdunk.toolkit.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs caméras différentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public class SlamStageOverlay {
	private Stage stage;
	
	/**
	 * Indique si l'overlay souhaite recevoir les inputs
	 */
	private boolean processInputs;
	
	public Stage getStage() {
		return stage;
	}
	
	public boolean isProcessInputs() {
		return processInputs;
	}

	public void setProcessInputs(boolean processInputs) {
		this.processInputs = processInputs;
	}

	public void createStage(Viewport viewport) {
		stage = new Stage(viewport);
		//DBG TODO Vérifier si on peut supprimer car inutile stage.getCamera().position.set(SlamViewportSettings.SCREEN_W / 2, SlamViewportSettings.SCREEN_H / 2, 0);
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
