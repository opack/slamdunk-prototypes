package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.pixelkingdomadvanced.screens.MoveCameraDragListener;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.ZoneBuilder;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.enums.Zones;

public class ArenaOverlay extends WorldOverlay {
	private static final int CELL_SIZE = 56;
	
	private Deck<Letters> lettersDeck;
	private ArenaCell[][] cells;
	private List<ArenaZone> zones;
	
	private MoveCameraDragListener moveCameraListener;
	
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
		final WordSelectionHandler wordSelectionHandler = ((ArenaScreen)getScreen()).getWordSelectionHandler();
		
		// Génère des lettres en tenant compte de leur représentation
		lettersDeck = new Deck<Letters>(Letters.values(), 1);
		
		// Crée l'arène
		cells = new ArenaCell[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Création de la cellule
				cells[x][y] = createCell(wordSelectionHandler, CellStates.NORMAL, x, y, lettersDeck.draw());
				
				// Ajout de la cellule à l'arène
				getWorld().addActor(cells[x][y]);
			}
		}
		
		// Crée les zones
		ZoneBuilder zoneBuilder = new ZoneBuilder();
		zoneBuilder.setOwner(Zones.NEUTRAL);
		zoneBuilder.addCell(cells[0][0]);
		zoneBuilder.addCell(cells[0][1]);
		zoneBuilder.addCell(cells[1][1]);
		zoneBuilder.addCell(cells[2][1]);
		ArenaZone zone = zoneBuilder.build();
		zones.add(zone);
		
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
	private ArenaCell createCell(WordSelectionHandler wordSelectionHandler, CellStates state, int x, int y, Letters letter) {
		ArenaCell cell = new ArenaCell(wordSelectionHandler);
		
		// Définition des données du modèle
		cell.getData().letter = letter;
		cell.getData().position.setXY(x, y);
		cell.getData().state = state;
		cell.getData().zoneOnBorder.put(Borders.LEFT, Zones.NONE);
		cell.getData().zoneOnBorder.put(Borders.TOP, Zones.NONE);
		cell.getData().zoneOnBorder.put(Borders.RIGHT, Zones.NONE);
		cell.getData().zoneOnBorder.put(Borders.BOTTOM, Zones.NONE);
		cell.updateCellImages();
		
		// Placement de la cellule dans le monde
		cell.setPosition(x * CELL_SIZE, y * CELL_SIZE);
		
		return cell;
	}
}
