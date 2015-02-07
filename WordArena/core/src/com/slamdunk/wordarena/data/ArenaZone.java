package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.slamdunk.toolkit.graphics.SpriteBatchUtils;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellOwners;

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
	private CellOwners owner;
	
	/**
	 * Sprites dessinant la zone
	 */
	private List<Sprite> lines;
	
	/**
	 * Indique si la zone a changé, et donc qu'il faut redessiner le contour
	 */
	private boolean invalidate;
	
	public ArenaZone() {
		edges = new ArrayList<ZoneEdge>();
		lines = new ArrayList<Sprite>();
	}
	
	public void addEdge(ZoneEdge edge) {
		edges.add(edge);
		edge.cell.getData().zone = this;
		invalidate = true;
	}

	public CellOwners getOwner() {
		return owner;
	}

	private void setOwner(CellOwners owner) {
		// Même owner ? Rien à faire
		if (this.owner == owner) {
			return;
		}
		// Owner null ou aucun ? On considère que la zone est neutre
		if (owner == null) {
			owner = CellOwners.NEUTRAL;
		}
		// Changement de l'owner
		this.owner = owner;
		// Demande la mise à jour des Sprites dessinant le contour de la zone
		invalidate = true;
	}
	
	private void prepareLines() {
		lines.clear();
		for (ZoneEdge edge : edges) {
			lines.add(SpriteBatchUtils.createSpritedLine(Assets.edges.get(owner), edge.p1, edge.p2));
		}
		invalidate = false;
	}
	
	public void draw(Batch batch) {
		boolean openedHere = false;
		if (!batch.isDrawing()) {
			openedHere = true;
			batch.begin();
		}
		if (invalidate) {
			prepareLines();
		}
		for (Sprite line : lines) {
			line.draw(batch);
		}
		if (openedHere) {
			batch.end();
		}
	}

	/**
	 * Détermine le propriétaire de la zone en fonction du propriétaire
	 * des cellules
	 */
	public void updateOwner() {
		// Liste les cellules uniques : donc qu'une sSet<E>e fois même si une cellule est sur 2 côtés
		Set<ArenaCell> cells = new HashSet<ArenaCell>();
		for (ZoneEdge edge : edges) {
			cells.add(edge.cell);
		}
		
		// Compte le nombre de cellules occupées par chaque joueur
		Map<CellOwners, Integer> occupations = new HashMap<CellOwners, Integer>();
		for (CellOwners owner : CellOwners.values()) {
			occupations.put(owner, 0);
		}
		CellData cellData;
		for (ArenaCell cell : cells) {
			cellData = cell.getData();
			// TODO DBG Faut-il dire qu'il faut également battre "NEUTRE" pour conquérir la zone ? Si oui, retirer ce test
			if (cellData.owner != CellOwners.NEUTRAL) {
				// Ajout du poids de la cellule au poids de ce joueur
				occupations.put(cellData.owner, occupations.get(cellData.owner) + cellData.weight);
			}
		}
		
		// Détermine qui occupe le plus de cellules
		CellOwners owner = CellOwners.NEUTRAL;
		int maxOccupation = 0;
		int power;
		for (Map.Entry<CellOwners, Integer> occupation : occupations.entrySet()) {
			power = occupation.getValue();
			if (power > maxOccupation) {
				// TODO Faut-il dire qu'en cas d'égalité la zone repasse à NEUTRAL ?
				maxOccupation = power;
				owner = occupation.getKey();
			}
		}
		
		// Change le propriétaire de la zone
		setOwner(owner);
	}
}
