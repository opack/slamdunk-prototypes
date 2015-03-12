package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

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
		cell.getData().state = CellStates.OWNED;
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
