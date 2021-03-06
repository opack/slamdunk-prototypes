package com.slamdunk.wordarena;

import com.badlogic.gdx.Gdx;
import com.slamdunk.toolkit.screen.SlamGame;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.wordarena.screens.arena.ArenaScreen;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.home.HomeScreen;
import com.slamdunk.wordarena.screens.preeditor.PreEditorScreen;

public class WordArenaGame extends SlamGame {
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;
	
	@Override
	public void create() {
		super.create();
		
		setClearColor(1, 1, 1, 1);
		Gdx.input.setCatchBackKey(true);
		
		// Initialise les réglages
		SlamSettings.init("WordArena");
		
		// Charge les ressources
		Assets.load();

		// Connecte l'utilisateur
		if (!UserManager.getInstance().logIn()) {
			// TODO DBG Faire une boîte de saisie d'identifiants
			System.err.println("Connexion échouée. Veuillez saisir vos identifiants ou créer un compte.");
		}
		
		// Crée les écrans
		addScreen(new HomeScreen(this));
		addScreen(new ArenaScreen(this));
		addScreen(new PreEditorScreen(this));
		addScreen(new EditorScreen(this));
		
		// Affiche le premier écran
		setScreen(HomeScreen.NAME);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}
}
