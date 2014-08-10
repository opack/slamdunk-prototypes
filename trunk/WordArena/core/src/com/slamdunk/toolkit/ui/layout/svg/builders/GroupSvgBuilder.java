package com.slamdunk.toolkit.ui.layout.svg.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GroupSvgBuilder extends SvgComponentBuilder {
	
	@Override
	protected Group createEmpty(Skin skin, String style) {
		return new Group();
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		Group group = (Group)super.build(skin);
		return group;
	}
}
