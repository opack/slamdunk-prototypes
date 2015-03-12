package com.slamdunk.wordarena.data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.toolkit.lang.KeyListMap;
import com.slamdunk.toolkit.lang.TypedProperties;
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
public class ArenaBuilderProperties {
	public static final String ZONE_NONE = ArenaZone.NONE.getData().id;
	private static final String CELL_SEPARATOR = " ";
	
	private GameManager gameManager;
	private Array<Player> players;
	private Skin skin;
	
	private String[] types;
	private String[] letters;
	private int[] powers;
	private int[] owners;
	private String[] zones;
	
	private ArenaData arena;
	
	private KeyListMap<String, ArenaCell> cellsByZone;
	private Set<Point> cellsWithWalls;
	
	public ArenaBuilderProperties(GameManager gameManager) {
		this(gameManager, Assets.skin);
	}
	
	public ArenaBuilderProperties(GameManager gameManager, Skin skin) {
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
		
		// Charge les murs
		// TODO
		
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
		int index;
		for (int y = arena.height - 1; y >= 0; y--) {
			for (int x = 0; x < arena.width; x++) {
				// Petite astuce pour calculer l'index car y=0 est en bas,
				// contrairement au fichier properties
				index = (arena.height - 1 - y) * arena.width + x;
				
				cell = new ArenaCell(skin);
				cell.addListener(new CellSelectionListener(cell, wordSelectionHandler));
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
		if (Letters.FROM_DECK == letter) {
			return letterDeck.draw();
		}
		
		// Sinon on retourne la lettre
		return letter;
	}
}
