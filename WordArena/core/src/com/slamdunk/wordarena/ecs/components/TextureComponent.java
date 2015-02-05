package com.slamdunk.wordarena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent extends Component {
	public TextureRegion region = null;
	public Color tint = new Color(Color.WHITE);
}
