package com.slamdunk.wordarena.old.letters;

public enum Letters {
	A(9, 1, "A"),
	B(2, 3, "B"),
	C(2, 3, "C"),
	D(3, 2, "D"),
	E(15, 1, "E"),
	F(2, 4, "F"),
	G(2, 2, "G"),
	H(2, 4, "H"),
	I(8, 1, "I"),
	J(1, 8, "J"),
	K(1, 10, "K"),
	L(5, 1, "L"),
	M(3, 2, "M"),
	N(6, 1, "N"),
	O(6, 1, "O"),
	P(2, 3, "P"),
	Q(1, 8, "Q"),
	R(6, 1, "R"),
	S(6, 1, "S"),
	T(6, 1, "T"),
	U(6, 1, "U"),
	V(2, 4, "V"),
	W(1, 10, "W"),
	X(1, 10, "X"),
	Y(1, 10, "Y"),
	Z(1, 10, "Z"),
	JOKER(2, 0, "?");
	
	public int representation;
	public int points;
	public String label;
	
	Letters(int representation, int points, String label) {
		this.representation = representation;
		this.points = points;
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
