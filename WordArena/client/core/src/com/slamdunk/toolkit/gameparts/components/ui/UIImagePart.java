package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Représente un Label
 */
public class UIImagePart extends UIComponent {
	public TextureRegion textureRegion;
	
	private Image image;
	
	@Override
	public void reset() {
		textureRegion = null;
	}
	
	@Override
	public void init() {
		image = new Image(textureRegion);
		actor = image;
		super.init();
	}
	
	@Override
	public void updateWidget() {
		image.setDrawable(new TextureRegionDrawable(textureRegion));
	}
}
