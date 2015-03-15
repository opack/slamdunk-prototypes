package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.enums.Letters;

public class ArenaData {
	public int width;
	public int height;
	
	/**
	 * Contient les différentes cellules de l'arène
	 */
	public ArenaCell[][] cells;
	
	/**
	 * Contient les différentes zones de l'arène
	 */
	public List<ArenaZone> zones;
	
	/**
	 * Indique s'il y a un mur entre les 2 cellules indiquées
	 */
	public DoubleEntryArray<ArenaCell, ArenaCell, Boolean> walls;
	
	public Deck<Letters> letterDeck;
	
	/**
	 * Nom de l'arène
	 */
	public String name;
	
	public ArenaData() {
		zones = new ArrayList<ArenaZone>();
		walls = new DoubleEntryArray<ArenaCell, ArenaCell, Boolean>();
	}
	
	/**
	 * Indique s'il y a un mur entre les 2 cellules spécifiées
	 * @param cell1
	 * @param cell2
	 * @return
	 */
	public boolean hasWall(ArenaCell cell1, ArenaCell cell2) {
		Boolean wall = walls.get(cell1, cell2);
		return wall != null
			&& wall == Boolean.TRUE;
	}

	/**
	 * Ajoute un mur entre cell1 et cell2.
	 * Ce mur est ajouté 2 fois : une fois de cell1 vers cell2, et une fois
	 * dans l'autre sens. Ainsi la recherche de mur via {@link #hasWall(ArenaCell, ArenaCell)}
	 * se fait rapidement et sans se préoccuper du sens.
	 * @param cell1
	 * @param cell2
	 */
	public void addWall(ArenaCell cell1, ArenaCell cell2) {
		walls.put(cell1, cell2, Boolean.TRUE);
		walls.put(cell2, cell1, Boolean.TRUE);
	}
	
	/**
	 * Supprime un mur entre cell1 et cell2.
	 * Ce mur est supprimé 2 fois : une fois de cell1 vers cell2, et une fois
	 * dans l'autre sens.
	 * @param cell1
	 * @param cell2
	 */
	public void removeWall(ArenaCell cell1, ArenaCell cell2) {
		walls.remove(cell1, cell2, Boolean.TRUE);
		walls.remove(cell2, cell1, Boolean.TRUE);
	}
	
	/**
	 * Retourne une liste des 8 voisins de la cellule indiquée
	 * @param cell
	 * @param listToFill Liste qui contiendra les résultats
	 * @return
	 */
	public void getNeighbors8(ArenaCell cell, List<ArenaCell> listToFill) {
		final int x = cell.getData().position.getX();
		final int y = cell.getData().position.getY();
		
		addCellIfValidPos(x - 1, y - 1, listToFill);
		addCellIfValidPos(x + 0, y - 1, listToFill);
		addCellIfValidPos(x + 1, y - 1, listToFill);
		
		addCellIfValidPos(x - 1, y + 0, listToFill);
		addCellIfValidPos(x + 1, y + 0, listToFill);
		
		addCellIfValidPos(x - 1, y + 1, listToFill);
		addCellIfValidPos(x + 0, y + 1, listToFill);
		addCellIfValidPos(x + 1, y + 1, listToFill);
	}
	
	/**
	 * Retourne une liste des 4 voisins (haut, bas, gauche, droite) 
	 * de la cellule indiquée
	 * @param cell
	 * @param listToFill Liste qui contiendra les résultats
	 * @return
	 */
	public void getNeighbors4(ArenaCell cell, List<ArenaCell> listToFill) {
		final int x = cell.getData().position.getX();
		final int y = cell.getData().position.getY();
		
		addCellIfValidPos(x + 0, y - 1, listToFill);
		
		addCellIfValidPos(x - 1, y + 0, listToFill);
		addCellIfValidPos(x + 1, y + 0, listToFill);
		
		addCellIfValidPos(x + 0, y + 1, listToFill);
	}
	
	private void addCellIfValidPos(int x, int y, List<ArenaCell> listToFill) {
		if (isValidPos(x, y)) {
			listToFill.add(cells[x][y]);
		}
	}

	/**
	 * Indique si la position existe dans l'arène
	 * @param pos
	 * @return
	 */
	public boolean isValidPos(Point pos) {
		return pos.getX() > -1
			&& pos.getX() < width
			&& pos.getY() > -1
			&& pos.getY() < height;
	}
	
	/**
	 * Indique si la position existe dans l'arène
	 * @param pos
	 * @return
	 */
	public boolean isValidPos(int x, int y) {
		return x > -1
			&& x < width
			&& y > -1
			&& y < height;
	}
}
