package com.slamdunk.toolkit.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Représente un jeu de T.
 * L'intérêt est de le coupler à un générateur.
 * On peut alors itérer sur ce deck pour récupérer la valeur suivante,
 * et lorsque le deck est fini, l'ensemble est de nouveau mélangé
 * et une nouvelle valeur peut être tirée.
 */
public class Deck<T extends Deckable> implements Iterable<T>{
	private List<T> uniqueValues;
	private List<T> deckValues;
	
	/**
	 * Nombre de pioches qu'on met dans cette pioche.
	 * Pour un jeu de cartes normal, si on met 1 seul
	 * deck on aura 1 seul roi de coeur. Si on met
	 * 2, on aura 2 rois de coeur et ainsi de suite.
	 */
	private int nbDecks;
	
	public Deck(List<T> uniqueValues, int nbDecks) {
		if (uniqueValues == null || uniqueValues.isEmpty()) {
			throw new IllegalArgumentException("uniqueValues must not be null nor empty");
		}
		this.uniqueValues = uniqueValues;
		
		if (nbDecks < 1) {
			throw new IllegalArgumentException("nbDecks must be > 0");
		}
		this.nbDecks = nbDecks;
		deckValues = new ArrayList<T>(nbDecks * uniqueValues.size());
	}
	
	public Deck(T[] uniqueValues, int nbDecks) {
		if (uniqueValues == null || uniqueValues.length == 0) {
			throw new IllegalArgumentException("uniqueValues must not be null nor empty");
		}
		this.uniqueValues = Arrays.asList(uniqueValues);
		
		if (nbDecks < 1) {
			throw new IllegalArgumentException("nbDecks must be > 0");
		}
		this.nbDecks = nbDecks;
		deckValues = new ArrayList<T>(nbDecks * uniqueValues.length);
	}
	
	/**
	 * Réinitialise le tas
	 */
	public void reset() {
		// Remplit le tas
		deckValues.clear();
		int representation;
		for (int curDeck = 0; curDeck < nbDecks; curDeck++) {
			for (T value : uniqueValues) {
				representation = value.countInOneDeck();
				for (int count = 0; count < representation; count++) {
					deckValues.add(value);
				}
			}
		}
		
		// Mélange le tas
		shuffle();
	}
	
	public void shuffle() {
		Collections.shuffle(deckValues);
	}
	
	/**
	 * Pioche un certain nombre de valeurs.
	 * @param resetIfEmpty Si true, la pioche est réinitialisée
	 * s'il n'est plus possible de tirer une carte.
	 */
	public List<T> draw(int count, boolean resetIfEmpty) {
		List<T> selected = new ArrayList<T>();
		for (int drawn = 0; drawn < count; drawn++) {
			selected.add(draw(resetIfEmpty));
		}
		return selected;
	}
	
	/**
	 * Pioche une valeurs. Comme dans une pioche, c'est la dernière
	 * valeur ajoutée (donc celle en haut du tas) qui est piochée.
	 * @param resetIfEmpty Si true, la pioche est réinitialisée
	 * s'il n'est plus possible de tirer une carte.
	 */
	public T draw(boolean resetIfEmpty) {
		if (deckValues.isEmpty()) {
			reset();
		}
		return deckValues.remove(deckValues.size() - 1);
	}
	
	/**
	 * Pioche une valeurs. Comme dans une pioche, c'est la dernière
	 * valeur ajoutée (donc celle en haut du tas) qui est piochée.
	 * La pioche est réinitialisée s'il n'est plus possible de tirer
	 * une carte.
	 */
	public T draw() {
		return draw(true);
	}
	
	@Override
	public Iterator<T> iterator() {
		return deckValues.iterator();
	}
}
