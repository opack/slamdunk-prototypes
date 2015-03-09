package com.slamdunk.wordarena.actors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.slamdunk.toolkit.graphics.SpriteBatchUtils;
import com.slamdunk.toolkit.lang.MaxValueFinder;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.data.ZoneData;
import com.slamdunk.wordarena.data.ZoneEdge;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Représente une zone.
 * Une zone pointe les bordures des cellules afin de changer d'un coup
 * toutes les couleurs.
 */
public class ArenaZone extends Group {
	public static final ArenaZone NONE = new ArenaZone(null, "-");
	
	private final float borderThickness;
	
	private ZoneData data;
	
	private GameManager gameManager;
	
	/**
	 * Cellules uniques de la zone, rangées par position
	 */
	private Map<Point, ArenaCell> cells;
	
	private static Point tmp = new Point(0, 0);
	
	public ArenaZone(GameManager gameManager, String id) {
		this.gameManager = gameManager;
		borderThickness = Assets.edge.getHeight();

		data = new ZoneData(id);
		cells = new HashMap<Point, ArenaCell>();
		
		tmp = new Point(0, 0);
	}
	
	public Collection<ArenaCell> getCells() {
		return cells.values();
	}

	public void addCell(ArenaCell cell) {
		cells.put(cell.getData().position, cell);
	}
	
	public void removeCell(ArenaCell cell) {
		cells.remove(cell.getData().position);
	}
	
	public void update() {
		// Les cellules ajoutées à la zone NONE n'apparaissent pas dans une bordure
		if (this == NONE) {
			for (ArenaCell cell : cells.values()) {
				// Affecte la zone à chaque cellule
				cell.getData().zone = this;
			}
			return;
		}
		
		// Met à jour la liste des côtés
		final List<ZoneEdge> edges = new ArrayList<ZoneEdge>();
		for (ArenaCell cell : cells.values()) {
			// Affecte la zone à chaque cellule
			cell.getData().zone = this;
		
			// Ajoute les côtés uniques dans la liste
			checkEdge(cell, Borders.LEFT, -1, 0, edges);
			checkEdge(cell, Borders.TOP, 0, +1, edges);
			checkEdge(cell, Borders.RIGHT, +1, 0, edges);
			checkEdge(cell, Borders.BOTTOM, 0, -1, edges);
		}
		
		// Crée les lignes pour dessiner ces côtés
		clear();
		for (ZoneEdge edge : edges) {
			addActor(new SpritedActor(SpriteBatchUtils.createSpritedLine(Assets.edge, edge.p1, edge.p2)));
		}
		
		// Choisit l'owner de la zone
		updateOwner();
	}
	
	/**
	 * Vérifie si le voisin à l'offset indiqué fait partie de cette zone.
	 * Si non, alors c'est que ce côté marque la fin de la zone, et il est
	 * donc ajouté à la liste des côté de la zone
	 * @param cell
	 * @param border
	 * @param offsetX
	 * @param offsetY
	 * @param edges
	 */
	private void checkEdge(ArenaCell cell, Borders border, int offsetX, int offsetY, List<ZoneEdge> edges) {
		tmp.setX(cell.getData().position.getX() + offsetX);
		tmp.setY(cell.getData().position.getY() + offsetY);
		// S'il n'y a pas de voisin de ce côté, alors c'est qu'on est à la limite de la zone
		if (!cells.containsKey(tmp)) {
			ZoneEdge edge = createEdge(cell, border);
			edges.add(edge);
		}
	}

	private ZoneEdge createEdge(ArenaCell cell, Borders border) {
		ZoneEdge edge = new ZoneEdge();
		edge.border = border;
		edge.cell = cell;
		
		final float cellX = cell.getX();
		final float cellY = cell.getY();
		final float cellWidth = cell.getWidth();
		final float cellHeight = cell.getHeight();
		
		switch (border) {
		case BOTTOM:
			edge.p1.set(cellX + borderThickness, cellY + borderThickness);
			edge.p2.set(cellX + cellWidth - borderThickness, cellY + borderThickness);
			break;
		case LEFT:
			edge.p1.set(cellX + borderThickness, cellY + borderThickness);
			edge.p2.set(cellX + borderThickness, cellY + cellHeight - borderThickness);
			break;
		case RIGHT:
			edge.p1.set(cellX + cellWidth - borderThickness, cellY + borderThickness);
			edge.p2.set(cellX + cellWidth - borderThickness, cellY + cellHeight - borderThickness);
			break;
		case TOP:
			edge.p1.set(cellX + borderThickness, cellY + cellHeight - borderThickness);
			edge.p2.set(cellX + cellWidth - borderThickness, cellY + cellHeight - borderThickness);
			break;
		}
		return edge;
	}

	public ZoneData getData() {
		return data;
	}

	private void setOwner(Player newOwner) {
		// Même owner ? Rien à faire
		if (data.owner.equals(newOwner)) {
			return;
		}
		
		// Avertit le game manager pour la mise à jour du score
		if (gameManager != null) {
			gameManager.zoneChangedOwner(data.owner, newOwner);
		}
		
		// Changement de l'owner
		data.owner = newOwner;
		
		// Change l'image des cellules de la zone
		CellData data;
		for (ArenaCell cell : cells.values()) {
			data = cell.getData();
			
			// Une cellule passe sous le contrôle du joueur si elle est dans la zone et :
			//    - soit neutre
			//    - soit sous le simple contrôle d'un adversaire
			if (Player.NEUTRAL.equals(data.owner)
			|| data.state != CellStates.OWNED) {
				data.owner = newOwner;
				data.state = CellStates.CONTROLED;
			}
			
			// Met à jour l'image
			cell.updateDisplay();
		}
	}
	
	/**
	 * Détermine le propriétaire de la zone en fonction du propriétaire
	 * des cellules
	 */
	public void updateOwner() {
		// Zone none ? Personne ne peut la posséder
		if (this == NONE) {
			return;
		}
				
		MaxValueFinder<Player> occupations = new MaxValueFinder<Player>();
		occupations.setIgnoredValue(Player.NEUTRAL);
		occupations.setValueIfDraw(Player.NEUTRAL);
		
		// Compte le nombre de cellules occupées par chaque joueur
		CellData cellData;
		for (ArenaCell cell : cells.values()) {
			cellData = cell.getData();
			// Ajout de la puissance de la cellule à celle de ce joueur
			if (cellData.state == CellStates.OWNED) {
				occupations.addValue(cellData.owner, cellData.power);
			}
		}
		
		// Détermine qui occupe le plus de cellules
		Player newOwner = occupations.getMaxValue();
		
		// Change le propriétaire de la zone
		setOwner(newOwner);
	}
	
	@Override
	public String toString() {
		return data.id;
	}
}
