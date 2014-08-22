package com.slamdunk.toolkit.ui.loader.builders.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TextFieldJsonBuilder extends JsonComponentBuilder {
	private String language;
	
	public TextFieldJsonBuilder(String language) {
		this.language = language;
	}
	
	@Override
	protected TextField createEmpty(Skin skin, String style) {
		return new TextField("", skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		TextField textField = (TextField)super.build(skin);
		
		// Gère la propriété TextField
		parseTextKey(textField);
		parseText(textField);
		
		return textField;
	}

	private void parseText(TextField textField) {
		if (hasProperty("text")) {
			textField.setText(getStringProperty("text"));
		}
	}
	
	protected void parseTextKey(TextField textField) {
		if (hasProperty("text-key")) {
			String key = getStringProperty("text-key");
			textField.setText(getStringValue(key, language));
		}
	}
}
