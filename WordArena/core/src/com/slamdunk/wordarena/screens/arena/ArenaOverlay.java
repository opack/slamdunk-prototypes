package com.slamdunk.wordarena.screens.arena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaWall;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.data.ArenaBuilder;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

public class ArenaOverlay extends WorldOverlay {
	private ArenaData data;
	
	public ArenaOverlay() {
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
	}
	
	public ArenaData getData() {
		return data;
	}

	/**
	 * Crée l'arène de jeu, c'est-à-dire le tableau de cellules.
	 * Cette méthode crée une simple arène rectangulaire, sans
	 * respecter de plan particulier. 
	 * @param width Largeur de l'arène, en nombre de cellules
	 * @param height Hauteur de l'arène, en nombre de cellules
	 */
	public void buildArena(String plan, GameManager gameManager) {
		// Vide l'arène actuelle
		getWorld().clear();
		
		// Charge le plan
		TypedProperties arenaProperties = new TypedProperties(plan);
		
		// Construit l'arène à partir du plan
		ArenaBuilder builder = new ArenaBuilder(gameManager);
		builder.load(arenaProperties);
		data = builder.build();		
		
		// Ajoute les cellules
		GroupEx arenaGroup = new GroupEx();
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				// Ajout de la cellule à l'arène
				arenaGroup.addActor(data.cells[x][y]);
			}
		}
		
		// Ajoute les murs
		for (ArenaCell cell1 : data.walls.getEntries1()) {
			for (ArenaWall wall : data.walls.getValues(cell1)) {
				arenaGroup.addActor(wall);
			}
		}
		
		// Ajoute les zones
		for (ArenaZone zone : data.zones) {
			arenaGroup.addActor(zone);
		}
		
		// Centre l'arène dans la zone d'affichage
		arenaGroup.setX(Math.max(0, (int)((WordArenaGame.SCREEN_WIDTH - arenaGroup.getWidth()) / 2)));
		arenaGroup.setY(Math.max(0, (int)((672 - arenaGroup.getHeight()) / 2)));
		getWorld().addActor(arenaGroup);
	}

	/**
	 * Change le propriétaire des cellules indiquées et celui des zones
	 * les contenant le cas échéant
	 * @param cells
	 * @param owner
	 */
	public void setOwner(List<ArenaCell> cells, Player owner) {
		// Change le propriétaire des cellules et note les zones impactées
		Set<ArenaZone> impactedZones = new HashSet<ArenaZone>();
		ArenaZone zone;
		for (ArenaCell cell : cells) {
			cell.setOwner(owner);
			cell.getData().state = CellStates.OWNED;
			
			zone = cell.getData().zone;
			if (zone != null) {
				impactedZones.add(zone);
			}
		}
		
		// Change le propriétaire des zones
		for (ArenaZone impactedZone : impactedZones) {
			impactedZone.updateOwner();
		}
	}

	/**
	 * Affiche ou masque l'arène
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		getWorld().setVisible(visible);
	}
	
	/**
	 * Affiche ou masque le lettres
	 * @param visible
	 */
	public void showLetters(boolean show) {
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				data.cells[x][y].showLetter(show);
			}
		}
	}
	
	/**
	 * Si le joueur n'a pas encore joué, il peut demander de nouvelles
	 * lettres dans sa zone de départ
	 */
	public void refreshStartingZone(Player owner) {
		// Recherche la zone de cet owner
		for (ArenaZone zone : data.zones) {
			if (owner.equals(zone.getData().owner)) {
				// Tire de nouvelles lettres pour les cellules de cette zone
				CellData cellData;
				for (ArenaCell cell : zone.getCells()) {
					cellData = cell.getData();
					cellData.letter = ArenaBuilder.chooseLetter(cellData.type, cellData.planLetter, data.letterDeck);
					cell.updateDisplay();
				}
				// Les lettres de la zone ont été changées. On suppose qu'il n'y a
				// qu'une seule zone de départ. Inutile donc de continuer à traiter
				// les autres zones.
				break;
			}
		}
	}

	/**
	 * Indique s'il y a un mur entre les 2 cellules spécifiées
	 * @param cell1
	 * @param cell2
	 * @return
	 */
	public boolean hasWall(ArenaCell cell1, ArenaCell cell2) {
		ArenaWall wall = data.walls.get(cell1, cell2);
		return wall != null
			&& wall.getData().active;
	}
}
