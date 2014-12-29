package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * 2 façons d'initialiser le renderer :
 *    - Soit en spécifiant un fichier de particules et un répertoire contenant les images
 *    grâce aux paramètres effectFile et imagesDirectory
 *    - Soit en passant directement un objet ParticleEffect
 * 
 * Particules créées avec l'éditeur (http://code.google.com/p/libgdx/wiki/ParticleEditor). 
 * Exporter les données de la particule (example: particle.p) et l'ajouter au projet Android 
 * dans le dossier Assets/Data (y placer aussi l'image utilisée dans la particule le cas
 * échéant). 
 */
public class ParticleRendererComponent extends Component {
	public String effectFile;
	public String imagesDirectory;
	
	public ParticleEffect particleEffect;
	
	private TransformComponent transform;
	
	@Override
	public void init() {
		transform = gameObject.getComponent(TransformComponent.class);
		if (effectFile != null
		&& !effectFile.isEmpty()
		&& imagesDirectory != null
		&& !imagesDirectory.isEmpty()) {
			particleEffect = new ParticleEffect();
			particleEffect.load(Gdx.files.internal(effectFile), Gdx.files.internal(imagesDirectory));
		}
		
		if (particleEffect == null) {
			throw new IllegalStateException("ParticleRendererComponent cannot work properly because no particle effect has been defined.");
		}
		particleEffect.start();
	}
	
	@Override
	public void update(float deltaTime) {
		particleEffect.update(deltaTime);
	}
	
	@Override
	public void render(Batch batch) {
		particleEffect.draw(batch);
	}
	
	@Override
	public void physics(float deltaTime) {
		particleEffect.setPosition(transform.worldPosition.x, transform.worldPosition.y);
	}
}
