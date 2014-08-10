package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ImageJsonBuilder extends JsonComponentBuilder {
	
	@Override
	protected Image createEmpty(Skin skin, String style) {
		return new Image();
	}
	
	@Override
	public Actor build(Skin skin) {
		// G�re les propri�t�s basiques du widget
		Image image = (Image)super.build(skin);
		
		// G�re la propri�t� image
		parseImage(skin, image);
		
		return image;
	}

	private void parseImage(Skin skin, Image image) {
		if (hasProperty("image")) {
			String atlasRegionName = actorDescription.getString("image");
			if (skin.has(atlasRegionName, Drawable.class)) {
				Drawable drawable = skin.getDrawable(atlasRegionName);
				image.setDrawable(drawable);
			}
		}
	}
}
