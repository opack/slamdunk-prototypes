package com.slamdunk.toolkit.gameparts.components.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.AnchorPoint;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.BoundsPart;
import com.slamdunk.toolkit.gameparts.components.position.SizePart;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;

public class SpriteRendererPart extends Component {
	public String spriteFile;
	public TextureRegion textureRegion;
	public Color tint;
	
	/**
	 * Indique quel point de l'image sera aligné sur les coordonnées du TransformPart.
	 * Par exemple, si la valeur est TOP_RIGHT, cela signifie que le coin
	 * haut-droit de l'image sera aligné sur le TransformPart du GameObject.
	 * Par défaut, la valeur est BOTTOM_LEFT.
	 */
	public AnchorPoint anchor;
	
	/**
	 * Indique quel point (exprimé en % de la taille de l'image) sert d'origine
	 * pour le calcul de la rotation et de la mise à l'échelle. Par défaut,
	 * c'est le point à l'emplacement de TransformComponent.worldPosition.
	 * Pour centrer, il suffit de placer origin à 0.5,0.5.
	 */
	public Vector2 origin;
	
	/**
	 * SizePart à mettre à jour avec les dimensions du sprite
	 * effectivement dessiné
	 */
	public SizePart size;
	
	/**
	 * BoundsPart à mettre à jour avec les coordonnées et dimensions du sprite
	 * effectivement dessiné
	 */
	public BoundsPart bounds;
	
	private TransformPart transform;
	
	private Color tmpOrigBatchColor;
	private float tmpTextureWidth;
	private float tmpTextureHeight;
	private Vector3 tmpDrawPosition;
	
	public SpriteRendererPart() {
		origin = new Vector2();
		tmpDrawPosition = new Vector3();
	}
	
	@Override
	public void reset() {
		spriteFile = null;
		textureRegion = null;
		tint = new Color(Color.WHITE);
		anchor = AnchorPoint.BOTTOM_LEFT;
		origin.set(0, 0);
	}
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformPart.class);
		if (spriteFile != null
		&& !spriteFile.isEmpty()) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spriteFile)));
		}
		
		size = gameObject.getComponent(SizePart.class);
		bounds = gameObject.getComponent(BoundsPart.class);
	}

	@Override
	public void render(Batch batch, ShapeRenderer shapeRenderer) {
		if (textureRegion == null) {
			return;
		}
		
		tmpTextureWidth = textureRegion.getRegionWidth();
		tmpTextureHeight = textureRegion.getRegionHeight();
		if (size != null) {
			size.width = tmpTextureWidth;
			size.height = tmpTextureHeight;
		}
		
		// Calcule l'offset du dessin
		anchor.computeAlignedPosition(transform.worldPosition, tmpTextureWidth, tmpTextureHeight, tmpDrawPosition);
		
		tmpOrigBatchColor = new Color(batch.getColor());
		batch.setColor(tint);
		batch.draw(textureRegion,
			tmpDrawPosition.x, tmpDrawPosition.y,
			origin.x * tmpTextureWidth, origin.y * tmpTextureHeight,
			tmpTextureWidth, tmpTextureHeight,
			transform.worldScale.x, transform.worldScale.y, 
			transform.worldRotation.z);
		batch.setColor(tmpOrigBatchColor);
		
		if (bounds != null) {
			bounds.x = tmpDrawPosition.x;
			bounds.y = tmpDrawPosition.y;
			bounds.width = tmpTextureWidth;
			bounds.height = tmpTextureHeight;
		}
	}
}
