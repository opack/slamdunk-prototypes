package com.slamdunk.wordarena.words;

import java.util.HashMap;
import java.util.Map;

public class Node {
	public char letter;
	public Map<Character, Node> children;
	/**
	 * Représente le mot qui a ce noeud pour noeud final.
	 * On suppose ici qu'on part toujours de la racine.
	 */
	public String word;
	
	public Node(char letter) {
		this.letter = letter;
		children = new HashMap<Character, Node>();
		word = "";
	}
}
