package com.slamdunk.wordarena;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.slamdunk.wordarena.letters.Letters;
import com.slamdunk.wordarena.numberduel.Player;

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
