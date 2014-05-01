package com.slamdunk.cobalt;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameUI {
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	
	private Skin skin;
	private Table cases;
	private Map<Action, Image> actions;

	/**
	 * Crée les composants de l'IHM pour l'écran de jeu
	 * @param stage
	 */
	public void create(Stage stage) {
		// Charge la skin
		skin = new Skin(Gdx.files.internal("cobalt.json"));
		
		// Crée la grille de cases
		cases = createGrid();
		stage.addActor(cases);
		
		// Charge les styles pour les différentes actions
		actions = loadActionImages();
	}

	/**
	 * Crée la grille de cases du jeu
	 * @return
	 */
	private Table createGrid() {
		Table grid = new Table();
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int y = 0; y < GRID_HEIGHT; y++) {
				Button button = new Button();
			}	
		}
		return grid;
	}

	/**
	 * Charge les images correspondant à chaque action
	 * @return
	 */
	private Map<Action, Image> loadActionImages() {
		// TODO Auto-generated method stub
		return null;
	}
}
