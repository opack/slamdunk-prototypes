package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.slamdunk.toolkit.gameparts.components.ui.ButtonClickScript;

public class ButtonClickTestScript extends ButtonClickScript {
	@Override
	public void clicked(Button button) {
		System.out.println("ButtonClickTestScript.clicked()");
	}
}
