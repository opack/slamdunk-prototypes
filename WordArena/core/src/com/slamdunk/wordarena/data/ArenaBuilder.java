package com.slamdunk.wordarena.data;

import java.util.ArrayList;
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
import com.slamdunk.wordarena.actors.ApplyToolListener;
import com.slamdunk.wordarena.actors.ArenaCell;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.actors.CellSelectionListener;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

/**
 * Construit une arène à partir d'un plan
 */
public class ArenaBuilder {
	public static final String ZONE_NONE = ArenaZone.NONE.getData().id;
	
	/**
	 * Caractère qui sépare l'info de 2 cellules dans le Json
	 */
	private static final String CELL_SEPARATOR = " ";
	
	/**
	 * Caractère qui sépare l'x et l'y d'un point dans le Json
	 */
	private static final String POSITION_SEPARATOR = ";";
	
	private GameManager gameManager;
	private Array<Player> players;
	private Skin skin;
	/**
	 * Indique si on est en train de construire une arène pour l'éditeur
	 * ou pour jouer.
	 */
	private EditorScreen editorScreen;
	
	private String[][] types;
	private String[][] letters;
	private int[][] powers;
	private int[][] owners;
	private String[][] zones;
	private List<Point> walls;
	
	private ArenaData arena;
	
	private KeyListMap<String, ArenaCell> cellsByZone;
	private Set<Point> cellsWithWalls;
	
	public ArenaBuilder(GameManager gameManager) {
		this(gameManager, Assets.skin);
	}
	
	public ArenaBuilder(GameManager gameManager, Skin skin) {
		this.gameManager = gameManager;
		this.players = gameManager.getPlayers();
		this.skin = skin;
		arena = new ArenaData();
	}
	
	public EditorScreen getEditorScreen() {
		return editorScreen;
	}

	public void setEditorScreen(EditorScreen editorScreen) {
		this.editorScreen = editorScreen;
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
	
	public void setWalls(List<Point> walls) {
		this.walls = walls;
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
		setName(plan.getString("name"));
		
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
		
		// TODO Charge les murs
		setWalls(extractPointList(plan.get("plan.walls")));
		
		return true;
	}
	
	private List<Point> extractPointList(JsonValue jsonValue) {
		List<Point> walls = new ArrayList<Point>();
		if (jsonValue != null) {
			String[] cellsPos;
			String[] cellPos;
			int x;
			int y;
			
			for (String wallCells : jsonValue.asStringArray()) {
				cellsPos = wallCells.split(CELL_SEPARATOR);
				
				cellPos = cellsPos[0].split(POSITION_SEPARATOR);
				x = Integer.parseInt(cellPos[0]);
				y = Integer.parseInt(cellPos[1]);			
				walls.add(new Point(x, y));
				
				cellPos = cellsPos[1].split(POSITION_SEPARATOR);
				x = Integer.parseInt(cellPos[0]);
				y = Integer.parseInt(cellPos[1]);			
				walls.add(new Point(x, y));
			}
		}
		return walls;
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
		if (editorScreen == null) {
			// Génère des lettres en tenant compte de leur représentation
			arena.letterDeck = new Deck<Letters>(Letters.values(), 1);
		}
		
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
		
		// Crée les murs
		Point pos1;
		Point pos2;
		for (int curPos = 0; curPos < walls.size(); curPos += 2) {
			pos1 = walls.get(curPos);
			pos2 = walls.get(curPos + 1);
			
			// Ajoute le mur
			addWall(pos1, pos2);
			
			// Enregistre les cellules comme possédant des murs
			cellsWithWalls.add(pos1);
			cellsWithWalls.add(pos2);
		}
		
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

	private void addWall(Point pos1, Point pos2) {
		arena.addWall(arena.cells[pos1.getX()][pos1.getY()], arena.cells[pos2.getX()][pos2.getY()]);
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
				if (editorScreen != null) {
					cell.addListener(new ApplyToolListener(editorScreen, cell));
				} else {
					cell.addListener(new CellSelectionListener(cell, wordSelectionHandler));
				}
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
		// Dans le plan, l'owner comence à 1. C'est bien le premier joueur
		// en mode jeu, mais pas en mode édition
		if (editorScreen == null) {
			return players.get(ownerIndex - 1);
		} else {
			return players.get(ownerIndex);
		}
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
	 * @param planLetter
	 * @return
	 */
	public static Letters chooseLetter(CellTypes cellType, String planLetter, Deck<Letters> letterDeck) {
		if (!cellType.hasLetter()) {
			return Letters.EMPTY;
		}

		// Récupère la lettre correspondant au symbole
		Letters letter = Letters.getFromLabel(planLetter);
		
		// Si c'est le symbole de pioche, on renvoie une lettre piochée
		if (Letters.FROM_DECK == letter
		&& letterDeck != null) {
			return letterDeck.draw();
		}
		
		// Sinon on retourne la lettre
		return letter;
	}
}
