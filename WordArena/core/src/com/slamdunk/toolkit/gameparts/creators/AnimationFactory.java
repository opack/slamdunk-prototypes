package com.slamdunk.toolkit.gameparts.creators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Classe simplifiant la création d'une animation à partir d'une spritesheet
 * @author Didier
 *
 */
public class AnimationFactory {
	
	/**
	 * Crée une animation en utilisant les régions nommées regionName dans l'atlas
	 * indiqué
	 * @param atlas
	 * @param regionName
	 * @param frameDuration
	 * @return
	 */
	public static Animation create(TextureAtlas atlas, String regionName, float frameDuration) {
		Array<AtlasRegion> regions = atlas.findRegions(regionName);
		if (regions == null) {
			return null;
		}
		TextureRegion[] frames = new TextureRegion[regions.size];
		for (int index = 0; index < regions.size; index++) {
			frames[index] = regions.get(index);
		}
		return new Animation(frameDuration, frames);
	}
	
	/**
	 * Crée une animation en utilisant la spriteSheet indiquée
	 * @param spritesFile
	 * @param frameCols
	 * @param frameRows
	 * @param frameDuration
	 * @return
	 */
	public static Animation create(String spritesFile, int frameCols, int frameRows, float frameDuration) {
		Texture spriteSheet = new Texture(Gdx.files.internal(spritesFile));
		TextureRegion[][] tmp = TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / frameCols,
				spriteSheet.getHeight() / frameRows);
		TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
		int index = 0;
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return new Animation(frameDuration, frames);
	}
	
	public static Animation create(String spritesFile, int frameCols, int frameRows, float frameDuration, int... spriteIndexes) {
		return create(spritesFile, frameCols, frameRows, frameDuration, null, spriteIndexes);
	}
	
	/**
	 * Crée une animation en utilisant une partie de la spriteSheet indiquée. Seuls les
	 * sprites aux indices spécifiés par indexes sont utilisés.
	 * @param spritesFile
	 * @param frameCols
	 * @param frameRows
	 * @param frameDuration
	 * @param spriteIndexes Index 0 pour le sprite en haut à gauche, incrémenté de gauche
	 * à droite et de haut en bas
	 * @return
	 */
	public static Animation create(String spritesFile, int frameCols, int frameRows, float frameDuration, PlayMode playMode, int... spriteIndexes) {
		Texture spriteSheet = new Texture(Gdx.files.internal(spritesFile));
		TextureRegion[][] tmp = TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / frameCols,
				spriteSheet.getHeight() / frameRows);
		TextureRegion[] frames = new TextureRegion[spriteIndexes.length];
		int index = 0;
		for (int spriteIndex : spriteIndexes) {
			frames[index++] = tmp[spriteIndex / frameCols][spriteIndex % frameCols];
		}
		
		Animation animation = new Animation(frameDuration, frames);
		if (playMode != null) {
			animation.setPlayMode(playMode);
		}
		return animation;
	}
}
