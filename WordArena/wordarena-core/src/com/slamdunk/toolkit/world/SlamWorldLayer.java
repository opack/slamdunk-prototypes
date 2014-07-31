package com.slamdunk.toolkit.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Une couche du monde. La couche peut contenir des acteurs
 * et possède des propriétés comme la transparence...
 */
public class SlamWorldLayer extends Group {
	
	/**
	 * Référence vers le monde auquel appartient cette couche
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
	
	public SlamWorld getWorld() {
		return world;
	}

	public void setBackgroundImage(TextureRegion textureBackground, Scaling scaling, boolean fillParent, boolean touchable) {
		Drawable tBg = new TextureRegionDrawable(textureBackground);
		background = new Image(tBg, scaling);
		background.setFillParent(fillParent);

		if (!touchable) {
			background.setTouchable(Touchable.disabled);
		}

		// On s'assure que l'image de fond est... au fond ;)
		addActorAt(0, background);
	}
	
	/**
	 * Ajoute un SlamActor à la couche et définit le lien dans la couche
	 * vers le SlamWorldLayer
	 * @param actor
	 */
	public void addActor(SlamActor actor) {
		super.addActor(actor);
		
		// Ajoute le lien entre l'acteur et la couche du monde
		actor.setWorldLayer(this);
	}
	
	/**
	 * Ajoute un SlamActor à la couche après un autre, et définit le lien dans la couche
	 * vers le SlamWorldLayer
	 * @param actor
	 */
	public void addActorAfter(Actor actorAfter, SlamActor actor) {
		super.addActorAfter(actorAfter, actor);
		
		// Ajoute le lien entre l'acteur et la couche du monde
		actor.setWorldLayer(this);
	}
	
	/**
	 * Ajoute un SlamActor à la couche à un z-index donné, et définit le lien dans la couche
	 * vers le SlamWorldLayer
	 * @param actor
	 */
	public void addActorAt(int index, SlamActor actor) {
		super.addActorAt(index, actor);
		
		// Ajoute le lien entre l'acteur et la couche du monde
		actor.setWorldLayer(this);
	}
	
	/**
	 * Ajoute un SlamActor à la couche avant un autre, et définit le lien dans la couche
	 * vers le SlamWorldLayer
	 * @param actor
	 */
	public void addActorBefore(Actor actorBefore, SlamActor actor) {
		super.addActorBefore(actorBefore, actor);
		
		// Ajoute le lien entre l'acteur et la couche du monde
		actor.setWorldLayer(this);
	}
}
