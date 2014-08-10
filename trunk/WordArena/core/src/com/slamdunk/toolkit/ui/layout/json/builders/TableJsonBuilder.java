package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableJsonBuilder extends JsonComponentBuilder {
	
	@Override
	protected Table createEmpty(Skin skin, String style) {
		return new Table(skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		Table table = (Table)super.build(skin);
		return table;
	}
}
