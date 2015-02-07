package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.toolkit.ui.GroupEx;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;

/**
 * Une cellule de l'arène. Une cellule contient bien sûr une lettre
 * mais aussi 4 bords qui indique à quelle zone appartient la cellule.
 */
public class ArenaCell extends GroupEx {
	/**
	 * Le modèle de cette cellule
	 */
	private final CellData data;
	
	private Image background;
	
	private Label letter;
	
	public ArenaCell(final Skin skin, final WordSelectionHandler wordSelectionHandler) {
		// Crée les composants de la cellule
		data = new CellData();
		
		// Crée les acteurs
		background = new Image(Assets.cells.get(data.owner, data.state));
		background.setTouchable(Touchable.disabled);
		addActor(background);
		letter = new Label("?", skin);
		letter.setTouchable(Touchable.disabled);
		letter.setPosition(background.getWidth() / 2, background.getHeight() / 2, Align.center);
		addActor(letter);
		
		// Ajoute le listener pour sélectionner la lettre
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				wordSelectionHandler.addCell(ArenaCell.this);
				return true;
			}
		});
	}
	
	public CellData getData() {
		return data;
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
	
	public void updateDisplay() {
		letter.setText(data.letter.label);
		background.setDrawable(Assets.cells.get(data.owner, data.state));
	}

	/**
	 * Sélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à SELECTED) et l'image de la lettre
	 */
	public void select() {
		data.state = CellStates.SELECTED;
		updateDisplay();
	}
	
	/**
	 * Désélectionne la cellule, ce qui a pour effet de changer son état
	 * (qui passe à NORMAL) et l'image de la lettre
	 */
	public void unselect() {
		data.state = CellStates.NORMAL;
		updateDisplay();
	}

	/**
	 * Change le propriétaire de la cellule et met à jour l'image
	 * @param owner
	 */
	public void setOwner(CellOwners owner) {
		data.owner = owner;
		updateDisplay();		
	}
}
