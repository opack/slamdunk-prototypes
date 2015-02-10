package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;

public class Assets {
	public static Skin skin;
	public static Map<CellOwners, LabelStyle> ownerStyles;
	
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static DoubleEntryArray<CellOwners, CellStates, TextureRegionDrawable> cells;
	public static Map<CellOwners, Texture> edges;
	
	public static void load () {
		loadSkin();
		loadCells();
		loadEdges();
		background = loadTexture("textures/background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
	}
	
	private static void loadSkin() {
		skin = new Skin(Gdx.files.internal("skins/wordarena/uiskin.json"));
		ownerStyles = new HashMap<CellOwners, LabelStyle>();
		for (CellOwners owner : CellOwners.values()) {
			ownerStyles.put(owner, skin.get(owner.name(), LabelStyle.class));
		}
	}

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadCells() {
		cells = new DoubleEntryArray<CellOwners, CellStates, TextureRegionDrawable>();
		final TextureRegion[][] textures = splitSpriteSheet(
			"textures/cells4.png",
			CellStates.values().length,
			CellOwners.values().length);
		TextureRegion region;
		for (CellStates state : CellStates.values()) {
			for (CellOwners owner : CellOwners.values()) {
				region = textures[state.ordinal()][owner.ordinal()];
				cells.put(owner, state, new TextureRegionDrawable(region));
			}
		}
	}
	
	private static void loadEdges() {
		edges = new HashMap<CellOwners, Texture>();

	    Texture neutral = new Texture(Gdx.files.internal("textures/edge_neutral.png"));
	    neutral.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		neutral.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(CellOwners.NEUTRAL, neutral);
	    
	    Texture player1 = new Texture(Gdx.files.internal("textures/edge_player1.png"));
	    player1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    player1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(CellOwners.PLAYER1, player1);
	    
	    Texture player2 = new Texture(Gdx.files.internal("textures/edge_player2.png"));
		player2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player2.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(CellOwners.PLAYER2, player2);
	    
	    Texture player3 = new Texture(Gdx.files.internal("textures/edge_player3.png"));
		player3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player3.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(CellOwners.PLAYER3, player3);
	    
	    Texture player4 = new Texture(Gdx.files.internal("textures/edge_player4.png"));
		player4.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player4.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(CellOwners.PLAYER4, player4);
	}
	
	private static TextureRegion[][] splitSpriteSheet(String file, int nbRows, int nbCols) {
		Texture spriteSheet = new Texture(Gdx.files.internal(file));
		return TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / nbCols,
				spriteSheet.getHeight() / nbRows);
	}
}
