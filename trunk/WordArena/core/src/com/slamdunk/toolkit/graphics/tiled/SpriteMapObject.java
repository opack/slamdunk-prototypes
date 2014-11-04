package com.slamdunk.toolkit.graphics.tiled;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

/**
 * Un simple wrapper servant uniquement à permettre le dessin
 * d'un Sprite sur une TiledMap.
 * 
 * NOTE IMPORTANTE : La table de properties du MapObject N'EST PAS
 * mise à jour avec les attributs associés du Sprite (notamment
 * les coordonnées) lorsque celles-ci sont modifiées.
 * En revanche, certaines propriétés du Sprite associé (comme 
 * sa position) peuvent être modifiées depuis cette classe.
 * Il convient donc de ne plus manipuler directement le Sprite,
 * mais de passer uniquement par cette classe. Le Sprite sert
 * uniquement pour contenir la texture à dessiner ainsi que
 * pour les éventuelles transformations.
 */
public class SpriteMapObject extends MapObject {
	private Sprite sprite;
	private int tileSize;
	
	public SpriteMapObject(Sprite sprite, String name, int tileSize) {
		this.sprite = sprite;
		this.tileSize = tileSize;
		setName(name);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	/**
	 * Place l'objet en utilisant les coordonnées de la map,
	 * donc indiquées en tiles
	 * @param x
	 * @param y
	 */
	public void setMapPosition(float x, float y) {
		// Met à jour les attributs du Sprite
		sprite.setPosition(x, y);
		
		// Met à jour les propriétés du MapObject
		getProperties().put("x", (float)x * tileSize);
		getProperties().put("y", (float)y * tileSize);
	}
	
	/**
	 * Place l'objet en utilisants des coordonnées en pixels
	 * @param x
	 * @param y
	 */
	public void setPixelPosition(float x, float y) {
		// Met à jour les attributs du Sprite
		sprite.setPosition(x / tileSize, y / tileSize);
		
		// Met à jour les propriétés du MapObject
		getProperties().put("x", (float)x);
		getProperties().put("y", (float)y);
	}
}
