package com.slamdunk.wordarena.actors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.enums.Borders;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class Cell extends GroupEx {
	/**
	 * Les 4 images placées sur les bords de la cellule
	 */
	private final Map<Borders, Image> borders;
	
	/**
	 * L'image représentant la lettre de cette cellule
	 */
	private final Image letter;
	
	/**
	 * Le modèle de cette cellule
	 */
	private final CellData data;
	
	public Cell() {
		// Crée les composants de la cellule
		data = new CellData();
		
		borders = new HashMap<Borders, Image>();
		for (Borders border : Borders.values()) {
			Image image = new Image(Assets.borders.get(border, data.zoneOnBorder.get(border)));
			borders.put(border, image);
		}
		
		letter = new Image(Assets.letters.get(data.letter, data.state));
		
		// Place les images aux bons endroits
		layoutImages();		
		
		// Met à jour les images
		updateCellImages();
	}

	/**
	 * Place les images aux bons endroits
	 */
	private void layoutImages() {
		final float cellX = getX();
		final float cellY = getY();
		final float borderThickness = borders.get(Borders.LEFT).getWidth();
		final float letterWidth = letter.getWidth();
		
		borders.get(Borders.LEFT).setPosition(cellX, cellY);
		borders.get(Borders.BOTTOM).setPosition(cellX, cellY);
		borders.get(Borders.RIGHT).setPosition(cellX + borderThickness + letterWidth, cellY);
		borders.get(Borders.TOP).setPosition(cellX, cellY + borderThickness + letterWidth);
		
		letter.setPosition(cellX + borderThickness, cellY + borderThickness);
		
		setSize(borders.get(Borders.BOTTOM).getWidth(), borders.get(Borders.LEFT).getHeight());
	}

	/**
	 * Met à jour les images de cette cellule
	 */
	private void updateCellImages() {
		letter.setDrawable(Assets.letters.get(data.letter, data.state));
		
		for (Borders border : Borders.values()) {
			borders.get(border).setDrawable(Assets.borders.get(border, data.zoneOnBorder.get(border)));
		}
	}
}
