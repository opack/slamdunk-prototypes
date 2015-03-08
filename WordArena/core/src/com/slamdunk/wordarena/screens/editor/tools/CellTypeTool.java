package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

public class CellTypeTool implements EditorTool<CellTypes> {
	private CellTypes value;
	
	public CellTypeTool() {
		value = CellTypes.L;
	}

	@Override
	public void setValue(CellTypes value) {
		this.value = value;
	}
	
	@Override
	public CellTypes getValue() {
		return value;
	}

	@Override
	public void apply(ArenaCell cell) {
		cell.getData().type = value;
		if (!value.hasLetter()) {
			cell.getData().letter = Letters.EMPTY;
		}
		if (value == CellTypes.J) {
			cell.getData().letter = Letters.JOKER;
		}
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
