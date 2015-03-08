package com.slamdunk.wordarena.screens.arena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
	private GroupEx arenaGroup;
	
	public ArenaOverlay() {
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		arenaGroup = new GroupEx();
		getWorld().addActor(arenaGroup);
	}
	
	public ArenaData getData() {
		return data;
	}
	
	protected void setData(ArenaData data) {
		this.data = data;
	}

	/**
	 * Crée l'arène de jeu, c'est-à-dire le tableau de cellules.
	 */
	public void buildArena(String plan, GameManager gameManager) {
		// Charge le plan
		TypedProperties arenaProperties = new TypedProperties(plan);
		
		// Charge les données de l'arène à partir du plan
		ArenaBuilder builder = new ArenaBuilder(gameManager);
		builder.load(arenaProperties);
		data = builder.build();
		
		// Construit l'arène
		buildArena();
	}
	
	protected GroupEx getArenaGroup() {
		return arenaGroup;
	}
		
	/**
	 * Reconstruit l'arène à partir des données de l'ArenaData
	 */
	protected void buildArena() {
		// Vide l'arène
		arenaGroup.clear();
		
		// Ajoute les cellules
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				// Ajout de la cellule à l'arène
				arenaGroup.addActor(data.cells[x][y]);
			}
		}
		
		// Ajoute les murs
		Actor wallActor;
		for (ArenaCell cell1 : data.walls.getEntries1()) {
			for (ArenaCell cell2 : data.walls.getEntries2(cell1)) {
				wallActor = ArenaWall.buildWall(cell1, cell2);
				if (wallActor != null) {
					arenaGroup.addActor(wallActor);
				}
			}
		}
		
		// Ajoute les zones
		for (ArenaZone zone : data.zones) {
			arenaGroup.addActor(zone);
		}
		
		// Centre l'arène dans la zone d'affichage
		centerArena();
	}
	
	protected void centerArena() {
		arenaGroup.setX(Math.max(0, (int)((WordArenaGame.SCREEN_WIDTH - arenaGroup.getWidth()) / 2)));
		arenaGroup.setY(Math.max(0, (int)((672 - arenaGroup.getHeight()) / 2)));
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
}
