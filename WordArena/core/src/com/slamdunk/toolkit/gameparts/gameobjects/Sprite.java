package com.slamdunk.toolkit.gameparts.gameobjects;

import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererComponent;

public class Sprite extends GameObject {
	public SpriteRendererComponent spriteRenderer;
	
	public Sprite() {
		super();
		
		spriteRenderer = addComponent(SpriteRendererComponent.class);
	}
}
