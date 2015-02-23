package com.slamdunk.wordarena.screens.arena;

import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.actors.ZoomGestureHandler;
import com.slamdunk.wordarena.actors.ZoomInputProcessor;
import com.slamdunk.wordarena.data.GameManager;
import com.slamdunk.wordarena.data.Player;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private GameManager gameManager;
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	public ArenaScreen(SlamGame game) {
		super(game);

		gameManager = new GameManager();
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI(gameManager);
		addOverlay(ui);

		// Gestionnaires permettant de zoomer avec la souris ou un pinch.
		// Ces gestionnaires sont insérés après ArenaUI.
		OrthographicCamera camera = (OrthographicCamera)arena.getStage().getCamera();
		getInputMultiplexer().addProcessor(1, new GestureDetector(new ZoomGestureHandler(camera)));
		getInputMultiplexer().addProcessor(1, new ZoomInputProcessor(camera));
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void pause () {
		gameManager.pause();
	}
	
	public ArenaOverlay getArena() {
		return arena;
	}

	public ArenaUI getUI() {
		return ui;
	}

	public void centerCamera() {
		arena.centerCamera();
	}
	
	public void prepareGame(String arenaPlanFile, List<Player> players) {
		gameManager.prepareGame(this, arenaPlanFile, players);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// Au cours d'une partie, appuyer sur back met le jeu en pause
		if (keycode == Keys.BACK) {
			gameManager.requestBack();
		}
	    return false;
	 }
}
