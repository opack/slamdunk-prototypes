package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellStates;

public class OwnerTool extends EditorTool<Player> {
	
	public OwnerTool() {
		setValue(Player.NEUTRAL);
	}

	@Override
	public void apply(ArenaCell cell) {
		CellData data = cell.getData();
		if (!data.type.canBeOwned()) {
			return;
		}
		data.owner = getValue();
		data.state = CellStates.OWNED;
		
		// Met à jour l'owner de la zone
		data.zone.updateOwner();
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
