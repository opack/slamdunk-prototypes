package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.enums.Borders;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Letters;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static DoubleEntryArray<Letters, CellStates, TextureRegionDrawable> letters;
	public static Map<CellStates, TextureRegionDrawable> cells;
	public static DoubleEntryArray<Borders, CellOwners, TextureRegionDrawable> borders;
	
	public static Texture edge;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		loadLetters();
		loadBorders();
		loadCells();
		background = loadTexture("textures/background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
		
		edge = new Texture(Gdx.files.internal("textures/edge.png"));
		edge.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        edge.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadCells() {
		cells = new HashMap<CellStates, TextureRegionDrawable>();
		final TextureRegion[][] textures = splitSpriteSheet("textures/cells.png", 1, CellStates.values().length);
		TextureRegion region;
		for (CellStates state : CellStates.values()) {
			region = textures[0][state.ordinal()];
			cells.put(state, new TextureRegionDrawable(region));
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
		borders = new DoubleEntryArray<Borders, CellOwners, TextureRegionDrawable>();
		
		final TextureRegion[][] verticalTextures = splitSpriteSheet("textures/borders_vertical.png", 1, CellOwners.values().length);
		for (CellOwners zone : CellOwners.values()) {
			TextureRegion leftBorderRegion = verticalTextures[0][zone.ordinal()];
			borders.put(Borders.LEFT, zone, new TextureRegionDrawable(leftBorderRegion));
			
			TextureRegion rightBorderRegion = new TextureRegion(leftBorderRegion);
			rightBorderRegion.flip(true, false);
			borders.put(Borders.RIGHT, zone, new TextureRegionDrawable(rightBorderRegion));
		}
		
		final TextureRegion[][] horizontalTextures = splitSpriteSheet("textures/borders_horizontal.png", CellOwners.values().length, 1);
		for (CellOwners zone : CellOwners.values()) {
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
