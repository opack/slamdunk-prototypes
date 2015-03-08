package com.slamdunk.wordarena.data;

import java.util.List;

import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaWall;
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
	public DoubleEntryArray<ArenaCell, ArenaCell, ArenaWall> walls;
	
	public Deck<Letters> letterDeck;
	
	/**
	 * Nom de l'arène
	 */
	public String name;
}
