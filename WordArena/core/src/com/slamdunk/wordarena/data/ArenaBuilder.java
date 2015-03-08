package com.slamdunk.wordarena.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.toolkit.lang.TypedProperties;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaWall;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

/**
 * Construit une arène à partir d'un plan
 */
public class ArenaBuilder {
	public static final String LETTER_FROM_DECK = "-";
	private static final String ZONE_NONE = "-";
	private static final String CELL_SEPARATOR = " ";
	
	private GameManager gameManager;
	private List<Player> players;
	private Skin skin;
	
	private String[] types;
	private String[] letters;
	private int[] powers;
	private int[] owners;
	private String[] zones;
	
	private ArenaData arena;
	
	private KeyListMap<String, ArenaCell> cellsByZone;
	
	public ArenaBuilder(GameManager gameManager) {
		this(gameManager, Assets.skin);
	}
	
	public ArenaBuilder(GameManager gameManager, Skin skin) {
		this.gameManager = gameManager;
		this.players = gameManager.getPlayers();
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
		
		// Charge le nom de l'arène
		arena.name = Assets.i18nBundle.get("arena." + plan.getIntegerProperty("index", -1));
		
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
		
		// Construction des murs
		buildWalls();
		
		// Construction des zones
		buildZones();
		
		return arena;
	}

	private void buildWalls() {
		arena.walls = new DoubleEntryArray<ArenaCell, ArenaCell, ArenaWall>();
		
		// DBG
		ArenaCell cell1 = arena.cells[0][0];
		ArenaCell cell2 = arena.cells[0][1];
		ArenaWall wall = new ArenaWall(cell1, cell2);
		arena.walls.put(cell1, cell2, wall);
		arena.walls.put(cell2, cell1, wall);
	}

	private void buildZones() {
		arena.zones = new ArrayList<ArenaZone>();
		for (Map.Entry<String, List<ArenaCell>> entry : cellsByZone.entrySet()) {
			ArenaZone zone = new ArenaZone(gameManager, entry.getKey());
			for (ArenaCell cell : entry.getValue()) {
				zone.addCell(cell);
			}
			zone.update();
			arena.zones.add(zone);
		}
	}

	private void buildCells() {
		final WordSelectionHandler wordSelectionHandler = gameManager.getWordSelectionHandler();
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
				data.state = CellStates.OWNED;
				
				data.type = CellTypes.valueOf(types[index]);
				data.planLetter = letters[index];
				data.letter = chooseLetter(data.type, letters[index], arena.letterDeck);
				data.power = choosePower(data.type, powers[index]);
				data.owner = chooseOwner(data.type, owners[index]);
				
				// Placement de la cellule dans le monde et mise à jour du display
				cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
				cell.updateDisplay();
				
				// Regroupe les cellules par zone
				if (!ZONE_NONE.equals(zones[index])) {
					cellsByZone.putValue(zones[index], cell);
				}
			}
		}
	}

	private Player chooseOwner(CellTypes cellType, int ownerIndex) {
		if (!cellType.canBeOwned()
		|| ownerIndex == 0) {
			return Player.NEUTRAL;
		}
		// Dans le plan, l'owner comence à 1
		return players.get(ownerIndex - 1);
	}

	private static int choosePower(CellTypes cellType, int power) {
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
	public static Letters chooseLetter(CellTypes cellType, String letter, Deck<Letters> letterDeck) {
		if (!cellType.hasLetter()) {
			return Letters.EMPTY;
		}
		if (cellType == CellTypes.J) {
			return Letters.JOKER;
		}
		if (LETTER_FROM_DECK.equals(letter)) {
			return letterDeck.draw();
		}
		return Letters.valueOf(letter);
	}
}
