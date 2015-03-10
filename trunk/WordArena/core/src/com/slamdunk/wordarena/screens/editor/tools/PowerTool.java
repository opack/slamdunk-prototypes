package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;

public class PowerTool extends EditorTool<Integer> {
	public PowerTool() {
		setValue(1);
	}

	@Override
	public void apply(ArenaCell cell) {
		if (!cell.getData().type.hasPower()) {
			return;
		}
		cell.getData().power = getValue();
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
