package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
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
}
