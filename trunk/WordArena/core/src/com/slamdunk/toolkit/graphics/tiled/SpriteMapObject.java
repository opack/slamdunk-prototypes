package com.slamdunk.toolkit.graphics.tiled;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

/**
 * Un simple wrapper servant uniquement � permettre le dessin
 * d'un Sprite sur une TiledMap.
 * 
 * NOTE IMPORTANTE : La table de properties du MapObject N'EST PAS
 * mise � jour avec les attributs associ�s du Sprite (notamment
 * les coordonn�es) lorsque celles-ci sont modifi�es.
 * En revanche, certaines propri�t�s du Sprite associ� (comme 
 * sa position) peuvent �tre modifi�es depuis cette classe.
 * Il convient donc de ne plus manipuler directement le Sprite,
 * mais de passer uniquement par cette classe. Le Sprite sert
 * uniquement pour contenir la texture � dessiner ainsi que
 * pour les �ventuelles transformations.
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
	 * Place l'objet en utilisant les coordonn�es de la map,
	 * donc indiqu�es en tiles
	 * @param x
	 * @param y
	 */
	public void setMapPosition(float x, float y) {
		// Met � jour les attributs du Sprite
		sprite.setPosition(x, y);
		
		// Met � jour les propri�t�s du MapObject
		getProperties().put("x", (float)x * tileSize);
		getProperties().put("y", (float)y * tileSize);
	}
	
	/**
	 * Place l'objet en utilisants des coordonn�es en pixels
	 * @param x
	 * @param y
	 */
	public void setPixelPosition(float x, float y) {
		// Met � jour les attributs du Sprite
		sprite.setPosition(x / tileSize, y / tileSize);
		
		// Met � jour les propri�t�s du MapObject
		getProperties().put("x", (float)x);
		getProperties().put("y", (float)y);
	}
}
