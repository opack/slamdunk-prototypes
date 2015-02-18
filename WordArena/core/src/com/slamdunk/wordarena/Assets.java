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
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.enums.CellStates;
import com.uwsoft.editor.renderer.resources.ResourceManager;

public class Assets {
	public static ResourceManager overlap2dResourceManager;
	public static Skin skin;
	public static Map<Owners, LabelStyle> ownerStyles;
	public static DoubleEntryArray<Owners, CellStates, TextureRegionDrawable> cells;
	public static Map<Owners, Texture> edges;
	
	public static void load () {
		loadResourceManager();
		loadSkin();
		loadCells();
		loadEdges();
	}
	
	public static void dispose () {
		disposeResourceManager();
		disposeSkin();
		disposeEdges();
	}
	
	private static void loadResourceManager() {
		overlap2dResourceManager = new ResourceManager();
		overlap2dResourceManager.initAllResources();
	}
	
	private static void disposeResourceManager() {
		overlap2dResourceManager.dispose();
	}

	private static void loadSkin() {
		skin = new Skin(Gdx.files.internal("skins/wordarena/uiskin.json"));
		ownerStyles = new HashMap<Owners, LabelStyle>();
		for (Owners owner : Owners.values()) {
			ownerStyles.put(owner, skin.get(owner.name(), LabelStyle.class));
		}
	}
	
	private static void disposeSkin() {
		skin.dispose();
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
		cells = new DoubleEntryArray<Owners, CellStates, TextureRegionDrawable>();
		final TextureRegion[][] textures = splitSpriteSheet(
			"textures/cells4.png",
			CellStates.values().length,
			Owners.values().length);
		TextureRegion region;
		for (CellStates state : CellStates.values()) {
			for (Owners owner : Owners.values()) {
				region = textures[state.ordinal()][owner.ordinal()];
				cells.put(owner, state, new TextureRegionDrawable(region));
			}
		}
	}
	
	private static void loadEdges() {
		edges = new HashMap<Owners, Texture>();

	    Texture neutral = new Texture(Gdx.files.internal("textures/edge_neutral.png"));
	    neutral.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		neutral.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(Owners.NEUTRAL, neutral);
	    
	    Texture player1 = new Texture(Gdx.files.internal("textures/edge_player1.png"));
	    player1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    player1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(Owners.PLAYER1, player1);
	    
	    Texture player2 = new Texture(Gdx.files.internal("textures/edge_player2.png"));
		player2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player2.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(Owners.PLAYER2, player2);
	    
	    Texture player3 = new Texture(Gdx.files.internal("textures/edge_player3.png"));
		player3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player3.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(Owners.PLAYER3, player3);
	    
	    Texture player4 = new Texture(Gdx.files.internal("textures/edge_player4.png"));
		player4.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		player4.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    edges.put(Owners.PLAYER4, player4);
	}
	
	private static void disposeEdges() {
		for (Texture texture : edges.values()) {
			texture.dispose();
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
