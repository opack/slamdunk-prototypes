package com.slamdunk.toolkit.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Particules créées avec l'éditeur (http://code.google.com/p/libgdx/wiki/ParticleEditor). 
 * Exporter les données de la particule (example: particle.p) et l'ajouter au projet Android 
 * dans le dossier Assets/Data (y placer aussi l'image utilisée dans la particule le cas
 * échéant). 
 */
public class ParticleDrawer {
	private ParticleEffect particleEffect;
	private float particlePosX = 0.0f;
	private float particlePosY = 0.0f;
	private boolean isActive;
	
	public ParticleEffect getParticleEffect() {
		return particleEffect;
	}

	/**
	 * Définit la particule.
	 * @param centerPosition Permet de centrer la particule par rapport
	 * à la taille de l'acteur
	 * */
	public void setParticleEffect(Actor actor,
			ParticleEffect particleEffect,
			boolean isActive, boolean isStart,
			boolean centerPosition) {
		this.particleEffect = particleEffect;
		setActive(isActive);
		
		if (!centerPosition) {
			particleEffect.setPosition(actor.getX(), actor.getY());
		} else {
			particlePosX = actor.getWidth() / 2.0f;
			particlePosY = actor.getHeight() / 2.0f;
			particleEffect.setPosition(actor.getX() + particlePosX, actor.getY() + particlePosY);
		}

		if (isStart) {
			particleEffect.start();
		}
	}

	public void setParticlePositionXY(float x, float y) {
		particlePosX = x;
		particlePosY = y;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void draw(Actor actor, SpriteBatch batch) {
		if (isActive) {
			particleEffect.draw(batch, Gdx.graphics.getDeltaTime());
			particleEffect.setPosition(actor.getX() + particlePosX, actor.getY() + particlePosY);
		}
	}
}
