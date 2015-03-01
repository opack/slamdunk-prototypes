package com.slamdunk.toolkit.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * DoubleEntryArray dont les valeurs sont des listes
 */
public class DoubleEntryArrayList<Entry1Type, Entry2Type, ValueType> extends
		DoubleEntryArray<Entry1Type, Entry2Type, List<ValueType>> {
	
	/**
	 * Ajoute une valeur à la liste correspondant aux entrées indiquées
	 * 
	 * @param entry1
	 * @param entry2
	 * @param value
	 */
	public void add(Entry1Type entry1, Entry2Type entry2, ValueType value) {
		List<ValueType> list = get(entry1, entry2);
		if (list == null) {
			list = new ArrayList<ValueType>();
			put(entry1, entry2, list);
		}
		list.add(value);
	}
}
