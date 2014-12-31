package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;

public class UIButtonPart extends UIComponent {
	public ButtonClickScript script;
	public String text;
	
	private TextButton button;
	
	@Override
	public void init() {
		button = new TextButton(text, skin);
		button.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				if (script != null) {
					script.clicked(button);
				}
			}
		});
		
		actor = button;
		super.init();
	}
	
	@Override
	public void updateWidget() {
		if (!button.getText().equals(text)) {
			button.setText(text);
		}
	}
}
