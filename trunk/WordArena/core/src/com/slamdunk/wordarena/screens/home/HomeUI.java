package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;

public class HomeUI extends UIOverlay {
	private HomeScreen screen;
	
	public HomeUI(HomeScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
		
		// Charge la liste des parties en cours
		loadCurrentGames();
	}

	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Home");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Play !
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnPlay", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.launchGame("3_diamond");
			}
		});
		
		// Bouton Options
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnOptions", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Options");
			}
		});
		
		// Bouton Quit
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnQuit", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("DBG Quit");
			}
		});
		
		// Boutons de lancemant des arènes
		sceneLoader.sceneActor.getCompositeById("btnArena0").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena1").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena2").addScript(new GameLaunchScript(screen));
		sceneLoader.sceneActor.getCompositeById("btnArena3").addScript(new GameLaunchScript(screen));
	}
	
	private void loadCurrentGames() {
		// Créer une table
		// ...
		
		// Créer la ligne de boutons pour changer le type de partie
		// ...
		
		// Charger les parties en cours pour le premier type
		// ...
		
		// Ajouter ces parties à la table (1 partie par ligne, 1 ligne d'en-tête par type de partie)
		// ...
		
		// Placer la table dans un ScrollPane pour permettre le scroll
		ScrollPane scrollPane = new ScrollPane(list, Assets.skin);
		scrollPane.setFlickScroll(false);
		
		// Ajouter le ScrollPane au Stage
		getStage().addActor(scrollPane);
	}
}
