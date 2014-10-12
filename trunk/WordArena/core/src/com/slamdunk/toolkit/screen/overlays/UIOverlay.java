package com.slamdunk.toolkit.screen.overlays;

import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

/**
 * Une couche d'affichage qui a son propre Stage et peut �tre utilis�e
 * pour g�rer l'interface utilisateur.
 */
public class UIOverlay extends StageOverlay {
	
	/**
	 * Skin utilis�e pour dessiner les composants de l'interface
	 */
	private Skin skin;
	
	/**
	 * Les diff�rents niveaux de tables courantes pour l'ajout de composants
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
	 * Ajoute une cellule vide � la prochaine position de la table
	 * courante
	 */
	@SuppressWarnings("unchecked")
	public Cell<Actor> add() {
		return (Cell<Actor>)tables.getFirst().add();
	}
	
	/**
	 * Ajoute une cellule contenant le widget indiqu� � la prochaine
	 * position de la table courante
	 */
	public Cell<Actor> add(Actor widget) {
		return (Cell<Actor>)tables.getFirst().add(widget);
	}
	
	/**
	 * Termine la ligne actuelle et passe � la suivante
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
	 * Ajoute un bouton text utilisant la skin d�finie pour l'overlay
	 * � la prochaine position dans la m�me ligne de la table courante
	 */
	public Cell<Actor> addTextButton(String text) {
		TextButton button = new TextButton(text, skin);
		return add(button);
	}
	
	/**
	 * Ajoute un label utilisant la skin d�finie pour l'overlay
	 * � la prochaine position dans la m�me ligne de la table courante
	 */
	public Cell<Actor> addLabel(String text) {
		Label label = new Label(text, skin);
		return add(label);
	}
	
	/**
	 * Ajoute un champ d'�dition de texte utilisant la skin d�finie pour l'overlay
	 * � la prochaine position dans la m�me ligne de la table courante
	 */
	public Cell<Actor> addTextField(String text) {
		TextField textField = new TextField(text, skin);
		return add(textField);
	}

	/**
	 * Charge l'IHM d�crite dans le fichier JSON sp�cifi�
	 * @param string
	 */
	public void loadLayout(String layoutFile, Map<String, EventListener> listeners) {
		// Cr�e l'IHM et peuple le stage
		JsonUIBuilder uiCreator = new JsonUIBuilder(skin);
		uiCreator.load(layoutFile);
		uiCreator.populate(getStage());
		
		// Ajoute les �ventuels listeners aux acteurs
		if (listeners != null) {
			setListeners(listeners);
		}
	}
	
	/**
	 * Charge l'IHM d�crite dans le fichier JSON sp�cifi�
	 * @param string
	 */
	public void loadLayout(String layoutFile) {
		loadLayout(layoutFile, null);
	}
	
	/**
	 * Affecte les listeners indiqu�s aux objets dont le nom correspond � la
	 * cl� de la table.
	 * @param listeners
	 */
	public void setListeners(Map<String, EventListener> listeners) {
		Group stageRoot = getStage().getRoot();
		Actor actor;
		for (Map.Entry<String, EventListener> entry : listeners.entrySet()) {
			actor = stageRoot.findActor(entry.getKey());
			if (actor != null) {
				actor.addListener(entry.getValue());
			}
		}
	}

	@Override
	public boolean isProcessInputs() {
		return true;
	}
	
	public Actor getActor(String name) {
		return getStage().getRoot().findActor(name);
	}
}
