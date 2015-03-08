package com.slamdunk.wordarena.screens.editor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.enums.ToolTypes;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.EditorTool;
import com.slamdunk.wordarena.screens.home.HomeScreen;

public class EditorScreen extends SlamScreen {
public static final String NAME = "EDITOR";
	
	private EditorUI ui;
	@SuppressWarnings("rawtypes")
	private Map<ToolTypes, EditorTool> tools;
	
	public EditorScreen(WordArenaGame game) {
		super(game);
		
		createTools();
		
		ui = new EditorUI(this);
		addOverlay(ui);
	}
	
	@SuppressWarnings("rawtypes")
	private void createTools() {
		tools = new HashMap<ToolTypes, EditorTool>();
		tools.put(ToolTypes.CELL_TYPE, new CellTypeTool());
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
}
