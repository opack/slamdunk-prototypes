package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.Map;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellOwners;

public class ZoneBuilder {
	private final float borderThickness;
	
	private Map<Point, ArenaCell> cells;
	
	private Point tmp;
	
	public ZoneBuilder() {
		borderThickness = Assets.edges.get(CellOwners.NEUTRAL).getHeight();
		cells = new HashMap<Point, ArenaCell>();
		tmp = new Point(0, 0);
	}
	
	public ZoneBuilder addCell(ArenaCell cell) {
		cells.put(cell.getData().position, cell);
		return this;
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

	/**
	 * Crée la zone regroupant toutes les cellules ajoutées
	 * @return
	 */
	public ArenaZone build() {
		// Crée la zone
		final ArenaZone zone = new ArenaZone();
		
		// Ajoute les côtés uniques dans la liste
		for (ArenaCell cell : cells.values()) {
			checkEdge(cell, Borders.LEFT, -1, 0, zone);
			checkEdge(cell, Borders.TOP, 0, +1, zone);
			checkEdge(cell, Borders.RIGHT, +1, 0, zone);
			checkEdge(cell, Borders.BOTTOM, 0, -1, zone);
		}
		
		// Affecte la zone à chaque cellule
		for (ArenaCell cell : cells.values()) {
			cell.getData().zone = zone;
		}
		
		// Choisit l'owner de la zone
		zone.updateOwner();
		return zone;
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
	private void checkEdge(ArenaCell cell, Borders border, int offsetX, int offsetY, ArenaZone zone) {
		tmp.setX(cell.getData().position.getX() + offsetX);
		tmp.setY(cell.getData().position.getY() + offsetY);
		// S'il n'y a pas de voisin de ce côté, alors c'est qu'on est à la limite de la zone
		if (!cells.containsKey(tmp)) {
			zone.addEdge(createEdge(cell, border));
		}
	}

	public ZoneBuilder reset() {
		cells.clear();
		return this;
	}
}
