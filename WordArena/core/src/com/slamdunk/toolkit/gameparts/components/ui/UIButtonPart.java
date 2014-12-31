package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.ButtonClickListener;

/**
 * Représente un bouton ou une case à cocher
 */
public class UIButtonPart extends UIComponent {
	public ButtonClickScript script;
	public String text;
	
	/**
	 * Utilisée uniquement lors de la création du widget,
	 * non modifiable à runtime.
	 */
	public boolean isCheckBox;
	
	private TextButton button;
	
	@Override
	public void reset() {
		script = null;
		text = "";
		isCheckBox = false;
	}
	
	@Override
	public void init() {
		if (isCheckBox) {
			button = new CheckBox(text, skin);
		} else {
			button = new TextButton(text, skin);
		}
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
