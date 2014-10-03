package com.slamdunk.toolkit.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.slamdunk.toolkit.screen.overlays.SlamOverlay;

/**
 * Repr�sente un �cran du jeu. Cet �cran peut contenir plusieurs couches (monde, IHM, minimap...).
 */
public abstract class SlamScreen implements Screen, InputProcessor {
	private SlamGame game;
	
	private List<SlamOverlay> overlays;
	private InputMultiplexer inputMux;
	
	private boolean backButtonActive;
	
	public SlamScreen() {
		// Cr�ation de la liste d'overlays
		overlays = new ArrayList<SlamOverlay>();
		
		// Cr�ation de l'input processor
		inputMux = new InputMultiplexer();
		inputMux.addProcessor(this);
		Gdx.input.setInputProcessor(inputMux);
	}
	
	/**
	 * Ajoute un overlay � la liste et enregistre son input processor
	 * @param overlay
	 */
	public void addOverlay(SlamOverlay overlay) {
		// Ajout de la couche � la liste
		overlays.add(overlay);
		
		// Ajout de l'input processor de cette couche
		if (overlay.isProcessInputs()) {
			inputMux.addProcessor(overlay.getInputProcessor());
		}
	}
	
	/**
	 * Renvoit le nom unique associ� � cet �cran
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
	 * D�finit le jeu auquel est rattach� cet �cran
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
	 * D�finit si le bouton back est activ� et permet donc
	 * de revenir � l'�cran pr�c�dent, ou s'il n'a aucun effet.
	 */
	public void setBackButtonActive(boolean active) {
		Gdx.input.setCatchBackKey(active);
		this.backButtonActive = active;
	}
	
	/**
	 * Ajoute un listener au Stage afin de g�rer le bouton back.
	 * Une m�thode d�l�gu�e sera appel�e si le bouton back est
	 * activ�.
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
	 * M�thode � red�finir pour effectuer du traitement lors de l'appui
	 * sur le bouton back.
	 * Attention ! Cette m�thode n'est appel�e que si un appel
	 * � {@link #setBackButtonActive(boolean)} a �t� fait.
	 * @see #setBackButtonActive(boolean)
	 */
	public void keyBackPressed() {
	}

	@Override
	public void render(float delta) {
		// Efface l'�cran
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Fait agir les acteurs (mise � jour de la logique du jeu)
		for (SlamOverlay overlay : overlays) {
			overlay.act(delta);
		}
		
		// Dessine les couches (affichage de l'�tat)
		for (SlamOverlay overlay : overlays) {
			overlay.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
