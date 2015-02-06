package com.slamdunk.toolkit.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.slamdunk.toolkit.screen.overlays.SlamOverlay;

/**
 * Représente un écran du jeu. Cet écran peut contenir plusieurs couches (monde, IHM, minimap...).
 */
public abstract class SlamScreen implements Screen, InputProcessor {
	private SlamGame game;
	
	private List<SlamOverlay> overlays;
	private InputMultiplexer inputMux;
	
	private boolean backButtonActive;
	
	public SlamScreen(SlamGame game) {
		this();
		this.game = game;
	}
	
	public SlamScreen() {
		// Création de la liste d'overlays
		overlays = new ArrayList<SlamOverlay>();
		
		// Création de l'input processor
		inputMux = new InputMultiplexer();
		inputMux.addProcessor(this);
		Gdx.input.setInputProcessor(inputMux);
	}
	
	/**
	 * Ajoute un overlay à la liste et enregistre son input processor
	 * @param overlay
	 */
	public void addOverlay(SlamOverlay overlay) {
		// Ajout de la couche à la liste
		overlays.add(overlay);
		overlay.setScreen(this);
		
		// Ajout de l'input processor de cette couche
		if (overlay.isProcessInputs()) {
			// La dernière couche sera celle qui sera la plus "haute", donc
			// la première à recevoir les inputs
			inputMux.addProcessor(0, overlay.getInputProcessor());
		}
	}
	
	/**
	 * Renvoit le nom unique associé à cet écran
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * @see #setGame(SlamGame)
	 */
	public SlamGame getGame() {
		return game;
	}
	
	/**
	 * Définit le jeu auquel est rattaché cet écran
	 */
	public void setGame(SlamGame game) {
		this.game = game;
	}

	/**
	 * @see SlamScreen#setBackButtonActive(boolean)
	 */
	public boolean isBackButtonActive() {
		return backButtonActive;
	}

	/**
	 * Définit si le bouton back est activé et permet donc
	 * de revenir à l'écran précédent, ou s'il n'a aucun effet.
	 */
	public void setBackButtonActive(boolean active) {
		Gdx.input.setCatchBackKey(active);
		this.backButtonActive = active;
	}
	
	/**
	 * La méthode par défaut dans SlamScreen gère le bouton back en
	 * appelant la méthode keyBackPressed(), appelée si le bouton back
	 * est activé.
	 * @see #keyBackPressed()
	 */
	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			if (backButtonActive) {
				keyBackPressed();
			}
		}
		return false;
	}
	
	/**
	 * Méthode à redéfinir pour effectuer du traitement lors de l'appui
	 * sur le bouton back.
	 * Attention ! Cette méthode n'est appelée que si un appel
	 * à {@link #setBackButtonActive(boolean)} a été fait.
	 * @see #setBackButtonActive(boolean)
	 */
	public void keyBackPressed() {
	}

	@Override
	public void render(float delta) {
		// Fait agir les acteurs (mise à jour de la logique du jeu)
		for (SlamOverlay overlay : overlays) {
			overlay.act(delta);
		}
		
		// Dessine les couches (affichage de l'état)
		for (SlamOverlay overlay : overlays) {
			overlay.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		for (SlamOverlay overlay : overlays) {
			overlay.dispose();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
