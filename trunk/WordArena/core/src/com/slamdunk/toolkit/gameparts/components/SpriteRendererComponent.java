package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteRendererComponent extends Component {
	public String spriteFile;
	public TextureRegion textureRegion;
	public Color tint;
	
	private TransformComponent transform;
	private Color tmpOrigBatchColor;
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformComponent.class);
		tint = Color.WHITE;
		if (spriteFile != null
		&& !spriteFile.isEmpty()) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spriteFile)));
		}
	}

	@Override
	public void render(Batch batch) {
		tmpOrigBatchColor = batch.getColor();
		batch.setColor(tint);
		batch.draw(textureRegion,
			transform.position.x, transform.position.y,
			transform.origin.x, transform.origin.y,
			textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
			transform.scale.x, transform.scale.y, 
			transform.rotation.z);
		batch.setColor(tmpOrigBatchColor);
	}
}
