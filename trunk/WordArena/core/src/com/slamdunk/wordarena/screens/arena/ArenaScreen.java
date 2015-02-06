package com.slamdunk.wordarena.screens.arena;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.enums.GameStates;

public class ArenaScreen extends SlamScreen {
	public static final String NAME = "ARENA";
	
	private ArenaOverlay arena;
	private ArenaUI ui;
	
	private GameStates state;
	private WordSelectionHandler wordSelectionHandler;
	
	private List<Sprite> lines;

	public ArenaScreen(SlamGame game) {
		super(game);
		
		arena = new ArenaOverlay();
		addOverlay(arena);
		
		ui = new ArenaUI();
		addOverlay(ui);
		
		wordSelectionHandler = new WordSelectionHandler();
		loadNextLevel();
		changeState(GameStates.READY);
		
		lines = new ArrayList<Sprite>();
		lines.add(drawLine(new Vector2(10, 10), new Vector2(150, 10)));
		lines.add(drawLine(new Vector2(150, 10), new Vector2(250, 10)));
		lines.add(drawLine(new Vector2(250, 10), new Vector2(200, 100)));
		lines.add(drawLine(new Vector2(200, 100), new Vector2(200, 50)));
		lines.add(drawLine(new Vector2(200, 50), new Vector2(10, 50)));
		lines.add(drawLine(new Vector2(10, 50), new Vector2(10, 10)));
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public WordSelectionHandler getWordSelectionHandler() {
		return wordSelectionHandler;
	}
	
	@Override
	public void pause () {
		if (state == GameStates.RUNNING) {
			changeState(GameStates.PAUSED);
		}
	}

	public void loadNextLevel() {
		arena.buildArena(10, 10);
		wordSelectionHandler.reset();
	}

	/**
	 * Change l'état actuel du jeu et met à jour l'IHM
	 * @param newState
	 */
	public void changeState(GameStates newState) {
		state = newState;
		ui.present(newState);
	}
	
	/**
	 * Vérifie si le mot est valide, ajoute des points au score
	 * le cas échéant et choisit d'autres lettres sur le mot
	 * sélectionné.
	 */
	public void validateWord() {
		if (wordSelectionHandler.validate()) {
			System.out.println("ArenaScreen.validateWord() Mot valide B-)");
		} else {
			System.out.println("ArenaScreen.validateWord() Mot invalide :(");
		}
		
		cancelWord();
	}

	/**
	 * Réinitialise les lettres sélectionnées
	 */
	public void cancelWord() {
		wordSelectionHandler.reset();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		Batch batch = ui.getStage().getBatch();
		batch.begin();
		for (Sprite line : lines) {
			line.draw(batch);
		}
		batch.end();
	}
	
	private Sprite drawLine(final Vector2 s, final Vector2 e) {
		int d = (int) s.dst(e);
		int h = Assets.edge.getHeight();
		
		Sprite sprite = new Sprite(Assets.edge, 0, 0, d, h);
		sprite.setOrigin(0, h/2);
		sprite.setPosition(s.x, s.y);
		float degrees = (float)Math.toDegrees(Math.atan2(e.y - s.y, e.x - s.x));
		sprite.setRotation(degrees);
		return sprite;
   }
}
