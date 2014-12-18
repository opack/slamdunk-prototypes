package com.slamdunk.wordarena.screens.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.units.Units;

public class InGameUIOverlay extends UIOverlay {
	private static final int NB_SPAWN_BUTTONS = 8;
	
	/**
	 * Unité actuellement sélectionnée pour être créée
	 */
	private Units selectedUnit;
	
	public InGameUIOverlay() {
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(800, 480));
		// Par défaut, la skin par défaut sera utilisée
		setSkin(new Skin(Gdx.files.internal(UIOverlay.DEFAULT_SKIN)));
	}
	
	/**
	 * Initialise la couche
	 */
	public void init(final Units... spawnables) {
		// Charge les widgets
		getStage().clear();
		loadLayout("layouts/game.json");
		
		// Tous les boutons sont placés dans un groupe pour que seul un soit actif à la fois
		ButtonGroup group = new ButtonGroup();
		Button spawnButton;
		for (int curButton = 0; curButton < NB_SPAWN_BUTTONS; curButton++) {
			spawnButton = (Button)getActor("spawn_unit" + curButton);
			if (curButton < spawnables.length) {
				Units unit = spawnables[curButton];
				spawnButton.setUserObject(unit);
				
				Image buttonImage = unit.getButtonImage();
				if (buttonImage != null) {
					// On ne veut pas que l'image récupère les touches, mais plutôt le bouton
					buttonImage.setTouchable(Touchable.disabled);
					spawnButton.add(buttonImage);
				}
				group.add(spawnButton);
			} else {
				spawnButton.remove();
			}
		}
		final Button moveCamera = (Button)getActor("move_camera");
		group.add(moveCamera);
		group.uncheckAll();
		
		// Par défaut, on active le bouton de déplacement de la caméra
		moveCamera.setChecked(true);
		selectedUnit = null;
		
		// Création des listeners qui interprèteront les clics sur les boutons
		Map<String, EventListener> listeners = new HashMap<String, EventListener>();
		for (int curSpawn = 0; curSpawn < NB_SPAWN_BUTTONS; curSpawn++) {
			listeners.put("spawn_unit" + curSpawn, new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					Button clickedButton = (Button)event.getTarget();
					// Définit l'unité à créer
					if (clickedButton.isChecked()) {
						selectedUnit = (Units)clickedButton.getUserObject();
					}
				}
			});
		}
		listeners.put("move_camera", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				selectedUnit = null;
			}
		});
		setListeners(listeners);
	}

	public Units getSelectedUnit() {
		return selectedUnit;
	}
}
