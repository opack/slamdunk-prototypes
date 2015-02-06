package com.slamdunk.wordarena.actors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class ArenaCell extends GroupEx {
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
	
	public ArenaCell() {
		// Crée les composants de la cellule
		data = new CellData();
		
		borders = new HashMap<Borders, Image>();
		for (Borders border : Borders.values()) {
			Image image = new Image(Assets.borders.get(border, data.zoneOnBorder.get(border)));
			borders.put(border, image);
			addActor(image);
		}
		
		letter = new Image(Assets.letters.get(data.letter, data.state));
		addActor(letter);
		
		// Place les images aux bons endroits
		layoutImages();		
		
		// Met à jour les images
		updateCellImages();
		
		// Ajoute le listener pour sélectionner la lettre
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return WordSelectionHandler.getInstance().addCell(ArenaCell.this);
			}
		});
	}
	
	public CellData getData() {
		return data;
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
	public void updateCellImages() {
		letter.setDrawable(Assets.letters.get(data.letter, data.state));
		
		for (Borders border : Borders.values()) {
			borders.get(border).setDrawable(Assets.borders.get(border, data.zoneOnBorder.get(border)));
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ArenaCell) {
			// 2 cellules sont considérées identiques si elles sont
			// au même endroit
			ArenaCell cell2 = (ArenaCell)other;
			return cell2.data.position.equals(data.position);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return data.position.hashCode();
	}

	/**
	 * Sélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à SELECTED) et l'image de la lettre
	 */
	public void select() {
		data.state = CellStates.SELECTED;
		updateCellImages();
	}
	
	/**
	 * Désélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à NORMAL) et l'image de la lettre
	 */
	public void unselect() {
		data.state = CellStates.NORMAL;
		updateCellImages();
	}
}
