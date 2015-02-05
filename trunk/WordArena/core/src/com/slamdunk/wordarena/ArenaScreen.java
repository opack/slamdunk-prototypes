package com.slamdunk.wordarena;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";

	public ArenaScreen(SlamGame game) {
		super(game);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
