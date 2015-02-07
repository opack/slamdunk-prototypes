package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.pixelkingdomadvanced.screens.MoveCameraDragListener;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.ZoneBuilder;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;

public class ArenaOverlay extends WorldOverlay {
	private Deck<Letters> lettersDeck;
	private ArenaCell[][] cells;
	private List<ArenaZone> zones;
	
	private MoveCameraDragListener moveCameraListener;
	
	private Skin skin;
	private WordSelectionHandler wordSelectionHandler;
	
	public ArenaOverlay() {
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		zones = new ArrayList<ArenaZone>();
		
		moveCameraListener = new MoveCameraDragListener(getStage().getCamera());
		getStage().addListener(moveCameraListener);
	}
	
	/**
	 * Crée l'arène de jeu, c'est-à-dire le tableau de cellules.
	 * Cette méthode crée une simple arène rectangulaire, sans
	 * respecter de plan particulier. 
	 * @param width Largeur de l'arène, en nombre de cellules
	 * @param height Hauteur de l'arène, en nombre de cellules
	 */
	public void buildArena(final int width, final int height) {
		// Prépare les données nécessaires à la création des cellules
		skin = new Skin(Gdx.files.internal(UIOverlay.DEFAULT_SKIN));
		wordSelectionHandler = ((ArenaScreen)getScreen()).getWordSelectionHandler();
		
		// Génère des lettres en tenant compte de leur représentation
		lettersDeck = new Deck<Letters>(Letters.values(), 1);
		
		// Crée l'arène
		cells = new ArenaCell[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Création de la cellule
				cells[x][y] = createCell(
					CellStates.NORMAL,
					x, y,
					lettersDeck.draw(),
					CellOwners.NONE,
					1);
				
				// Ajout de la cellule à l'arène
				getWorld().addActor(cells[x][y]);
			}
		}
		
		// Crée les zones
		ZoneBuilder zoneBuilder = new ZoneBuilder();
		cells[0][0].getData().owner = CellOwners.PLAYER1;
		zoneBuilder.addCell(cells[0][0]).addCell(cells[0][1]).addCell(cells[1][1]).addCell(cells[2][1]);
		zones.add(zoneBuilder.build());
		
		zoneBuilder.reset();
		cells[5][5].getData().owner = CellOwners.PLAYER2;
		cells[5][6].getData().owner = CellOwners.PLAYER2;
		cells[5][7].getData().owner = CellOwners.PLAYER3;
		zoneBuilder.addCell(cells[5][5]).addCell(cells[5][6]).addCell(cells[5][7]).addCell(cells[6][5]).addCell(cells[6][6]);
		zones.add(zoneBuilder.build());
		
		// Ajoute le listener permettant de déplacer l'arène
		moveCameraListener.computeMoveBounds(getStage().getCamera(), getWorld(), 20);
	}
	
	/**
	 * Crée une cellule de l'arène
	 * @param type
	 * @param x
	 * @param y
	 * @param letter
	 * @return
	 */
	private ArenaCell createCell(
			CellStates state,
			int x, int y,
			Letters letter,
			CellOwners owner,
			int weight) {
		ArenaCell cell = new ArenaCell(skin, wordSelectionHandler);
		
		// Définition des données du modèle
		cell.getData().position.setXY(x, y);
		cell.getData().owner = owner;
		cell.getData().weight = weight;
		cell.getData().letter = letter;
		cell.getData().state = state;
		cell.updateDisplay();
		
		// Placement de la cellule dans le monde
		cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
		
		return cell;
	}
	
	@Override
	public void draw() {
		super.draw();
		Batch batch = getStage().getBatch();
		for (ArenaZone zone : zones) {
			zone.draw(batch);
		}
	}
}
