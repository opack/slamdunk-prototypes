package com.slamdunk.toolkit.ui.loader.builders.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonJsonBuilder extends JsonComponentBuilder {
	private String language;
	
	public TextButtonJsonBuilder(String language) {
		this.language = language;
	}
	
	@Override
	protected TextButton createEmpty(Skin skin, String style) {
		if (style == null) {
			return new TextButton("", skin);
		}
		return new TextButton("", skin, style);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		TextButton button = (TextButton)super.build(skin);
		
		// Gère les propriétés spécifiques du bouton
		parseTextKey(button);
		parseText(button);
		
		return button;
	}

	protected void parseText(TextButton button) {
		if (hasProperty("text")) {
			button.setText(getStringProperty("text"));
		}
	}
	
	protected void parseTextKey(TextButton button) {
		if (hasProperty("text-key")) {
			String key = getStringProperty("text-key");
			button.setText(getStringValue(key, language));
		}
	}
}
