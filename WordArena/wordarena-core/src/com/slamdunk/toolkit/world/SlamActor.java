package com.slamdunk.toolkit.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slamdunk.toolkit.drawers.AnimationDrawer;
import com.slamdunk.toolkit.drawers.ParticleDrawer;
import com.slamdunk.toolkit.drawers.TextureDrawer;
import com.slamdunk.toolkit.settings.SlamViewportSettings;

public class SlamActor extends Actor {

	/**
	 * Objet gérant le dessin d'une texture simple
	 */
	private TextureDrawer textureDrawer;
	
	/**
	 * Objet gérant les animations
	 */
	private AnimationDrawer animationDrawer;

	/**
	 * Objet gérant les particules
	 */
	private ParticleDrawer particleDrawer;
	
	/**
	 * Couche du monde dans laquelle se trouve cet acteur.
	 */
	private SlamWorldLayer worldLayer;

	public SlamActor(TextureRegion textureRegion, boolean isTextureRegionActive,
			float posX, float posY,
			float width, float height,
			float orgnX, float orgnY,
			boolean isDIPActive) {
		this(textureRegion, isTextureRegionActive, posX, posY, width, height, isDIPActive);
		setOrigin(orgnX, orgnY);
	}

	public SlamActor(TextureRegion textureRegion, boolean isTextureRegionActive,
			float posX, float posY,
			float width, float height,
			boolean isDIPActive) {
		this(posX, posY, width, height, isDIPActive);
		textureDrawer.setTextureRegion(textureRegion);
		textureDrawer.setActive(isTextureRegionActive);
	}

	public SlamActor(float posX, float posY, float width, float height, boolean isDIPActive) {
		this(width, height, isDIPActive);
		setBounds(posX, posY, width, height);
		setPosition(posX, posY);
	}

	public SlamActor(float width, float height, boolean isDIPActive) {
		super();
		if (isDIPActive) {
			float ratioSize = SlamViewportSettings.getWorldSizeRatio();
			setSize(width * ratioSize, height * ratioSize);
		} else {
			setSize(width, height);
		}
	}

	public SlamActor() {
		super();
	}
	

	public AnimationDrawer getAnimationDrawer() {
		return animationDrawer;
	}

	public TextureDrawer getTextureDrawer() {
		return textureDrawer;
	}

	public ParticleDrawer getParticleDrawer() {
		return particleDrawer;
	}
	
	public SlamWorldLayer getWorldLayer() {
		return worldLayer;
	}
	
	public void setWorldLayer(SlamWorldLayer worldLayer) {
		this.worldLayer = worldLayer;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		animationDrawer.updateTime(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		// Pour l'effet de fade in/out
		batch.setColor(getColor().r, getColor().g, getColor().b, parentAlpha * getColor().a);

		// Dessine la texture si elle est définie
		textureDrawer.draw(this, batch);

		// Dessine les animations (principale et temporaire)
		animationDrawer.draw(this, batch);

		// Dessine les particules
		particleDrawer.draw(this, batch);
	}
}
