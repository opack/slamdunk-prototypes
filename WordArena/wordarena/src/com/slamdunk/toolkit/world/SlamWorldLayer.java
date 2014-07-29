package com.slamdunk.toolkit.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Une couche du monde. La couche peut contenir des acteurs
 * et poss�de des propri�t�s comme la transparence...
 */
public class SlamWorldLayer extends Group {
	
	/**
	 * R�f�rence vers le monde auquel appartient cette couche
	 */
	private SlamWorld world;
	
	/**
	 * Image de fond de la couche
	 */
	private Image background;
	
	public SlamWorldLayer(SlamWorld world) {
		this.world = world;
	}
	
	public Image getBackgroundImage() {
		return background;
	}

	public void setBackgroundImage(Image image) {
		this.background = image;
	}
	
	public void setBackgroundImage(TextureRegion textureBackground, Scaling scaling, boolean fillParent, boolean touchable) {
		Drawable tBg = new TextureRegionDrawable(textureBackground);
		background = new Image(tBg, scaling);
		background.setFillParent(fillParent);

		if (!touchable) {
			background.setTouchable(Touchable.disabled);
		}

		addActor(background);

		// On s'assure que l'image de fond est... au fond ;)
		background.setZIndex(0);
	}
}
