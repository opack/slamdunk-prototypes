package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Représente un Label
 */
public class UILabelPart extends UIComponent {
	public String text;
	
	private Label label;
	
	@Override
	public void reset() {
		text = "";
	}
	
	@Override
	public void init() {
		label = new Label(text, skin);
		actor = label;
		super.init();
	}
	
	@Override
	public void updateWidget() {
		if (!label.getText().equals(text)) {
			label.setText(text);
		}
	}
}
