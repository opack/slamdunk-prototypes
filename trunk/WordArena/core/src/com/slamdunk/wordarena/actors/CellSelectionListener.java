package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.toolkit.ui.MoveCameraDragListener;
import com.slamdunk.wordarena.WordSelectionHandler;

public class CellSelectionListener extends InputListener {
	private ArenaCell cell;
	private WordSelectionHandler wordSelectionHandler;
	
	/**
	 * Permet d'interdire la sélection d'une lettre en cas de déplacement
	 * du pointeur
	 */
	private MoveCameraDragListener moveCameraDragListener;
	
	public CellSelectionListener(ArenaCell cell, WordSelectionHandler wordSelectionHandler, MoveCameraDragListener moveCameraDragListener) {
		this.cell = cell;
		this.wordSelectionHandler = wordSelectionHandler;
		this.moveCameraDragListener = moveCameraDragListener;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (!moveCameraDragListener.isDragging()
		&& cell.getData().type.isSelectable()) {
			wordSelectionHandler.selectCell(cell);
		}
	}
}

