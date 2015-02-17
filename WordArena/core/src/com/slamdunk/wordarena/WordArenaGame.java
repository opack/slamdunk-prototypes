package com.slamdunk.wordarena;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;
	
	@Override
	public void create() {
		super.create();
		setClearColor(1, 1, 1, 1);
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Charge les ressources
		Assets.load();
		
		// Crée les écrans
		addScreen(new ArenaScreen(this));
		
		// DBG Devrait être fait lorsqu'on lance une partie
		launchGame();
		
		// Affiche le premier écran
//		setScreen(ArenaScreen.NAME);
	}

	private void launchGame() {
		Player p1 = new Player();
		p1.name = "Anne";
		p1.score = 0;
		p1.owner = Owners.PLAYER1;
		
		Player p2 = new Player();
		p2.name = "Bob";
		p2.score = 0;
		p2.owner = Owners.PLAYER2;
		
		List<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		
		ArenaScreen arena = (ArenaScreen)getScreen(ArenaScreen.NAME);
		arena.prepareGame("arenas/0.properties", players);
		setScreen(ArenaScreen.NAME);
	}
}
