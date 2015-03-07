package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.wordarena.WordSelectionHandler;

public class CellSelectionListener extends InputListener {
	private ArenaCell cell;
	private WordSelectionHandler wordSelectionHandler;
	
	public CellSelectionListener(ArenaCell cell, WordSelectionHandler wordSelectionHandler) {
		this.cell = cell;
		this.wordSelectionHandler = wordSelectionHandler;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (cell.getData().type.isSelectable()) {
			wordSelectionHandler.selectCell(cell);
		}
	}
}

