package com.slamdunk.wordarena;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.data.CellData;
import com.slamdunk.wordarena.data.CellPack;
import com.slamdunk.wordarena.enums.CellStates;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.MySkin;

public class Assets {
	private static final String CELL_PACK_PREFIX = "cellpack";
	public static final String CELL_PACK_NEUTRAL = "neutral";
	
	public static TypedProperties appProperties;
	public static I18NBundle i18nBundle;
	public static ResourceManager overlap2dResourceManager;
	public static Skin skin;
	/**
	 * Overlap2D ne charge pas les skins. On doit donc surcharger la méthode getSkin() du ResourceManager
	 * pour retourner une skin qu'on aura chargé nous même afin d'avoir la possibilité d'utiliser les
	 * widgets de l'UI standard de LibGDX.
	 * On crée donc un MySkin et on y met notre skin.
	 */
	private static MySkin specialSkinForOverlap;
	public static TextureAtlas atlas;
	public static Map<String, CellPack> cellPacks;
	public static Texture edge;
	public static Texture wall;
	
	public static void load () {
		loadAppProperties();
		loadI18N();
		loadOverlapResources();
		loadSkin();
		loadAtlas();
		loadTextures();
	}
	
	public static void dispose () {
		disposeTextures();
		disposeAtlas();
		disposeSkin();
		disposeOverlapResources();
	}
	
	private static void loadAppProperties() {
		appProperties = new TypedProperties("wordarena.properties");
	}
	
	public static void loadTextures() {
		edge = new Texture("textures/zone_edge.png");
		edge.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		edge.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		wall = new Texture("textures/wall.png");
		wall.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		wall.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	public static void disposeTextures() {
		edge.dispose();
		wall.dispose();
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
		specialSkinForOverlap = new MySkin(Gdx.files.internal("skins/wordarena/uiskin.json"));
		overlap2dResourceManager = new ResourceManager() {
			@Override
			public MySkin getSkin() {
				return specialSkinForOverlap;
			}
		};
		overlap2dResourceManager.initAllResources();
	}
	
	private static void disposeOverlapResources() {
		overlap2dResourceManager.dispose();
	}

	private static void loadSkin() {
		skin = specialSkinForOverlap;
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
			
			// Charge le style de label
			pack.labelStyle = skin.get(CELL_PACK_PREFIX + "_" + packName, LabelStyle.class);
			
			// Enregistre le pack
			cellPacks.put(packName, pack);
		}
	}
	
	private static void putCellPackImage(final CellPack pack, final CellStates state, Boolean selected) {
		final String regionName = formatCellPackCellRegionName(pack.name, state, selected);
		final TextureRegion region = atlas.findRegion(regionName);
		fixBleeding(region);
		pack.cell.put(state, selected, new TextureRegionDrawable(region));
	}
	
	/**
	 * Permet de corriger le texture bleeding en décalant les coordonnées de la région d'un demi-pixel.
	 * Cette méthode vient de http://www.wendytech.de/2012/08/fixing-bleeding-in-libgdxs-textureatlas/.
	 * @param region
	 */
	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
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
	
	private static void disposeAtlas() {
		atlas.dispose();
	}

	/**
	 * Retourne l'image de cellule pour le pack et l'état de la cellule indiqués.
	 * @param data
	 * @param selected
	 * @return
	 */
	public static TextureRegionDrawable getCellImage(CellData data) {
		return cellPacks.get(data.owner.cellPack).cell.get(data.state, data.selected);
	}
	
	/**
	 * Retourne le LabelStyle pour le pack indiqué.
	 * @param pack
	 * @return
	 */
	public static LabelStyle getLabelStyle(String pack) {
		final String cellPack = pack != null ? pack : CELL_PACK_NEUTRAL;
		return cellPacks.get(cellPack).labelStyle;
	}
}
