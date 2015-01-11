package com.slamdunk.wordarena.letters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LetterGenerator {
	public static List<Letters> generate(int nbLetters) {
		// Crée une pioche de lettre en respectant la distribution des lettres
		// de l'énumération Letters
		List<Letters> letters = new ArrayList<Letters>();
		do {
			for (Letters letter : Letters.values()) {
				for (int count = 0; count < letter.representation; count++) {
					letters.add(letter);
				}
			}
		} while (letters.size() < nbLetters);
		
		// Mélance la pioche et tire autant de lettres que demandé
		Collections.shuffle(letters);
		List<Letters> selected = new ArrayList<Letters>();
		for (int count = 0; count < nbLetters; count++) {
			selected.add(letters.get(count));
		}
		return selected;
	}
}