package com.slamdunk.wordarena.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.screen.overlays.OverlayFactory;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.ui.Popup;

public class HomeScreen extends SlamScreen {

	public HomeScreen() {
		// On va utiliser une couche de contr�les
		UIOverlay ui = OverlayFactory.createUIOverlay();
		// Chargement d'une interface d�clar�e dans un fichier externe
		ui.loadLayout("layouts/home.json");
		// Cr�ation des listeners qui interpr�teront les clics sur les boutons
		final Popup popup = (Popup)ui.getActor("quit-confirm");
		Map<String, EventListener> listeners = new HashMap<String, EventListener>();
		listeners.put("quit", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				popup.setMessage("Voulez-vous vraiment quitter ?");
				popup.show();
			}
		});
		listeners.put("ok", new ButtonClickListener() {
			// Click sur ok lorsqu'on demande � confirmer qu'on veut bien quitter
			@Override
			public void clicked(Button button) {
				Gdx.app.exit();
			}
		});
		listeners.put("cancel", new ButtonClickListener() {
			// Click sur ok lorsqu'on demande � confirmer qu'on veut bien quitter
			@Override
			public void clicked(Button button) {
				popup.hide();
			}
		});
		ui.setListeners(listeners);
		// Ajout de la couche � l'�cran
		addOverlay(ui);
	}
	
	@Override
	public String getName() {
		return "HOME";
	}
}
