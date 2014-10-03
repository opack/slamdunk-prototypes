package com.slamdunk.toolkit.screen.overlays;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

/**
 * Une couche d'affichage qui a son propre Stage et peut être utilisée
 * pour gérer l'interface utilisateur.
 */
public class UIOverlay extends StageOverlay {
	
	/**
	 * Skin utilisée pour dessiner les composants de l'interface
	 */
	private Skin skin;
	
	/**
	 * Les différents niveaux de tables courantes pour l'ajout de composants
	 */
	private LinkedList<Table> tables;

	public UIOverlay() {
		tables = new LinkedList<Table>();
		
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		tables.addFirst(mainTable);
	}
	
	@Override
	public void createStage(Viewport viewport) {
		super.createStage(viewport);
		
		Table mainTable = tables.get(0);
		mainTable.pack();
		getStage().addActor(mainTable);
	}
	
	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	/**
	 * Ajoute une cellule vide à la prochaine position de la table
	 * courante
	 */
	@SuppressWarnings("unchecked")
	public Cell<Actor> add() {
		return (Cell<Actor>)tables.getFirst().add();
	}
	
	/**
	 * Ajoute une cellule contenant le widget indiqué à la prochaine
	 * position de la table courante
	 */
	public Cell<Actor> add(Actor widget) {
		return (Cell<Actor>)tables.getFirst().add(widget);
	}
	
	/**
	 * Termine la ligne actuelle et passe à la suivante
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cell<Actor> row() {
		return (Cell<Actor>)tables.getFirst().row();
	}
	
	/**
	 * Ajoute une table dans la prochaine position et fait de cette table
	 * la table courante
	 * @return
	 */
	public Cell<Table> subTableBegin() {
		Table subTable = new Table();
		Cell<Table> cell = tables.getFirst().add(subTable);
		tables.addFirst(subTable);
		return cell;
	}
	
	/**
	 * Ferme la sous-table
	 * @return
	 */
	public void subTableEnd() {
		// Pack la table
		tables.getFirst().pack();
		// Supprime la table courante de la liste
		tables.removeFirst();
	}
	
	/**
	 * Ajoute un bouton text utilisant la skin définie pour l'overlay
	 * à la prochaine position dans la même ligne de la table courante
	 */
	public Cell<Actor> addTextButton(String text) {
		TextButton button = new TextButton(text, skin);
		return add(button);
	}
	
	/**
	 * Ajoute un label utilisant la skin définie pour l'overlay
	 * à la prochaine position dans la même ligne de la table courante
	 */
	public Cell<Actor> addLabel(String text) {
		Label label = new Label(text, skin);
		return add(label);
	}
	
	/**
	 * Ajoute un champ d'édition de texte utilisant la skin définie pour l'overlay
	 * à la prochaine position dans la même ligne de la table courante
	 */
	public Cell<Actor> addTextField(String text) {
		TextField textField = new TextField(text, skin);
		return add(textField);
	}

	/**
	 * Charge l'IHM décrite dans le fichier JSON spécifié
	 * @param string
	 */
	public void loadLayout(String layoutFile) {
		JsonUIBuilder uiCreator = new JsonUIBuilder(skin);
		uiCreator.load(layoutFile);
		uiCreator.populate(getStage());
	}

	@Override
	public boolean isProcessInputs() {
		return true;
	}
}
