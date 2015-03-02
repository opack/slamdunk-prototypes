package com.slamdunk.wordarena.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Représente les différentes images d'un pack pour cellule
 */
public class CellPack {
	/**
	 * Nom du pack
	 */
	public String name;
	
	/**
	 * Images des cellules en fonction d'un état de cellule et de sélection
	 */
	public DoubleEntryArray<CellStates, Boolean, TextureRegionDrawable> cell;
	
	/**
	 * Image de la bordure
	 */
	public Texture edge;
}
