package com.slamdunk.toolkit.ui.loader.builders.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ButtonJsonBuilder extends JsonComponentBuilder {
	
	@Override
	protected Button createEmpty(Skin skin, String style) {
		if (style == null) {
			return new ImageButton(skin);
		}
		return new Button(skin, style);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		Button button = (Button)super.build(skin);
		
		return button;
	}
}
