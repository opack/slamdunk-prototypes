package com.slamdunk.wordarena.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaire permettant de récupérer la valeur la plus
 * représentée dans une liste
 */
public class MaxValueFinder<T> {
	private Map<T, Integer> values;
	private T valueIfDraw;
	private T ignoredValue;
	
	public MaxValueFinder() {
		values = new HashMap<T, Integer>();
		valueIfDraw = null;
		ignoredValue = null;
	}

	public T getValueIfDraw() {
		return valueIfDraw;
	}

	public void setValueIfDraw(T valueIfDraw) {
		this.valueIfDraw = valueIfDraw;
	}

	public void setIgnoredValue(T ignoredValue) {
		this.ignoredValue = ignoredValue;
	}
	
	public void reset() {
		values.clear();
	}
	
	public void addValue(T value, int weight) {
		if (value == null || value.equals(ignoredValue)) {
			return;
		}
		Integer count = values.get(value);
		if (count == null) {
			values.put(value, weight);
		} else {
			values.put(value, count + weight);
		}
	}
	
	public void addValue(T value) {
		addValue(value, 1);
	}

	/**
	 * Retourne l'indice de la valeur maximale dans le tableau
	 * fourni en paramètre
	 * @param values
	 * @return valueIfDraw en cas d'égalité
	 */
	public T getMaxValue() {
		T result = valueIfDraw;
		int max = -1;
		int weight;
		for (Map.Entry<T, Integer> value : values.entrySet()) {
			weight = value.getValue();
			if (weight == max) {
				// Egalité
				result = valueIfDraw;
			} else 	if (weight > max) {
				// Nouveau max
				max = weight;
				result = value.getKey();
			}
		}
		return result;
	}
}
