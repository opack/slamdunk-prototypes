package com.slamdunk.toolkit.game.overlays;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.toolkit.ui.layout.json.JsonUICreator;

/**
 * Une couche d'affichage qui a son propre Stage et peut �tre utilis�e
 * pour g�rer l'interface utilisateur.
 */
public class UIOverlay extends SlamStageOverlay {
	
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
	 * Charge l'IHM d�crite dans le fichier JSON sp�cifi�
	 * @param string
	 */
	public void loadLayout(String layoutFile) {
		JsonUICreator uiCreator = new JsonUICreator(skin);
		uiCreator.load(layoutFile);
		uiCreator.populate(getStage());
	}
}
