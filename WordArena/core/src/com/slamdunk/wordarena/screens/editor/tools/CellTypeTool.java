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
		if (cellData.type == value) {
			return;
		}
		// Affecte le type de la cellule
		cellData.type = value;
		
		// Choix de la lettre
		if (value == CellTypes.J) {
			cellData.letter = Letters.JOKER;
		} else {
			cellData.letter = value.hasLetter() ? Letters.getFromLabel(cellData.planLetter) : Letters.EMPTY;
		}
		
		// Choix d'une puissance
		cellData.power = value.hasPower() ? 1 : 0;
		
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
