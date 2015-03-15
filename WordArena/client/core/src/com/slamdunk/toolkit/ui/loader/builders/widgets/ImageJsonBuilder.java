package com.slamdunk.toolkit.ui.loader.builders.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageJsonBuilder extends JsonComponentBuilder {
	
	@Override
	protected Image createEmpty(Skin skin, String style) {
		return new Image();
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		Image image = (Image)super.build(skin);
		
		// Gère la propriété image
		parseImage(skin, image);
		
		// Gère la propriété image-file
		parseImageFile(skin, image);
		
		return image;
	}

	private void parseImage(Skin skin, Image image) {
		if (hasProperty("image")) {
			String atlasRegionName = getStringProperty("image");
			if (skin.has(atlasRegionName, Drawable.class)) {
				Drawable drawable = skin.getDrawable(atlasRegionName);
				image.setDrawable(drawable);
			}
		}
	}
	
	private void parseImageFile(Skin skin, Image image) {
		if (hasProperty("image-file")) {
			String filename = getStringProperty("image-file");
			if (!filename.isEmpty()) {
				FileHandle imageFile = Gdx.files.internal(filename);
				Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(imageFile)));
				image.setDrawable(drawable);
			}
		}
	}
}
