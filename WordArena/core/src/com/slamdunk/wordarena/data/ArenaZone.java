package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.slamdunk.toolkit.graphics.SpriteBatchUtils;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.utils.MaxValueFinder;

/**
 * Représente une zone.
 * Une zone pointe les bordures des cellules afin de changer d'un coup
 * toutes les couleurs.
 */
public class ArenaZone {
	private final float borderThickness;
	
	/**
	 * Identifiant de la zone, correspondant à ce qui
	 * est indiqué dans le fichier descriptif properties
	 */
	public String id;
	
	/**
	 * Ensemble des bordures de la zone
	 */
	private List<ZoneEdge> edges;
	
	/**
	 * Cellules uniques de la zone, rangées par position
	 */
	private Map<Point, ArenaCell> cells;
	
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
	
	private static Point tmp = new Point(0, 0);
	
	public ArenaZone() {
		borderThickness = Assets.edges.get(CellOwners.NEUTRAL).getHeight();

		cells = new HashMap<Point, ArenaCell>();
		edges = new ArrayList<ZoneEdge>();
		lines = new ArrayList<Sprite>();
		
		tmp = new Point(0, 0);
	}

	public void addCell(ArenaCell cell) {
		cells.put(cell.getData().position, cell);
	}
	
	public void removeCell(ArenaCell cell) {
		cells.remove(cell.getData().position);
	}
	
	public void update() {
		edges.clear();
		invalidate = true;
		for (ArenaCell cell : cells.values()) {
			// Affecte la zone à chaque cellule
			cell.getData().zone = this;
		
			// Ajoute les côtés uniques dans la liste
			checkEdge(cell, Borders.LEFT, -1, 0);
			checkEdge(cell, Borders.TOP, 0, +1);
			checkEdge(cell, Borders.RIGHT, +1, 0);
			checkEdge(cell, Borders.BOTTOM, 0, -1);
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
	private void checkEdge(ArenaCell cell, Borders border, int offsetX, int offsetY) {
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
		MaxValueFinder<CellOwners> occupations = new MaxValueFinder<CellOwners>();
		occupations.setValueIfDraw(CellOwners.NEUTRAL);
		
		// Compte le nombre de cellules occupées par chaque joueur
		CellData cellData;
		for (ArenaCell cell : cells.values()) {
			cellData = cell.getData();
			if (cellData.owner != CellOwners.NEUTRAL) {
				// Ajout de la puissance de la cellule à celle de ce joueur
				occupations.addValue(cellData.owner, cellData.power);
			}
		}
		
		// Détermine qui occupe le plus de cellules
		CellOwners newOwner = occupations.getMaxValue();
		
		// Change le propriétaire de la zone
		setOwner(newOwner);
	}
}
