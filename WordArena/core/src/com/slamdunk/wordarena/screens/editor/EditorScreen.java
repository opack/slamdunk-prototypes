package com.slamdunk.wordarena.screens.editor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class EditorScreen extends SlamScreen {
public static final String NAME = "EDITOR";
	private EditorArenaOverlay arena;
	private EditorUI ui;
	
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends EditorTool>, EditorTool> tools;
	
	@SuppressWarnings("rawtypes")
	private EditorTool currentTool;
	
	public EditorScreen(WordArenaGame game) {
		super(game);
		game.setClearColor(0.6f, 0.6f, 0.6f, 1);
		
		createTools();
		
		arena = new EditorArenaOverlay();
		addOverlay(arena);
		
		ui = new EditorUI(this);
		addOverlay(ui);
	}
	
	@SuppressWarnings("rawtypes")
	private void createTools() {
		tools = new HashMap<Class<? extends EditorTool>, EditorTool>();
		tools.put(CellTypeTool.class, new CellTypeTool());
		tools.put(LetterTool.class, new LetterTool());
		tools.put(PowerTool.class, new PowerTool());
		tools.put(OwnerTool.class, new OwnerTool());
		tools.put(ZoneTool.class, new ZoneTool());
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {
			getGame().setScreen(HomeScreen.NAME);
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
		arena.resetEditArena();
	}

	public ArenaZone getOrCreateZone(String id) {
		for (ArenaZone zone : arena.getData().zones) {
			if (zone.getData().id.equals(id)) {
				return zone;
			}
		}
		return arena.createZone(id);
	}
}
