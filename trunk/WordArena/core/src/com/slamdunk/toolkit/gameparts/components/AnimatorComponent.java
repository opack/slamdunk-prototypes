package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;

/**
 * Change l'image d'un SpriteRendererComponent en fonction d'une animation.
 * Nécessite que le GameObject contienne un SpriteRendererComponent.
 */
public class AnimatorComponent extends Component {
	public String spriteSheet;
	
	public int nbCols;
	
	public int nbRows;
	
	/**
	 * Si spécifié, seules les frames aux indices indiqués
	 * seront utilisées pour l'animation
	 */
	public int[] useFrames;
	
	public float frameDuration;
	
	public float stateTime;
	
	public PlayMode playMode;
	
	private SpriteRendererComponent spriteRenderer;
	
	private Animation animation;
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(SpriteRendererComponent.class)) {
			gameObject.addComponent(SpriteRendererComponent.class);
		}
	}
	
	@Override
	public void reset() {
		spriteSheet = null;
		nbCols = 0;
		nbRows = 0;
		
		useFrames = null;
		frameDuration = 0;
		stateTime = 0;
		playMode = PlayMode.NORMAL;
	}
	
	@Override
	public void init() {
		spriteRenderer = gameObject.getComponent(SpriteRendererComponent.class);
		if (spriteRenderer == null) {
			throw new IllegalStateException("Missing SpriteRendererComponent component in the GameObject. The AnimatorComponent component cannot work properly.");
		}
		
		if (spriteSheet != null
		&& !spriteSheet.isEmpty()) {
			if (useFrames != null) {
				animation = AnimationCreator.create(spriteSheet, nbCols, nbRows, frameDuration, playMode, useFrames);
			} else {
				animation = AnimationCreator.create(spriteSheet, nbCols, nbRows, frameDuration);
				animation.setPlayMode(playMode);
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		stateTime += deltaTime;
		spriteRenderer.textureRegion = animation.getKeyFrame(stateTime);
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
}
