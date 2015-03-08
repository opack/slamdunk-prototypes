package com.slamdunk.wordarena.screens.editor.tools;

import java.util.Collection;

import com.slamdunk.wordarena.actors.ArenaCell;

public interface EditorTool<Value> {
	
	/**
	 * Définit la valeur à appliquer
	 */
	void setValue(Value value);
	
	/**
	 * Récupère la valeur à appliquer
	 */
	Value getValue();
	
	/**
	 * Applique l'outil sur la cellule indiquée
	 * @param cell
	 */
	void apply(ArenaCell cell);
	
	/**
	 * Applique l'outil sur les cellules indiquées
	 * @param cell
	 */
	void apply(Collection<ArenaCell> cells);
}
