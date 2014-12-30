package com.slamdunk.toolkit.gameparts.components.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;

public class SpriteRendererPart extends Component {
	public String spriteFile;
	public TextureRegion textureRegion;
	public Color tint;
	
	/**
	 * Indique à partir de quel point (exprimé en % de la taille de l'image)
	 * est dessiné le Sprite. Par défaut, le sprite est dessiné avec le coin
	 * coin bas-gauche à l'emplacement de TransformComponent.worldPosition.
	 * Pour centrer, il suffit de placer anchor à 0.5,0.5.
	 */
	public Vector2 anchor;
	
	/**
	 * Indique quel point (exprimé en % de la taille de l'image) sert d'origine
	 * pour le calcul de la rotation et de la mise à l'échelle. Par défaut,
	 * c'est le point à l'emplacement de TransformComponent.worldPosition.
	 * Pour centrer, il suffit de placer origin à 0.5,0.5.
	 */
	public Vector2 origin;
	
	private TransformPart transform;
	
	private Color tmpOrigBatchColor;
	private float tmpTextureWidth;
	private float tmpTextureHeight;
	
	public SpriteRendererPart() {
		anchor = new Vector2();
		origin = new Vector2();
	}
	
	@Override
	public void reset() {
		spriteFile = null;
		textureRegion = null;
		tint = new Color(Color.WHITE);
		anchor.set(0, 0);
		origin.set(0, 0);
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformPart.class);
		if (spriteFile != null
		&& !spriteFile.isEmpty()) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spriteFile)));
		}
	}

	@Override
	public void render(Batch batch) {
		if (textureRegion == null) {
			return;
		}
		
		tmpTextureWidth = textureRegion.getRegionWidth();
		tmpTextureHeight = textureRegion.getRegionHeight();
		
		tmpOrigBatchColor = new Color(batch.getColor());
		batch.setColor(tint);
		batch.draw(textureRegion,
			transform.worldPosition.x - anchor.x * tmpTextureWidth, transform.worldPosition.y - anchor.y * tmpTextureHeight,
			origin.x * tmpTextureWidth, origin.y * tmpTextureHeight,
			tmpTextureWidth, tmpTextureHeight,
			transform.worldScale.x, transform.worldScale.y, 
			transform.worldRotation.z);
		batch.setColor(tmpOrigBatchColor);
	}
}
