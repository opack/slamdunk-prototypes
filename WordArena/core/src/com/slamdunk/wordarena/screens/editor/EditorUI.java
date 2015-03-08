package com.slamdunk.wordarena.screens.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;

public class EditorUI extends UIOverlay {
	private EditorScreen screen;
	
	public EditorUI(EditorScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
	}
	
	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Editor");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Change Size
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnChangeSize", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Type
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnType", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Letter
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnLetter", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Power
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPower", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Owner
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnOwner", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Zone
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		// Bouton Create wall
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnCreateWall", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
	}
}
