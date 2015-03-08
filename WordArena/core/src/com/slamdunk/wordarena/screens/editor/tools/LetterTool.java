package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

public class LetterTool implements EditorTool<Letters> {
	private Letters value;
	
	public LetterTool() {
		value = Letters.FROM_DECK;
	}

	@Override
	public void setValue(Letters value) {
		this.value = value;
	}
	
	@Override
	public Letters getValue() {
		return value;
	}

	@Override
	public void apply(ArenaCell cell) {
		CellTypes type = cell.getData().type;
		if (!type.hasLetter()
		|| type == CellTypes.J) {
			return;
		}
		cell.getData().letter = value;
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
