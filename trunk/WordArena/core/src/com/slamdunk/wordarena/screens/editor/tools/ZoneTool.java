package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;

public class ZoneTool extends EditorTool<ArenaZone> {
	@Override
	public void apply(ArenaCell cell) {
		// Retire la cellule de l'ancienne zone
		ArenaZone oldZone = cell.getData().zone;
		if (oldZone != null) {
			oldZone.removeCell(cell);
			oldZone.update();
		}
		
		// Ajoute la cellule à la nouvelle zone
		ArenaZone zone = getValue();
		zone.addCell(cell);
		zone.update();
	}

	@Override
	public void apply(Collection<ArenaCell> cells) {
		for (ArenaCell cell : cells) {
			apply(cell);
		}
	}
}
