package com.slamdunk.toolkit.game.overlays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;

/**
 * Une couche d'affichage qui a son propre Stage et peut être utilisée
 * pour gérer l'interface utilisateur.
 */
public class UIOverlay extends SlamStageOverlay {
	
	/**
	 * Skin utilisée pour dessiner les composants de l'interface
	 */
	private Skin skin;
	
	/**
	 * Composant racine qui permet d'organiser tous les autres
	 */
	private Table mainTable;
	
	/**
	 * Table courante pour l'ajout de composants
	 */
	private Table curTable;

	public UIOverlay() {
		mainTable = new Table();
		mainTable.setFillParent(true);
		curTable = mainTable;
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
	public Cell add() {
		return curTable.add();
	}
	
	/**
	 * Ajoute une cellule contenant le widget indiqué à la prochaine
	 * position de la table courante
	 */
	public Cell add(Actor widget) {
		return curTable.add(widget);
	}
	
	/**
	 * Termine la ligne actuelle et passe à la suivante
	 * @return
	 */
	public Cell row() {
		return curTable.row();
	}
	
	/**
	 * Ajoute une table dans la prochaine position et fait de cette table
	 * la table courante
	 * @return
	 */
	public Cell subTable() {
		Table subTable = new Table();
		Cell cell = curTable.add(subTable);
		curTable = subTable;
		return cell;
	}
	
	/**
	 * Ajoute un bouton text utilisant la skin définie pour l'overlay
	 * à la prochaine position dans la même ligne de la table courante
	 */
	public void addTextButton() {
		// TODO Auto-generated method stub
		
	}
}
