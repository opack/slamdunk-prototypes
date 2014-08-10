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
	 * Le bouton associ� � la case
	 */
	public TextButton button;
	/**
	 * Le joueur qui contr�le actuellement la case
	 */
	public Player controler;
}
