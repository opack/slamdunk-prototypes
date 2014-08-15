package com.slamdunk.toolkit.ui.layout.json.builders;

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
		// G�re les propri�t�s basiques du widget
		TextField textField = (TextField)super.build(skin);
		
		// G�re la propri�t� TextField
		parseTextKey(textField);
		parseText(textField);
		
		return textField;
	}

	private void parseText(TextField textField) {
		if (hasProperty("text")) {
			textField.setText(actorDescription.getString("text"));
		}
	}
	
	protected void parseTextKey(TextField textField) {
		if (hasProperty("text-key")) {
			String key = actorDescription.getString("text-key");
			textField.setText(getValueString(key, language));
		}
	}
}
