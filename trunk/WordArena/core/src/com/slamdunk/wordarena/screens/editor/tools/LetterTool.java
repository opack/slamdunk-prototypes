package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

public class LetterTool extends EditorTool<Letters> {
	public LetterTool() {
		setValue(Letters.FROM_DECK);
	}

	@Override
	public void apply(ArenaCell cell) {
		CellTypes type = cell.getData().type;
		if (!type.hasLetter()
		|| type == CellTypes.J) {
			return;
		}
		cell.getData().letter = getValue();
		cell.getData().planLetter = getValue().label;
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
