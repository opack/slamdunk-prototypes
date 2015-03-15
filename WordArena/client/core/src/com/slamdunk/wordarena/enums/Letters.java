package com.slamdunk.wordarena.enums;

import com.slamdunk.toolkit.lang.Deckable;

public enum Letters implements Deckable {
	A(9, "A"),
	B(2, "B"),
	C(2, "C"),
	D(3, "D"),
	E(15, "E"),
	F(2, "F"),
	G(2, "G"),
	H(2, "H"),
	I(8, "I"),
	J(1, "J"),
	K(1, "K"),
	L(5, "L"),
	M(3, "M"),
	N(6, "N"),
	O(6, "O"),
	P(2, "P"),
	Q(1, "Q"),
	R(6, "R"),
	S(6, "S"),
	T(6, "T"),
	U(6, "U"),
	V(2, "V"),
	W(1, "W"),
	X(1, "X"),
	Y(1, "Y"),
	Z(1, "Z"),
	/**
	 * Valeur pour une cellule qui ne contient pas de lettre
	 */
	EMPTY(0, ""),
	JOKER(0, "?"),
	/**
	 * Valeur pour une lettre qui doit être piochée dans le tas de lettres
	 */
	FROM_TYPE(0, "-");
	
	public int representation;
	public String label;
	
	Letters(int representation, String label) {
		this.representation = representation;
		this.label = label;
	}

	@Override
	public int countInOneDeck() {
		return representation;
	}
	
	public static Letters getFromLabel(String label) {
		if (label != null) {
			for (Letters letter : values()) {
				if (label.equals(letter.label)) {
					return letter;
				}
			}
		}
		return null;
	}
}
