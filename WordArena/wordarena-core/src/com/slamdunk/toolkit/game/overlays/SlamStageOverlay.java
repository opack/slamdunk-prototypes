package com.slamdunk.toolkit.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.slamdunk.toolkit.settings.SlamViewportSettings;

/**
 * Une couche d'affichage qui contient un Stage dans lequel on peut mettre tout et n'importe quoi.
 * Cela permet d'avoir plusieurs stages et donc plusieurs caméras différentes, utiles pour superposer
 * par exemple le monde, une minimap, une couche d'UI...
 */
public class SlamStageOverlay {
	private Stage stage;
	
	public Stage getStage() {
		return stage;
	}
	
	public void createStage(float width, float height) {
		if (!SlamViewportSettings.viewportSet) {
			System.err.println("Les réglages n'ont pas été définis !");
		}
		stage = new Stage(SlamViewportSettings.SCREEN_W, SlamViewportSettings.SCREEN_H, false);
		stage.getCamera().position.set(SlamViewportSettings.SCREEN_W / 2, SlamViewportSettings.SCREEN_H / 2, 0);
	}
	
	public void act(float delta) {
		stage.act(delta);
	}
	
	public void draw() {
		stage.draw();
	}
}
