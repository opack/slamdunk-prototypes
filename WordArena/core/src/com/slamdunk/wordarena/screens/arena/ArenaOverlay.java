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
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.ArenaBuilder;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.ArenaZone;
import com.slamdunk.wordarena.data.GameManager;
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
		ArenaBuilder builder = new ArenaBuilder(gameManager);
		
		TypedProperties arenaProperties = new TypedProperties(plan);
		builder.load(arenaProperties);
		
		// Crée l'arène
		data = builder.build();
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
}
