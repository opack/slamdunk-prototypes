package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Zones;

/**
 * Représente une zone.
 * Une zone pointe les bordures des cellules afin de changer d'un coup
 * toutes les couleurs.
 */
public class ArenaZone {
	/**
	 * Ensemble des bordures de la zone
	 */
	private List<ZoneEdge> edges;
	
	/**
	 * Joueur qui possède la zone
	 * @param edge
	 */
	private Zones owner;
	
	public ArenaZone() {
		edges = new ArrayList<ZoneEdge>();
	}
	
	public void addEdge(ZoneEdge edge) {
		edges.add(edge);
	}

	public Zones getOwner() {
		return owner;
	}

	public void setOwner(Zones owner) {
		this.owner = owner;

		// Change le propriétaire des cellules
		Set<ArenaCell> cells = new HashSet<ArenaCell>();
		for (ZoneEdge edge : edges) {
			edge.cell.getData().zoneOnBorder.put(edge.border, owner);
			cells.add(edge.cell);
		}
		
		// Met à jour les images des cellules
		for (ArenaCell cell : cells) {
			cell.updateCellImages();
		}
	}
}
