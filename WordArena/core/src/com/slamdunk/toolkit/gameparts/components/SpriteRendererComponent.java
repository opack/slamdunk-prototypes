package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteRendererComponent extends Component {
	public String spriteFile;
	public TextureRegion textureRegion;
	public Color tint = new Color(Color.WHITE);
	
	private TransformComponent transform;
	
	private Color tmpOrigBatchColor;
	private float textureWidth;
	private float textureHeight;
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformComponent.class);
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
		
		textureWidth = textureRegion.getRegionWidth();
		textureHeight = textureRegion.getRegionHeight();
		
		tmpOrigBatchColor = new Color(batch.getColor());
		batch.setColor(tint);
		batch.draw(textureRegion,
			transform.position.x - transform.anchor.x * textureWidth, transform.position.y - transform.anchor.y * textureHeight,
			transform.origin.x * textureWidth, transform.origin.y * textureHeight,
			textureWidth, textureHeight,
			transform.scale.x, transform.scale.y, 
			transform.rotation.z);
		batch.setColor(tmpOrigBatchColor);
	}
}
