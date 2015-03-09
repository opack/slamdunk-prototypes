package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;

public class ZoneTool extends EditorTool<ArenaZone> {
	@Override
	public void apply(ArenaCell cell) {
		ArenaZone zone = getValue();
		if (zone == null) {
			ArenaZone oldZone = cell.getData().zone;
			cell.getData().zone = null;
			if (oldZone != null) {
				oldZone.removeCell(cell);
				oldZone.update();
			}
			return;
		}
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
