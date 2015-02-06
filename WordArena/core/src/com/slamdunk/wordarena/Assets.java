package com.slamdunk.wordarena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.enums.Zones;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static DoubleEntryArray<Letters, CellStates, TextureRegionDrawable> letters;
	public static DoubleEntryArray<Borders, Zones, TextureRegionDrawable> borders;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		loadLetters();
		loadBorders();
		background = loadTexture("textures/background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
	}

	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadLetters() {
		letters = new DoubleEntryArray<Letters, CellStates, TextureRegionDrawable>();
		final TextureRegion[][] textures = splitSpriteSheet("textures/letters.png", CellStates.values().length, Letters.values().length);
		TextureRegion region;
		for (CellStates state : CellStates.values()) {
			for (Letters letter : Letters.values()) {
				region = textures[state.ordinal()][letter.ordinal()];
				letters.put(letter, state, new TextureRegionDrawable(region));
			}
		}
	}
	
	private static void loadBorders() {
		borders = new DoubleEntryArray<Borders, Zones, TextureRegionDrawable>();
		
		final TextureRegion[][] verticalTextures = splitSpriteSheet("textures/borders_vertical.png", 1, Zones.values().length);
		for (Zones zone : Zones.values()) {
			TextureRegion leftBorderRegion = verticalTextures[0][zone.ordinal()];
			borders.put(Borders.LEFT, zone, new TextureRegionDrawable(leftBorderRegion));
			
			TextureRegion rightBorderRegion = new TextureRegion(leftBorderRegion);
			rightBorderRegion.flip(true, false);
			borders.put(Borders.RIGHT, zone, new TextureRegionDrawable(rightBorderRegion));
		}
		
		final TextureRegion[][] horizontalTextures = splitSpriteSheet("textures/borders_horizontal.png", Zones.values().length, 1);
		for (Zones zone : Zones.values()) {
			TextureRegion topBorderRegion = horizontalTextures[zone.ordinal()][0];
			borders.put(Borders.TOP, zone, new TextureRegionDrawable(topBorderRegion));
			
			TextureRegion bottomBorderRegion = new TextureRegion(topBorderRegion);
			bottomBorderRegion.flip(false, true);
			borders.put(Borders.BOTTOM, zone, new TextureRegionDrawable(bottomBorderRegion));
		}
	}
	
	private static TextureRegion[][] splitSpriteSheet(String file, int nbRows, int nbCols) {
		Texture spriteSheet = new Texture(Gdx.files.internal(file));
		return TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / nbCols,
				spriteSheet.getHeight() / nbRows);
	}
}
