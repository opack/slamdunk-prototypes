package com.slamdunk.wordarena.gameparts.prefabs;

import com.slamdunk.toolkit.gameparts.components.TouchHandlerPart;
import com.slamdunk.toolkit.gameparts.components.position.BoundsPart;
import com.slamdunk.toolkit.gameparts.components.position.GridPositionPart;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.wordarena.gameparts.scripts.LetterChooserScript;
import com.slamdunk.wordarena.gameparts.scripts.LetterSelectionScript;

public class LetterCase extends GameObject {
	public LetterCase() {
		addComponent(BoundsPart.class);
		addComponent(GridPositionPart.class);
		addComponent(SpriteRendererPart.class);
		addComponent(LetterChooserScript.class);
		addComponent(LetterSelectionScript.class);
		addComponent(TouchHandlerPart.class).handler = getComponent(LetterSelectionScript.class);
	}
}
