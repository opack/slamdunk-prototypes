package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

public class LetterTool extends EditorTool<Letters> {
	public LetterTool() {
		setValue(Letters.FROM_TYPE);
	}

	@Override
	public void apply(ArenaCell cell) {
		CellTypes type = cell.getData().type;
		
		// On ne fait rien pour les cellules d'un type
		// n'affichant pas de lettre ou pour le joker
		// (dont la lettre sera toujours "?")
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
