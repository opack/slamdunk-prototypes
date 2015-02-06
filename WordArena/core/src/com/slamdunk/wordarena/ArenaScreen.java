package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.screens.arena.ArenaOverlay;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private ArenaOverlay arena;

	public ArenaScreen(SlamGame game) {
		super(game);
		
		arena = new ArenaOverlay();
		arena.buildArena(10, 10);
		addOverlay(arena);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
