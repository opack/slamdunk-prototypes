package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

public class CellTypeTool extends EditorTool<CellTypes> {
	
	public CellTypeTool() {
		setValue(CellTypes.L);
	}

	@Override
	public void apply(ArenaCell cell) {
		CellData cellData = cell.getData();
		CellTypes value = getValue();
		cellData.type = value;
		if (!value.hasLetter()) {
			cellData.letter = Letters.EMPTY;
		}
		if (value == CellTypes.J) {
			cellData.letter = Letters.JOKER;
		}
		if (!value.hasPower()) {
			cellData.power = 0;
		}
		if (!value.canBeOwned()) {
			cellData.owner = Player.NEUTRAL;
		}
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
