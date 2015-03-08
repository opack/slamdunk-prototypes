package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.Player;

public class OwnerTool extends EditorTool<Player> {
	
	public OwnerTool() {
		setValue(Player.NEUTRAL);
	}

	@Override
	public void apply(ArenaCell cell) {
		if (!cell.getData().type.canBeOwned()) {
			return;
		}
		cell.getData().owner = getValue();
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
