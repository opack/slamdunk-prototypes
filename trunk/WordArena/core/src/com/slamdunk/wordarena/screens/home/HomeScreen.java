package com.slamdunk.wordarena.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.ui.Popup;
import com.slamdunk.wordarena.screens.worldmap.WorldScreen;

public class HomeScreen extends SlamScreen {

	public HomeScreen(SlamGame game) {
		super(game);
		
		// On va utiliser une couche de contrôles
		UIOverlay ui = OverlayFactory.createUIOverlay(new FitViewport(800, 480));
		// Chargement d'une interface déclarée dans un fichier externe
		ui.loadLayout("layouts/home.json");
		// Création des listeners qui interprèteront les clics sur les boutons
		final Popup popup = (Popup)ui.getActor("quit-confirm");
		Map<String, EventListener> listeners = new HashMap<String, EventListener>();
		listeners.put("play", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(WorldScreen.NAME);
			}
		});
		listeners.put("quit", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				popup.setMessage("Voulez-vous vraiment quitter ?");
				popup.show();
			}
		});
		listeners.put("ok", new ButtonClickListener() {
			// Click sur ok lorsqu'on demande à confirmer qu'on veut bien quitter
			@Override
			public void clicked(Button button) {
				Gdx.app.exit();
			}
		});
		listeners.put("cancel", new ButtonClickListener() {
			// Click sur ok lorsqu'on demande à confirmer qu'on veut bien quitter
			@Override
			public void clicked(Button button) {
				popup.hide();
			}
		});
		ui.setListeners(listeners);
		// Ajout de la couche à l'écran
		addOverlay(ui);
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
