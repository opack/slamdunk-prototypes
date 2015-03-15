package com.slamdunk.wordarena.actors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.slamdunk.toolkit.lang.MaxValueFinder;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.EdgeData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.data.ZoneData;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Représente une zone.
 * Une zone pointe les bordures des cellules afin de changer d'un coup
 * toutes les couleurs.
 */
public class ArenaZone extends Group {
	public static final ArenaZone NONE = new ArenaZone(null, "-");
	
	private ZoneData data;
	
	private GameManager gameManager;
	
	/**
	 * Cellules uniques de la zone, rangées par position
	 */
	private Map<Point, ArenaCell> cells;
	
	private List<ZoneEdge> edges;
	
	private static Point tmp = new Point(0, 0);
	
	public ArenaZone(GameManager gameManager, String id) {
		this.gameManager = gameManager;

		data = new ZoneData(id);
		cells = new HashMap<Point, ArenaCell>();
		edges = new ArrayList<ZoneEdge>();
		
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
		// Si la cellule était simplement sous contrôle, elle est désormais neutre
		CellData data = cell.getData();
		if (data.state == CellStates.CONTROLED) {
			data.state = CellStates.OWNED;
			data.owner = Player.NEUTRAL;
		}
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
		edges.clear();
		for (ArenaCell cell : cells.values()) {
			// Affecte la zone à chaque cellule
			cell.getData().zone = this;
		
			// Ajoute les côtés uniques dans la liste
			checkEdge(cell, Borders.LEFT, -1, 0);
			checkEdge(cell, Borders.TOP, 0, +1);
			checkEdge(cell, Borders.RIGHT, +1, 0);
			checkEdge(cell, Borders.BOTTOM, 0, -1);
		}
		
		// Crée les lignes pour dessiner ces côtés
		clear();
		for (ZoneEdge edge : edges) {
			edge.update(data.highlighted);
			edge.setScaling(Scaling.stretch);
			edge.setAlign(Align.center);
			edge.setSize(edge.getPrefWidth(), edge.getPrefHeight());
			
			ActorHelper.alignInsideCell(edge.getData().border, edge.getData().p1, edge);
			addActor(edge);
		}
		
		// Choisit l'owner de la zone
		updateOwner();
	}
	
	/**
	 * Met la zone en surbrillance ou non
	 * @param highlighted
	 */
	public void highlight(boolean highlighted) {
		data.highlighted = highlighted;
		for (ZoneEdge edge : edges) {
			edge.update(highlighted);
		}
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
	private void checkEdge(ArenaCell cell, Borders border, int offsetX, int offsetY) {
		tmp.setX(cell.getData().position.getX() + offsetX);
		tmp.setY(cell.getData().position.getY() + offsetY);
		// S'il n'y a pas de voisin de ce côté, alors c'est qu'on est à la limite de la zone
		if (!cells.containsKey(tmp)) {
			edges.add(createEdge(cell, border));
		}
	}

	private ZoneEdge createEdge(ArenaCell cell, Borders border) {
		ZoneEdge edge = new ZoneEdge();
		EdgeData data = edge.getData();
		data.border = border;
		data.cell = cell;
		
		final float cellX = cell.getX();
		final float cellY = cell.getY();
		final float cellWidth = cell.getWidth();
		final float cellHeight = cell.getHeight();
		
		switch (border) {
		case BOTTOM:
			data.p1.set(cellX, cellY);
			data.p2.set(cellX + cellWidth, cellY);
			break;
		case LEFT:
			data.p1.set(cellX, cellY);
			data.p2.set(cellX, cellY + cellHeight);
			break;
		case RIGHT:
			data.p1.set(cellX + cellWidth, cellY);
			data.p2.set(cellX + cellWidth, cellY + cellHeight);
			break;
		case TOP:
			data.p1.set(cellX, cellY + cellHeight);
			data.p2.set(cellX + cellWidth, cellY + cellHeight);
			break;
		}
		return edge;
	}

	public ZoneData getData() {
		return data;
	}

	private void setOwner(Player newOwner) {
		// Changement d'owner ? Avertit le game manager pour la mise à jour du score
		if (!data.owner.equals(newOwner)
		&& gameManager != null) {
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
