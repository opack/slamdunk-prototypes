package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Représente un Label
 */
public class UITextFieldPart extends UIComponent {
	public String text;
	
	private TextField textField;
	
	@Override
	public void reset() {
		text = "";
	}
	
	@Override
	public void init() {
		textField = new TextField(text, skin);
		actor = textField;
		super.init();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		text = textField.getText();
	}
	
	@Override
	public void updateWidget() {
		if (!textField.getText().equals(text)) {
			textField.setText(text);
		}
	}
}
