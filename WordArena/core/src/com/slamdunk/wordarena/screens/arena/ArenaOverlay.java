package com.slamdunk.wordarena.screens.arena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.screen.overlays.WorldOverlay;
import com.slamdunk.toolkit.ui.MoveCameraDragListener;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaBuilder;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.enums.Owners;

public class ArenaOverlay extends WorldOverlay {
	private ArenaData data;
	private MoveCameraDragListener moveCameraListener;
	
	public ArenaOverlay() {
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		moveCameraListener = new MoveCameraDragListener(getStage().getCamera());
		getStage().addListener(moveCameraListener);
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
		// Charge le plan
		TypedProperties arenaProperties = new TypedProperties(plan);
		
		// Construit l'arène à partir du plan
		ArenaBuilder builder = new ArenaBuilder(moveCameraListener, gameManager);
		builder.load(arenaProperties);
		data = builder.build();		
		
		// Ajoute l'arène dans le monde
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				// Ajout de la cellule à l'arène
				getWorld().addActor(data.cells[x][y]);
			}
		}
		
		// Place la caméra au centre de l'arène
		centerCamera();
	}
	
	/**
	 * Place la caméra au centre de l'arène 
	 */
	public void centerCamera() {
		Actor arena = getWorld();
		Camera camera = getStage().getCamera();
		camera.position.x = arena.getX() + arena.getWidth() / 2;
		camera.position.y = arena.getY() + arena.getHeight() / 2;
	}

	@Override
	public void draw() {
		super.draw();
		Batch batch = getStage().getBatch();
		for (ArenaZone zone : data.zones) {
			zone.draw(batch);
		}
	}

	/**
	 * Change le propriétaire des cellules indiquées et celui des zones
	 * les contenant le cas échéant
	 * @param cells
	 * @param owner
	 */
	public void setOwner(List<ArenaCell> cells, Owners owner) {
		// Change le propriétaire des cellules et note les zones impactées
		Set<ArenaZone> impactedZones = new HashSet<ArenaZone>();
		ArenaZone zone;
		for (ArenaCell cell : cells) {
			cell.setOwner(owner);
			
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
	public void refreshStartingZone(Owners owner) {
		// Recherche la zone de cet owner
		for (ArenaZone zone : data.zones) {
			if (zone.getOwner() == owner) {
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
