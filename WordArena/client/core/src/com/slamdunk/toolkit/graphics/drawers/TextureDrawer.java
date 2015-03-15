package com.slamdunk.toolkit.graphics.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureDrawer {
	private TextureRegion textureRegion;
	private boolean isActive;
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void draw(Actor actor, Batch batch) {
		if (isActive && textureRegion != null) {
			batch.draw(textureRegion,
				actor.getX(), actor.getY(),
				actor.getOriginX(), actor.getOriginY(),
				actor.getWidth(), actor.getHeight(),
				actor.getScaleX(), actor.getScaleY(), 
				actor.getRotation());
		}
	}
}
