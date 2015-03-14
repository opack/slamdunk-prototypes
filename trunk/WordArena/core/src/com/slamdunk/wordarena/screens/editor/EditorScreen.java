package com.slamdunk.wordarena.screens.editor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.data.ArenaData;
import com.slamdunk.wordarena.data.ArenaSerializer;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.WallTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;

public class EditorScreen extends SlamScreen {
public static final String NAME = "EDITOR";
	private EditorArenaOverlay arena;
	private EditorUI ui;
	private EditorGameManager gameManager;
	
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends EditorTool>, EditorTool> tools;
	
	@SuppressWarnings("rawtypes")
	private EditorTool currentTool;
	
	public EditorScreen(WordArenaGame game) {
		super(game);
		
		createGameManager();
		createTools();
	
		arena = new EditorArenaOverlay();
		addOverlay(arena);
		
		ui = new EditorUI(this);
		addOverlay(ui);
	}
	
	private void createGameManager() {
		gameManager = new EditorGameManager();
		Array<Player> players = new Array<Player>();
		players.add(Player.NEUTRAL);
		players.add(new Player(1, Assets.i18nBundle.get("ui.editor.player.1"), "blue"));
		players.add(new Player(2, Assets.i18nBundle.get("ui.editor.player.2"), "orange"));
		players.add(new Player(3, Assets.i18nBundle.get("ui.editor.player.3"), "green"));
		players.add(new Player(4, Assets.i18nBundle.get("ui.editor.player.4"), "purple"));
		gameManager.setPlayers(players);
	}

	@SuppressWarnings("rawtypes")
	private void createTools() {
		tools = new HashMap<Class<? extends EditorTool>, EditorTool>();
		tools.put(CellTypeTool.class, new CellTypeTool());
		tools.put(LetterTool.class, new LetterTool());
		tools.put(PowerTool.class, new PowerTool());
		tools.put(OwnerTool.class, new OwnerTool());
		tools.put(ZoneTool.class, new ZoneTool());
		tools.put(WallTool.class, new WallTool());
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			getGame().setScreen(PreEditorScreen.NAME);
		}
	    return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends EditorTool> T getTool(Class<T> toolClass) {
		return (T)tools.get(toolClass);
	}

	@SuppressWarnings("rawtypes")
	public void setCurrentTool(Class<? extends EditorTool> toolClass) {
		currentTool = getTool(toolClass);
	}

	@SuppressWarnings("rawtypes")
	public EditorTool getCurrentTool() {
		return currentTool;
	}

	public void changeArenaSize(int width, int height) {
		arena.setArenaSize(width, height);
		arena.resetArena();
	}

	public ArenaZone getOrCreateZone(String id) {
		for (ArenaZone zone : arena.getData().zones) {
			if (zone.getData().id.equals(id)) {
				return zone;
			}
		}
		return arena.createZone(id);
	}

	public void save() {
		Json json = new Json();
		json.setSerializer(ArenaData.class, new ArenaSerializer());
		final String serialized = json.prettyPrint(arena.getData());
		
		FileHandle file = Gdx.files.absolute("E:\\Projets\\Programmes\\slamdunk-prototypes\\WordArena\\android\\assets\\arenas\\" + arena.getData().name + ".json");
		file.writeString(serialized, false, "UTF-8");
	}
	
	public void createNewArena(String name, int width, int height) {
		arena.setArenaSize(width, height);
		arena.resetArena();
		prepareUI(name);
	}
	
	public void editExistingArena(String name) {
		arena.buildArena("arenas/" + name + ".json", gameManager);
		prepareUI(name);
	}
	
	private void prepareUI(String name) {
		arena.getData().name = name;
		ui.loadData(arena.getData());
		getTool(WallTool.class).setArena(arena);
	}

	public Array<Player> getPlayers() {
		return gameManager.getPlayers();
	}
}
