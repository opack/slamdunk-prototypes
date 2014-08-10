package com.slamdunk.wordarena.old.numberduel;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GridCell {
	public Numbers value;
	public Bonus bonus;
	public TextButton button;
	public int x;
	public int y;
	
	public boolean isNeighbor(GridCell cell) {
		// La cellule peut �tre s�lectionn�e apr�s celle-ci si
		// c'est une voisine
		return Math.abs(cell.x - x) <= 1 && Math.abs(cell.y - y) <= 1;
	}
}
