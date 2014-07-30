package com.slamdunk.toolkit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.slamdunk.toolkit.settings.SlamViewportSettings;

/**
 * Représente un écran du jeu.
 * @author Yed
 *
 */
public abstract class SlamScreen implements Screen {

	private SlamGame game;
	
	private Stage stage;
	
	private boolean backButtonActive;
	
	public SlamScreen() {
		// Crée le stage
		if (!SlamViewportSettings.viewportSet) {
			System.err.println("Les réglages n'ont pas été définis !");
		}
		stage = new Stage(SlamViewportSettings.SCREEN_W, SlamViewportSettings.SCREEN_H, false);
		stage.getCamera().position.set(SlamViewportSettings.SCREEN_W / 2, SlamViewportSettings.SCREEN_H / 2, 0);
		Gdx.input.setInputProcessor(stage);
		
		// Crée le listener sur le bouton Back
		createBackButtonListener();
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
	 * Retourne le Stage dans lequel les acteurs
	 * évoluent.
	 * @return
	 */
	public Stage getStage() {
		return stage;
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
	private void createBackButtonListener() {
		stage.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					if (backButtonActive) {
						keyBackPressed();
					}
				}
				return false;
			}
		});
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
		stage.act(delta);

		// Dessine le stage (affichage de l'état)
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
