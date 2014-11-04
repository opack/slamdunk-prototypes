package com.slamdunk.wordarena.old;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.wordarena.old.letters.Letters;
import com.slamdunk.wordarena.old.numberduel.Player;

public class LetterCell {
	/**
	 * La lettre de la case
	 */
	public Letters letter;
	/**
	 * Le bouton associé à la case
	 */
	public TextButton button;
	/**
	 * Le joueur qui contrôle actuellement la case
	 */
	public Player controler;
}
