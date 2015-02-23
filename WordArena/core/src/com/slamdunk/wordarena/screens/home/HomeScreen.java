package com.slamdunk.wordarena.screens.home;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.Owners;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;

public class HomeScreen extends SlamScreen {
	public static final String NAME = "HOME";
	
	private HomeUI ui;
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public HomeScreen(WordArenaGame game) {
		super(game);
		
		ui = new HomeUI(this);
		addOverlay(ui);
	}

	public void launchGame(String arenaFile) {
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
		
		ArenaScreen arena = (ArenaScreen)getGame().getScreen(ArenaScreen.NAME);
		arena.prepareGame("arenas/" + arenaFile + ".properties", players);
		getGame().setScreen(ArenaScreen.NAME);
	}
}
