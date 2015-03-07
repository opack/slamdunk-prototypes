package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpritedActor extends Actor {
	private Sprite sprite;
	
	public SpritedActor(Sprite sprite) {
		this.sprite = sprite;
		setPosition(sprite.getX(), sprite.getY());
		setSize(sprite.getWidth(), sprite.getHeight());
		setOrigin(sprite.getOriginX(), sprite.getOriginY());
		setRotation(sprite.getRotation());
		setScale(sprite.getScaleX(), sprite.getScaleY());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Met à jour la position du sprite
		sprite.setPosition(getX(), getY());
		sprite.setSize(getWidth(), getHeight());
		sprite.setOrigin(getOriginX(), getOriginY());
		sprite.setRotation(getRotation());
		sprite.setScale(getScaleX(), getScaleY());
		sprite.draw(batch, parentAlpha);
	}
}
