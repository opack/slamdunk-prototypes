package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ScrollPaneJsonBuilder extends JsonComponentBuilder {
	
	@Override
	protected ScrollPane createEmpty(Skin skin, String style) {
		return new ScrollPane(null);
	}
}
