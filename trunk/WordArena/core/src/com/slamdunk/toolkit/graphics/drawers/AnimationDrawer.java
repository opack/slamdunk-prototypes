package com.slamdunk.toolkit.graphics.drawers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimationDrawer {
	/**
	 * Timer pour les animations
	 */
	private float stateTime = 0.0f;
	
	/**
	 * Animation permanente
	 */
	private Animation animation;
	private boolean isActive = false;
	
	/**
	 * Animation temporaire
	 */
	private Animation animationMomentary;
	private boolean isAnimationMomentaryActive = false;
	private boolean isAnimationMomentaryFinished = true;
	
	/**
	 * Variables de contrôle de l'animation
	 */
	private boolean isLooping = false;
	private boolean killAllAnimations = false;
	
	
	/**
	 * Indique si l'animation est active
	 * @return 
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Définit si l'animation est active. L'animation n'est dessinée
	 * que si elle est définie et active.
	 * 
	 * @param isActive
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	/**
	 * Indique si l'animation doit boucler
	 * @return 
	 */
	public boolean isLooping() {
		return isLooping;
	}

	/**
	 * Définit si l'animation boucle. Effectif uniquement
	 * si l'animation est définie et active.
	 * @param isAnimationLooping
	 */
	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	/**
	 * Retourne le state time
	 * @return state time (delta added)
	 */
	public float getStateTime() {
		return stateTime;
	}

	/**
	 * Définit le state time
	 */
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	/**
	 * Indique si killAllAnimation est activé
	 */
	public boolean isKillAllAnimations() {
		return killAllAnimations;
	}

	/**
	 * Définit si, une fois l'animation achevée, toutes les animations
	 * doivent cesser.
	 */
	public void setKillAllAnimations(boolean killAllAnimations) {
		this.killAllAnimations = killAllAnimations;
	}
	
	/**
	 * Dessine l'animation dans le batch indiqué, à la position
	 * de l'acteur
	 * @param actor
	 * @param batch
	 */
	public void draw(Actor actor, Batch batch) {
		// Dessine l'animation, uniquement si elle est active et non nulle
		if (isActive && animation != null) {
			// Get frame by frame and animate
			TextureRegion keyFrame = animation.getKeyFrame(stateTime, isLooping);

			// Draw it due to actors' settings
			batch.draw(keyFrame,
				actor.getX(), actor.getY(), 
				actor.getOriginX(), actor.getOriginY(),
				actor.getWidth(), actor.getHeight(), 
				actor.getScaleX(), actor.getScaleY(),
				actor.getRotation());

			if (animation.isAnimationFinished(stateTime)) {
				if (killAllAnimations) {
					isActive = false;
				}
			}
		}
		
		// Dessine l'animation ponctuelle, uniquement si elle est active et non nulle
		if (isAnimationMomentaryActive && animationMomentary != null) {
			if (animationMomentary.isAnimationFinished(stateTime)) {
				if (!killAllAnimations) {
					isActive = true;
					isAnimationMomentaryActive = false;
					isAnimationMomentaryFinished = true;
				} else {
					isActive = false;
					isAnimationMomentaryActive = false;
					isAnimationMomentaryFinished = true;
				}
			}

			if (isAnimationMomentaryActive) {
				// Get frame by frame and animate
				TextureRegion keyFrame = animationMomentary.getKeyFrame(
						stateTime, false);

				// Draw it due to actors' settings
				batch.draw(keyFrame,
					actor.getX(), actor.getY(), 
					actor.getOriginX(), actor.getOriginY(),
					actor.getWidth(), actor.getHeight(), 
					actor.getScaleX(), actor.getScaleY(),
					actor.getRotation());
			}
		}
	}

	/**
	 * Met à jour le compteur de temps
	 * @param delta
	 */
	public void updateTime(float delta) {
		stateTime += delta;
	}
	
	/**
	 * Retourne l'animation
	 * @return
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Définit l'animation courante, si elle est active et si elle doit être
	 * jouée en boucle.
	 * @param animation
	 * @param isActive
	 * @param isLooping
	 */
	public void setAnimation(Animation animation, boolean isActive, boolean isLooping) {
		this.animation = animation;
		this.isActive = isActive;
		this.isLooping = isLooping;
	}
	
	/**
	 * Retourne l'animation temporaire utilisée
	 * @return
	 */
	public Animation getAnimationMomentary() {
		return animationMomentary;
	}
	
	/**
	 * Définit une animation temporaire pour cet acteur.
	 * 
	 * <p>
	 * EXAMPLE<br>
	 * Actor has two animations idle and blinkeye. If you set the the momentary
	 * animation as blinkeye, actor will blink eye and it will go its' regular
	 * animation such as idle
	 * <p>
	 * "animationAfterMomentary" For instance, a bat flies and changes into
	 * dracula. Regular animation is flying bat, animation momentary is the
	 * smoke at moment of change, animationAfterMomentary is the dracula
	 * animation
	 * <p>
	 * "isAnimationMomentaryWaitingToBeCompleted" prevents the animation to be
	 * run again and again when this method clicked continusly, if its "true"
	 * this method wont be active until momentary animation completed
	 * <p>
	 * "killAllAnimations" is for ending animation, like a character dying
	 * animation, then no more animation will be running. It can be also used
	 * invisibility features
	 * 
	 * @param animationMomentary
	 *            set animation momentary
	 * @param isAnimationMomentaryActive
	 *            set animation momentary active to be drawn or not
	 * @param animationAfterMomentary
	 *            change regular animation after momentary animation completed
	 *            otherwise set null
	 * @param isAnimationMomentaryWaitingToBeCompleted
	 *            wait for to be completed, otherwise it will start again when
	 *            this method called
	 * @param killAllAnimations
	 *            do not run any animations after moementary animation completed
	 * */
	public void setAnimationMomentary(Animation animationMomentary,
			boolean isAnimationMomentaryActive,
			Animation animationAfterMomentary,
			boolean isAnimationMomentaryWaitingToBeCompleted,
			boolean killAllAnimations) {
		
		this.killAllAnimations = killAllAnimations;

		if (animationAfterMomentary != null) {
			animation = animationAfterMomentary;
		}

		if (!isAnimationMomentaryWaitingToBeCompleted) {
			this.animationMomentary = animationMomentary;
			animationMomentary.setPlayMode(Animation.PlayMode.NORMAL);
			this.isAnimationMomentaryActive = isAnimationMomentaryActive;

			if (isAnimationMomentaryActive) {
				stateTime = 0;
				isActive = false;
			}
		} else {
			if (isAnimationMomentaryFinished) {
				this.animationMomentary = animationMomentary;
				animationMomentary.setPlayMode(Animation.PlayMode.NORMAL);
				this.isAnimationMomentaryActive = isAnimationMomentaryActive;

				if (isAnimationMomentaryActive) {
					stateTime = 0;
					isActive = false;
				}

				isAnimationMomentaryFinished = false;
			}
		}
	}
}
