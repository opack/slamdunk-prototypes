package com.slamdunk.wordarena.data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.actors.CellSelectionListener;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;

/**
 * Construit une arène à partir d'un plan
 */
public class ArenaBuilderJson {
	public static final String ZONE_NONE = ArenaZone.NONE.getData().id;
	private static final String CELL_SEPARATOR = " ";
	
	private GameManager gameManager;
	private Array<Player> players;
	private Skin skin;
	
	private String[][] types;
	private String[][] letters;
	private int[][] powers;
	private int[][] owners;
	private String[][] zones;
	
	private ArenaData arena;
	
	private KeyListMap<String, ArenaCell> cellsByZone;
	private Set<Point> cellsWithWalls;
	
	public ArenaBuilderJson(GameManager gameManager) {
		this(gameManager, Assets.skin);
	}
	
	public ArenaBuilderJson(GameManager gameManager, Skin skin) {
		this.gameManager = gameManager;
		this.players = gameManager.getPlayers();
		this.skin = skin;
		arena = new ArenaData();
	}
	
	public void setTypes(String[][] types) {
		this.types = types;
	}

	public void setLetters(String[][] letters) {
		this.letters = letters;
	}

	public void setPowers(int[][] powers) {
		this.powers = powers;
	}

	public void setOwners(int[][] owners) {
		this.owners = owners;
	}

	public void setZones(String[][] zones) {
		this.zones = zones;
	}

	/**
	 * Crée des cellules pour constituer une arène ayant
	 * la structure correspondant au plan fourni.
	 * @param plan
	 * @return true si le plan a pu être chargé, false sinon
	 */
	public boolean load(JsonValue plan) {
		setSize(plan.getInt("width", 1), plan.getInt("height", 1));
		if (arena.width == 0 || arena.height == 0) {
			return false;
		}
		
		// Charge le nom de l'arène
		setName(Assets.i18nBundle.get("arena." + plan.getString("name")));
		
		// Charge les types de cellule
		setTypes(extractStringTable(plan.get("plan.types")));
		
		// Charge les lettres initiales.
		setLetters(extractStringTable(plan.get("plan.letters")));
		
		// Charge les puissances
		setPowers(extractIntTable(plan.get("plan.powers")));
		
		// Charge les possesseurs
		setOwners(extractIntTable(plan.get("plan.owners")));
		
		// Charge les zones
		setZones(extractStringTable(plan.get("plan.zones")));
		
		// Charge les murs
		// TODO
		
		return true;
	}
	
	public void setSize(int width, int height) {
		arena.width = width;
		arena.height = height;
	}

	public void setName(String name) {
		arena.name = name;
	}

	private String[][] extractStringTable(JsonValue jsonValue) {
		String[] values = jsonValue.asStringArray();
		String[] cols;
		String[][] table = new String[arena.width][arena.height];
		final int maxRow = arena.height - 1;
		for (int row = 0; row < arena.height; row++) {
			cols = values[row].split(CELL_SEPARATOR);
			for (int col = 0; col < arena.width; col++) {
				table[col][maxRow - row] = cols[col];
			}
		}
		return table;
	}
	
	private int[][] extractIntTable(JsonValue jsonValue) {
		String[] values = jsonValue.asStringArray();
		String[] cols;
		int[][] table = new int[arena.width][arena.height];
		final int maxRow = arena.height - 1;
		for (int row = 0; row < arena.height; row++) {
			cols = values[row].split(CELL_SEPARATOR);
			for (int col = 0; col < arena.width; col++) {
				table[col][maxRow - row] = Integer.parseInt(cols[col]);
			}
		}
		return table;
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
		arena.walls.clear();
		cellsWithWalls = new HashSet<Point>();
		
		// DBG
		addWall(arena.cells[0][0], arena.cells[0][1], true);
		addWall(arena.cells[2][0], arena.cells[3][0], true);
		addWall(arena.cells[2][0], arena.cells[2][1], true);
		
		// Recherche et crée les murs en coin
		createCornerWalls();
	}
	
	/**
	 * Recherche les murs formant des coins.
	 */
	private void createCornerWalls() {
		Point pos2 = new Point(0, 0);
		for (Point pos1 : cellsWithWalls) {
			// Mur avec la cellule du haut ?
			pos2.setXY(pos1.getX(), pos1.getY() + 1);
			boolean hasWallUp = hasWall(pos1, pos2);
			
			// Mur avec la cellule du bas ?
			pos2.setXY(pos1.getX(), pos1.getY() - 1);
			boolean hasWallDown = hasWall(pos1, pos2);
			
			// Mur avec la cellule de gauche ?
			pos2.setXY(pos1.getX() - 1, pos1.getY());
			boolean hasWallLeft = hasWall(pos1, pos2);
			
			// Mur avec la cellule de droite ?
			pos2.setXY(pos1.getX() + 1, pos1.getY());
			boolean hasWallRight = hasWall(pos1, pos2);
			
			// Cherche si les murs de cette cellule forment un coin
			boolean createWall = false;
			if (hasWallUp) {
				if (hasWallLeft) {
					pos2.setXY(pos1.getX() - 1, pos1.getY() + 1);
					createWall = true;
				} else if (hasWallRight) {
					pos2.setXY(pos1.getX() + 1, pos1.getY() + 1);
					createWall = true;
				}
			} else if (hasWallDown) {
				if (hasWallLeft) {
					pos2.setXY(pos1.getX() - 1, pos1.getY() - 1);
					createWall = true;
				} else if (hasWallRight) {
					pos2.setXY(pos1.getX() + 1, pos1.getY() - 1);
					createWall = true;
				}
			}
			// Crée le mur virtuel représentant le coin
			if (createWall) {
				addWall(pos1, pos2);
			}
		}
	}

	private boolean hasWall(Point pos1, Point pos2) {
		return isValidPos(pos2)
			&& arena.hasWall(arena.cells[pos1.getX()][pos1.getY()], arena.cells[pos2.getX()][pos2.getY()]);
	}

	private boolean isValidPos(Point pos) {
		return pos.getX() > -1
			&& pos.getX() < arena.width
			&& pos.getY() > -1
			&& pos.getY() < arena.height;
	}
	
	private void addWall(Point pos1, Point pos2) {
		addWall(arena.cells[pos1.getX()][pos1.getY()], arena.cells[pos2.getX()][pos2.getY()], false);
	}

	private void addWall(ArenaCell cell1, ArenaCell cell2, boolean trackCellsWithWalls) {
		// Crée le mur
		arena.walls.put(cell1, cell2, Boolean.TRUE);
		arena.walls.put(cell2, cell1, Boolean.TRUE);
		
		// Enregistre les cellules comme possédant des murs
		if (trackCellsWithWalls) {
			cellsWithWalls.add(cell1.getData().position);
			cellsWithWalls.add(cell2.getData().position);
		}
	}

	private void buildZones() {
		arena.zones.clear();
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
		for (int y = arena.height - 1; y >= 0; y--) {
			for (int x = 0; x < arena.width; x++) {
				cell = new ArenaCell(skin);
				cell.addListener(new CellSelectionListener(cell, wordSelectionHandler));
				arena.cells[x][y] = cell;
				
				// Définition des données du modèle
				data = cell.getData();
				data.position.setXY(x, y);
				data.state = CellStates.OWNED;
				
				data.type = CellTypes.valueOf(types[x][y]);
				data.planLetter = letters[x][y];
				data.letter = chooseLetter(data.type, letters[x][y], arena.letterDeck);
				data.power = choosePower(data.type, powers[x][y]);
				data.owner = chooseOwner(data.type, owners[x][y]);
				
				// Placement de la cellule dans le monde et mise à jour du display
				cell.setPosition(x * cell.getWidth(), y * cell.getHeight());
				cell.updateDisplay();
				
				// Regroupe les cellules par zone
				if (!ZONE_NONE.equals(zones[x][y])) {
					cellsByZone.putValue(zones[x][y], cell);
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
		if (Letters.FROM_DECK.label.equals(letter)) {
			return letterDeck.draw();
		}
		return Letters.valueOf(letter);
	}
}
