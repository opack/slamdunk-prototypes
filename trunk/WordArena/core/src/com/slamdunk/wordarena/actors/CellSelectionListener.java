package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.wordarena.WordSelectionHandler;

public class CellSelectionListener extends InputListener {
	private ArenaCell cell;
	private WordSelectionHandler wordSelectionHandler;
	
	/**
	 * Permet d'interdire la sélection d'une lettre en cas de déplacement
	 * du pointeur
	 */
	private boolean moving;
	
	public CellSelectionListener(ArenaCell cell, WordSelectionHandler wordSelectionHandler) {
		this.cell = cell;
		this.wordSelectionHandler = wordSelectionHandler;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		moving = false;
		return true;
	}
	
	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		moving = true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (!moving
		&& cell.getData().type.isSelectable()) {
			wordSelectionHandler.selectCell(cell);
		}
	}
}

