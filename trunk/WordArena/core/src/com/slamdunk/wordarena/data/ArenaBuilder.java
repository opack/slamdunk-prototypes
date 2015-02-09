package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.enums.CellOwners;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.Assets;

/**
 * Construit une arène à partir d'un plan
 */
public class ArenaBuilder {
	private static final String LETTER_FROM_DECK = "-";
	private static final String ZONE_NONE = "0";
	private static final String CELL_SEPARATOR = " ";
	
	private WordSelectionHandler wordSelectionHandler;
	private Skin skin;
	
	private String[] types;
	private String[] letters;
	private int[] powers;
	private int[] owners;
	private String[] zones;
	
	private ArenaData arena;
	
	private KeyListMap<String, ArenaCell> cellsByZone;
	
	public ArenaBuilder(WordSelectionHandler wordSelectionHandler) {
		this(wordSelectionHandler, Assets.skin);
	}
	
	public ArenaBuilder(WordSelectionHandler wordSelectionHandler, Skin skin) {
		this.wordSelectionHandler = wordSelectionHandler;
		this.skin = skin;
		arena = new ArenaData();
	}
	
	public void setTypes(String[] types) {
		this.types = types;
	}

	public void setLetters(String[] letters) {
		this.letters = letters;
	}

	public void setPowers(int[] powers) {
		this.powers = powers;
	}

	public void setOwners(int[] owners) {
		this.owners = owners;
	}

	public void setZones(String[] zones) {
		this.zones = zones;
	}

	/**
	 * Crée des cellules pour constituer une arène ayant
	 * la structure correspondant au plan fourni.
	 * @param plan
	 * @return true si le plan a pu être chargé, false sinon
	 */
	public boolean load(TypedProperties plan) {
		arena.width = plan.getIntegerProperty("size.width", 0);
		arena.height = plan.getIntegerProperty("size.height", 0);
		if (arena.width == 0 || arena.height == 0) {
			return false;
		}
		
		// Charge les types de cellule
		String[] types = plan.getStringArrayProperty("plan.types", CELL_SEPARATOR);
		if (types == null) {
			return false;
		}
		setTypes(types);
		
		// Charge les lettres initiales.
		String[] letters = plan.getStringArrayProperty("plan.letters", CELL_SEPARATOR);
		if (letters == null) {
			return false;
		}
		setLetters(letters);
		
		// Charge les lettres initiales.
		int[] powers = plan.getIntegerArrayProperty("plan.powers", CELL_SEPARATOR);
		if (powers == null) {
			return false;
		}
		setPowers(powers);
		
		// Charge les lettres initiales.
		int[] owners = plan.getIntegerArrayProperty("plan.owners", CELL_SEPARATOR);
		if (owners == null) {
			return false;
		}
		setOwners(owners);
		
		// Charge les zones
		String[] zones = plan.getStringArrayProperty("plan.zones", CELL_SEPARATOR);
		if (zones == null) {
			return false;
		}
		setZones(zones);
		
		return true;
	}

	public ArenaData build() {
		// Génère des lettres en tenant compte de leur représentation
		arena.letterDeck = new Deck<Letters>(Letters.values(), 1);
		
		// Construction des cellules
		buildCells();
		
		// Construction des zones
		buildZones();

		return arena;
	}

	private void buildZones() {
		arena.zones = new ArrayList<ArenaZone>();
		for (Map.Entry<String, List<ArenaCell>> entry : cellsByZone.entrySet()) {
			ArenaZone zone = new ArenaZone();
			zone.id = entry.getKey();			
			for (ArenaCell cell : entry.getValue()) {
				zone.addCell(cell);
			}
			zone.update();
			arena.zones.add(zone);
		}
	}

	private void buildCells() {
		cellsByZone = new KeyListMap<String, ArenaCell>();
		arena.cells = new ArenaCell[arena.width][arena.height];
		ArenaCell cell;
		CellData data;
		int index;
		for (int y = arena.height - 1; y >= 0; y--) {
			for (int x = 0; x < arena.width; x++) {
				// Petite astuce pour calculer l'index car y=0 est en bas,
				// contrairement au fichier properties
				index = (arena.height - 1 - y) * arena.width + x;
				
				cell = new ArenaCell(skin, wordSelectionHandler);
				arena.cells[x][y] = cell;
				
				// Définition des données du modèle
				data = cell.getData();
				data.position.setXY(x, y);
				data.state = CellStates.NORMAL;
				
				data.type = CellTypes.valueOf(types[index]);
				data.letter = chooseLetter(data.type, letters[index]);
				data.power = choosePower(data.type, powers[index]);
				data.owner = chooseOwner(data.type, owners[index]);
				
				// Placement de la cellule dans le monde et mise à jour du display
				cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
				arena.cells[x][y].updateDisplay();
				
				// Regroupe les cellules par zone
				if (!ZONE_NONE.equals(zones[index])) {
					cellsByZone.putValue(zones[index], cell);
				}
			}
		}
	}

	private CellOwners chooseOwner(CellTypes cellType, int ownerIndex) {
		if (!cellType.canBeOwned()) {
			return CellOwners.NEUTRAL;
		}
		return CellOwners.values()[ownerIndex];
	}

	private int choosePower(CellTypes cellType, int power) {
		if (!cellType.hasPower()) {
			return 0;
		}
		return power;
	}

	/**
	 * Retourne la lettre indiquée, ou une lettre tirée dans le tas
	 * si letter = LETTER_FROM_DECK.
	 * @param letter
	 * @return
	 */
	private Letters chooseLetter(CellTypes cellType, String letter) {
		if (!cellType.hasLetter()) {
			return Letters.EMPTY;
		}
		if (cellType == CellTypes.J) {
			return Letters.JOKER;
		}
		if (LETTER_FROM_DECK.equals(letter)) {
			return arena.letterDeck.draw();
		}
		return Letters.valueOf(letter);
	}
}
