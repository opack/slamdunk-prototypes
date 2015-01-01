package com.slamdunk.wordarena.gameparts.scripts;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.slamdunk.toolkit.gameparts.components.position.AlignScript;
import com.slamdunk.toolkit.gameparts.components.position.AlignScript.AlignSpots;
import com.slamdunk.toolkit.gameparts.components.ui.ButtonClickScript;

public class ChangeAlignScript extends ButtonClickScript {
	private int align;
	
	@Override
	public void clicked(Button button) {
		AlignSpots alignSpot = AlignSpots.values()[align];
		gameObject.getComponent(AlignScript.class).alignSpot = alignSpot;
		align++;
		if (align == AlignSpots.values().length) {
			align = 0;
		}
		System.out.println(alignSpot);
	}
}
