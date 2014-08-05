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
 * Représente un écran du jeu. Cet écran peut contenir plusieurs couches (monde, IHM, minimap...).
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
		// Création de l'input processor
		inputMux = new InputMultiplexer();
		inputMux.addProcessor(this);
		Gdx.input.setInputProcessor(inputMux);
	}
	
	/**
	 * Initialise un overlay et ajoute son stage aux input processors
	 * @param overlay
	 * @param processInputs Si true, alors le stage est ajouté aux input processors
	 */
	private void setupOverlay(SlamStageOverlay overlay, float width, float height) {
		// Initialisation de la couche
		if (width <= 0 || height <= 0) {
			if (!SlamViewportSettings.viewportSet) {
				System.err.println("Impossible de créer l'overlay car les réglages n'ont pas été définis !");
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
	 * Crée un overlay destiné à afficher le monde
	 */
	public void setupWorldOverlay() {
		// Création de la couche
		worldOverlay = new WorldOverlay();
		setupOverlay(worldOverlay, 0, 0);
	}
	
	public WorldOverlay getWorldOverlay() {
		return worldOverlay;
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionné à l'emplacement indiqué et aura la taille indiquée.
	 */
	public void setupUIOverlay(float x, float y, float width, float height) {
		// Création de la couche
		uiOverlay = new UIOverlay();
		Skin uiSkin = new Skin(Gdx.files.internal(DEFAULT_SKIN));
		uiOverlay.setSkin(uiSkin);
		setupOverlay(uiOverlay, width, height);
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay s'étalera sur toute la surface de l'écran.
	 */
	public void setupUIOverlay() {
		setupUIOverlay(0, 0, 0, 0);
	}
	
	public UIOverlay getUIOverlay() {
		return uiOverlay;
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay sera positionné à l'emplacement indiqué et aura la taille indiquée.
	 */
	public void setupMiniMapOverlay(float x, float y, float width, float height) {
		// Création de la couche
		minimapOverlay = new MiniMapOverlay();
		setupOverlay(minimapOverlay, width, height);
	}
	
	/**
	 * Crée un overlay destiné à afficher les boutons et autres composants d'interface.
	 * Cet overlay s'étalera sur toute la surface de l'écran.
	 */
	public void setupMiniMapOverlay() {
		setupMiniMapOverlay(0, 0, 0, 0);
	}
	
	public MiniMapOverlay getMiniMapOverlay() {
		return minimapOverlay;
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
	 * Ajoute un listener au Stage afin de gérer le bouton back.
	 * Une méthode déléguée sera appelée si le bouton back est
	 * activé.
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
		// Efface l'écran
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Fait agir les acteurs (mise à jour de la logique du jeu)
		if (worldOverlay != null) {
			worldOverlay.act(delta);
		}
		if (uiOverlay != null) {
			uiOverlay.act(delta);
		}
		if (minimapOverlay != null) {
			minimapOverlay.act(delta);
		}

		// Dessine les couches (affichage de l'état)
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
