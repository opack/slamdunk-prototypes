package com.slamdunk.toolkit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.game.overlays.MiniMapOverlay;
import com.slamdunk.toolkit.game.overlays.SlamStageOverlay;
import com.slamdunk.toolkit.game.overlays.UIOverlay;
import com.slamdunk.toolkit.game.overlays.WorldOverlay;
import com.slamdunk.toolkit.settings.SlamViewportSettings;

/**
 * Repr�sente un �cran du jeu. Cet �cran peut contenir plusieurs couches (monde, IHM, minimap...).
 */
public abstract class SlamScreen implements Screen, InputProcessor {
	public static final String DEFAULT_SKIN = "skins/uiskin/uiskin.json";
	private SlamGame game;
	
	private WorldOverlay worldOverlay;
	private UIOverlay uiOverlay;
	private MiniMapOverlay minimapOverlay;
	private InputMultiplexer inputMux;
	
	private boolean backButtonActive;
	
	public SlamScreen() {
		// Cr�ation de l'input processor
		inputMux = new InputMultiplexer();
		inputMux.addProcessor(this);
		Gdx.input.setInputProcessor(inputMux);
	}
	
	/**
	 * Initialise un overlay et ajoute son stage aux input processors
	 * @param overlay
	 * @param processInputs Si true, alors le stage est ajout� aux input processors
	 */
	private void setupOverlay(SlamStageOverlay overlay, float width, float height) {
		// Initialisation de la couche
		if (width <= 0 || height <= 0) {
			if (!SlamViewportSettings.viewportSet) {
				System.err.println("Impossible de cr�er l'overlay car les r�glages n'ont pas �t� d�finis !");
			} else {
				if (width <= 0) {
					width = SlamViewportSettings.SCREEN_W;
				}
				if (height <= 0) {
					height = SlamViewportSettings.SCREEN_H;
				}
			}
		}
		overlay.createStage(width, height);
		
		// Ajout du Stage de cette couche en tant qu'input processor
		if (overlay.isProcessInputs()) {
			inputMux.addProcessor(overlay.getStage());
		}
	}
	
	/**
	 * Cr�e un overlay destin� � afficher le monde
	 */
	public void setupWorldOverlay() {
		// Cr�ation de la couche
		worldOverlay = new WorldOverlay();
		setupOverlay(worldOverlay, 0, 0);
	}
	
	public WorldOverlay getWorldOverlay() {
		return worldOverlay;
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionn� � l'emplacement indiqu� et aura la taille indiqu�e.
	 */
	public void setupUIOverlay(float x, float y, float width, float height) {
		// Cr�ation de la couche
		uiOverlay = new UIOverlay();
		Skin uiSkin = new Skin(Gdx.files.internal(DEFAULT_SKIN));
		uiOverlay.setSkin(uiSkin);
		setupOverlay(uiOverlay, width, height);
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay s'�talera sur toute la surface de l'�cran.
	 */
	public void setupUIOverlay() {
		setupUIOverlay(0, 0, 0, 0);
	}
	
	public UIOverlay getUIOverlay() {
		return uiOverlay;
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionn� � l'emplacement indiqu� et aura la taille indiqu�e.
	 */
	public void setupMiniMapOverlay(float x, float y, float width, float height) {
		// Cr�ation de la couche
		minimapOverlay = new MiniMapOverlay();
		setupOverlay(minimapOverlay, width, height);
	}
	
	/**
	 * Cr�e un overlay destin� � afficher les boutons et autres composants d'interface.
	 * Cet overlay s'�talera sur toute la surface de l'�cran.
	 */
	public void setupMiniMapOverlay() {
		setupMiniMapOverlay(0, 0, 0, 0);
	}
	
	public MiniMapOverlay getMiniMapOverlay() {
		return minimapOverlay;
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
		if (worldOverlay != null) {
			worldOverlay.act(delta);
		}
		if (uiOverlay != null) {
			uiOverlay.act(delta);
		}
		if (minimapOverlay != null) {
			minimapOverlay.act(delta);
		}

		// Dessine les couches (affichage de l'�tat)
		if (worldOverlay != null) {
			worldOverlay.draw();
		}
		if (uiOverlay != null) {
			uiOverlay.draw();
		}
		if (minimapOverlay != null) {
			minimapOverlay.draw();
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
		if (worldOverlay != null) {
			worldOverlay.dispose();
		}
		if (uiOverlay != null) {
			uiOverlay.dispose();
		}
		if (minimapOverlay != null) {
			minimapOverlay.dispose();
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
