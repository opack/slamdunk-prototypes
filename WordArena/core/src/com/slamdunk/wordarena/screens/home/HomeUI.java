package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

public class HomeUI extends UIOverlay {
	private HomeScreen screen;
	
	public HomeUI(HomeScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
	}

	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.resourceManager);
		sceneLoader.loadScene("Home");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Play !
		SimpleButtonScript playScript = new SimpleButtonScript();
		playScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.launchGame();
			}
		});
		sceneLoader.sceneActor.getCompositeById("play").addScript(playScript);
		
		// Bouton Options
		SimpleButtonScript optionsScript = new SimpleButtonScript();
		optionsScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Options");
			}
		});
		sceneLoader.sceneActor.getCompositeById("options").addScript(optionsScript);
		
		// Bouton Quit
		SimpleButtonScript quitScript = new SimpleButtonScript();
		quitScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Quit");
			}
		});
		sceneLoader.sceneActor.getCompositeById("quit").addScript(quitScript);
	}
}
