package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.data.CellPack;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.Owners;
import com.uwsoft.editor.renderer.resources.ResourceManager;

public class Assets {
	private static final String CELL_PACK_PREFIX = "cellpack";
	
	public static TypedProperties appProperties;
	public static I18NBundle i18nBundle;
	public static ResourceManager overlap2dResourceManager;
	public static Skin skin;
	public static TextureAtlas atlas;
	public static Map<Owners, LabelStyle> ownerStyles;
	public static Map<String, CellPack> cellPacks;
	
	public static void load () {
		loadAppProperties();
		loadI18N();
		loadOverlapResources();
		loadSkin();
		loadAtlas();
	}
	
	public static void dispose () {
		disposeOverlapResources();
		disposeSkin();
		disposeAtlas();
	}
	
	private static void loadAppProperties() {
		appProperties = new TypedProperties("wordarena.properties");
	}
	
	public static void loadAtlas() {
		atlas = new TextureAtlas("textures/wordarena.txt");
		
		// Charge les cell-packs
		loadCellPacks();
	}
	
	public static void loadI18N() {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/WordArena");
		Locale locale = new Locale(SlamSettings.LANGUAGE.get());
		i18nBundle = I18NBundle.createBundle(baseFileHandle, locale);
	}
	
	private static void loadOverlapResources() {
		overlap2dResourceManager = new ResourceManager();
		overlap2dResourceManager.initAllResources();
	}
	
	private static void disposeOverlapResources() {
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
	
	private static void loadCellPacks() {
		// Charge la liste des cell-packs
		final String[] packList = appProperties.getStringArrayProperty("cellpacks", ",");
		
		// Charge les cellules et les bords
		cellPacks = new HashMap<String, CellPack>();
		
		CellPack pack;
		for (String packName : packList) {
			// Crée le pack
			pack = new CellPack();
			pack.name = packName;
			
			// Charge les images des cellules
			putCellPackImage(pack, CellStates.OWNED, Boolean.FALSE);
			putCellPackImage(pack, CellStates.OWNED, Boolean.TRUE);
			putCellPackImage(pack, CellStates.CONTROLED, Boolean.FALSE);
			putCellPackImage(pack, CellStates.CONTROLED, Boolean.TRUE);
			
			// Charge l'image de la bordure
			final TextureRegion region = atlas.findRegion(formatCellPackEdgeRegionName(pack.name));
			pack.edge = region.getTexture();
			
			// Enregistre le pack
			cellPacks.put(packName, pack);
		}
	}
	
	private static void putCellPackImage(final CellPack pack, final CellStates state, Boolean selected) {
		final TextureRegion region = atlas.findRegion(formatCellPackCellRegionName(pack.name, state, selected));
		pack.cell.put(state, selected, new TextureRegionDrawable(region));
	}

	/**
	 * Retourne le nom d'une région d'une cellule en fonction du nom du pack et de l'état de la
	 * cellule pour lequel on souhaite récupérer l'image du pack dans l'atlas
	 * @param pack
	 * @param state
	 * @param selected
	 * @return
	 */
	private static String formatCellPackCellRegionName(String pack, CellStates state, boolean selected) {
		return CELL_PACK_PREFIX + "_"
			+ pack + "_"
			+ state.name().toLowerCase() + "_"
			+ (selected ? "selected" : "normal");
	}
	
	/**
	 * Retourne le nom d'une région d'une bordure en fonction du nom du pack
	 * @param pack
	 * @param state
	 * @param selected
	 * @return
	 */
	private static String formatCellPackEdgeRegionName(String pack) {
		return CELL_PACK_PREFIX + "_" + pack + "_edge";
	}
	
	private static void disposeAtlas() {
		atlas.dispose();
	}
}
