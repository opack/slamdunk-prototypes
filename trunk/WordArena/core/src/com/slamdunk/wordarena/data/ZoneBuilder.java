package com.slamdunk.wordarena.data;

import java.util.HashMap;
import java.util.Map;

import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellOwners;

public class ZoneBuilder {
	private Map<Point, ArenaCell> cells;
	private CellOwners owner;
	private Point tmp;
	
	public ZoneBuilder() {
		owner = CellOwners.NEUTRAL;
		cells = new HashMap<Point, ArenaCell>();
		tmp = new Point(0, 0);
	}
	
	public CellOwners getOwner() {
		return owner;
	}

	public ZoneBuilder setOwner(CellOwners owner) {
		this.owner = owner;
		return this;
	}

	public ZoneBuilder addCell(ArenaCell cell) {
		cells.put(cell.getData().position, cell);
		return this;
	}

	private ZoneEdge createEdge(ArenaCell cell, Borders border) {
		ZoneEdge edge = new ZoneEdge();
		edge.border = border;
		edge.cell = cell;
		return edge;
	}

	/**
	 * Crée la zone regroupant toutes les cellules ajoutées
	 * @return
	 */
	public ArenaZone build() {
		// Crée la zone
		final ArenaZone zone = new ArenaZone();
		
		// Ajoute les côtés uniques dans la zone
		for (ArenaCell cell : cells.values()) {
			checkEdge(cell, Borders.LEFT, -1, 0, zone);
			checkEdge(cell, Borders.TOP, 0, +1, zone);
			checkEdge(cell, Borders.RIGHT, +1, 0, zone);
			checkEdge(cell, Borders.BOTTOM, 0, -1, zone);
		}
		
		// Change l'owner de la zone pour que tous les côtés ajoutés
		// prennent la bonne couleur
		zone.setOwner(owner);
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
