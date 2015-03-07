package com.slamdunk.wordarena.data;

import java.util.List;

import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.enums.Letters;

public class ArenaData {
	public int width;
	public int height;
	public ArenaCell[][] cells;
	public List<ArenaZone> zones;
	public Deck<Letters> letterDeck;
	
	/**
	 * Nom de l'arène
	 */
	public String name;
}
