package com.slamdunk.wordarena.data;

import com.slamdunk.wordarena.actors.ArenaCell;

public class WallData {
	public ArenaCell cell1;
	public ArenaCell cell2;
	
	/**
	 * Indique si le mur est actif. Si false, alors il n'empêche pas le passage
	 * entre les cellules.
	 */
	public boolean active;
	
	public WallData(ArenaCell cell1, ArenaCell cell2) {
		this.cell1 = cell1;
		this.cell2 = cell2;
		active = true;
	}
}
