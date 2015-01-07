package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.slamdunk.toolkit.gameparts.AnchorPoint;
import com.slamdunk.toolkit.gameparts.components.position.AlignScript;
import com.slamdunk.toolkit.gameparts.components.ui.ButtonClickScript;

public class ChangeAlignScript extends ButtonClickScript {
	private int align;
	
	@Override
	public void clicked(Button button) {
		AnchorPoint alignSpot = AnchorPoint.values()[align];
		gameObject.getComponent(AlignScript.class).gameObjectPointToAlign = alignSpot;
		align++;
		if (align == AnchorPoint.values().length) {
			align = 0;
		}
		System.out.println(alignSpot);
	}
}
