package com.slamdunk.toolkit.gameparts.gameobjects;

import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;

public class Sprite extends GameObject {
	public SpriteRendererPart spriteRenderer;
	
	public Sprite() {
		super();
		
		spriteRenderer = addComponent(SpriteRendererPart.class);
	}
}
