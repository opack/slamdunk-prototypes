package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

public class ApplyToolListener extends InputListener {
	private ArenaCell cell;
	private EditorScreen screen;
	
	public ApplyToolListener(EditorScreen screen, ArenaCell cell) {
		this.screen = screen;
		this.cell = cell;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		screen.getCurrentTool().apply(cell);
		cell.updateDisplay();
	}
}

