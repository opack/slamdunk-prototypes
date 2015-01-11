package com.slamdunk.wordarena.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WordTree {
	private Node root;
	
	public WordTree() {
		root = new Node('#');
	}
	
	/**
	 * Ajoute un mot à l'arbre.
	 * @param word
	 * @param startIndex
	 */
	public void addWord(String word) {
		addWord(root, word, 0);
	}
	
	private void addWord(Node node, String word, int startIndex) {
		// Vérifie s'il reste des caractères à traiter
		if (startIndex >= word.length()) {
			// On est arrivés à la fin du mot. Ce noeud
			// représente donc le dernier caractère d'un
			// mot existant.
			node.word = word;
			return;
		}
		
		// Récupère le caractère à traiter
		char curChar = word.charAt(startIndex);
		
		// Récupère l'enfant de ce noeud qui a déjà ce caractère
		Node child = node.children.get(curChar);
		
		// S'il n'y en a pas, on en crée un
		if (child == null) {
			child = new Node(curChar);
			node.children.put(curChar, child);
		}
		
		// On délègue à l'enfant le soin de gérer le reste du mot
		addWord(child, word, startIndex + 1);
	}
	
	/**
	 * Charge tous les mots du fichier indiqué en supposant qu'il n'y
	 * a qu'un mot par ligne.
	 * @return true si le fichier a pu être lu et correctement chargé
	 */
	public boolean load(String filePath) {
		return load(filePath, 0);
	}
	
	/**
	 * Charge tous les mots du fichier indiqué en supposant qu'il n'y
	 * a qu'un mot par ligne.
	 * Seuls les mots d'une longueur supérieure à la taille indiquée
	 * sont ajoutés.
	 * @param filePath
	 * @param minWordLength Nb de caractères min. Les mots plus courts
	 * présents dans le fichier ne sont pas ajoutés. Si 0, aucune limite
	 * n'est utilisée.
	 * @return true si le fichier a pu être lu et correctement chargé
	 */
	public boolean load(String filePath, int minWordLength) {
		if (minWordLength < 0) {
			minWordLength = 0;
		}
		
		// Chargement des mots du fichier dans un Set
		// pour s'assurer qu'il n'y a pas de doublons
		Set<String> words = new HashSet<String>();
		FileHandle file = Gdx.files.internal(filePath);
		BufferedReader reader = new BufferedReader(file.reader("UTF-8"));
		String extracted = null;
		try {
			while ((extracted = reader.readLine()) != null) {
				words.add(extracted);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// Ajoute les mots dans l'arbre
		for (String word : words) {
			addWord(word);
		}
		return true;
	}

	/**
	 * Teste si le mot indiqué existe en entier
	 * @param string
	 */
	public boolean contains(String word) {
		return contains(root, word, 0);
	}
	
	private boolean contains(Node node, String word, int startIndex) {
		// Vérifie s'il reste des caractères à traiter
		if (startIndex >= word.length()) {
			// On est arrivé au bout du mot ! Il existe en
			// entier si ce noeud le confirme
			return node.word.equals(word);
		}
		
		// Récupère le caractère à traiter
		char curChar = word.charAt(startIndex);
		
		// Récupère l'enfant de ce noeud qui a déjà ce caractère
		Node child = node.children.get(curChar);
		
		// S'il n'y en a pas, le mot n'existe pas
		if (child == null) {
			return false;
		}
		
		// On délègue à l'enfant le soin de gérer le reste du mot
		return contains(child, word, startIndex + 1);
	}
	
	/**
	 * Teste s'il existe un mot commençant par le
	 * préfixe indiqué
	 * @param start
	 */
	public boolean containsStart(String start) {
		return containsStart(root, start, 0);
	}
	
	private boolean containsStart(Node node, String start, int startIndex) {
		// Vérifie s'il reste des caractères à traiter
		if (startIndex >= start.length()) {
			return true;
		}
		
		// Récupère le caractère à traiter
		char curChar = start.charAt(startIndex);
		
		// Récupère l'enfant de ce noeud qui a déjà ce caractère
		Node child = node.children.get(curChar);
		
		// S'il n'y en a pas, le mot n'existe pas
		if (child == null) {
			return false;
		}
		
		// On délègue à l'enfant le soin de gérer le reste du mot
		return containsStart(child, start, startIndex + 1);
	}
	
	/**
	 * Retourne une liste de mots qui commencent par le préfixe
	 * indiqué.
	 * @param start
	 * @param result Liste qui contiendra les mots trouvés. Cette liste
	 * n'est pas vidée avant l'ajout des résultats.
	 */
	public void getPossibleWords(String start, List<String> result) {
		getPossibleWords(root, start, 0, result);
	}
	
	private void getPossibleWords(Node node, String start, int startIndex, List<String> result) {
		// Si on n'est toujours pas arrivé au noeud contenant la dernière lettre,
		// on continue
		if (startIndex < start.length()) {
			// Récupère le caractère à traiter
			char curChar = start.charAt(startIndex);
			
			// Récupère l'enfant de ce noeud qui a déjà ce caractère
			Node child = node.children.get(curChar);
			
			// S'il n'y a pas d'enfant, alors il ne peut y avoir aucun mot
			if (child == null) {
				return;
			}
			
			// Continue de s'enfoncer dans l'arbre
			getPossibleWords(child, start, startIndex + 1, result);
		}
		
		// Si on est arrivé à la fin du mot start, alors on peut commencer à
		// remplir la liste des mots.
		// On y met tous les mots de ce noeud et des enfants
		if (node.word.equals(start)) {
			result.add(start);
		}
		// Pour chaque enfant on va chercher tous les mots possibles
		for (Node child : node.children.values()) {
			getPossibleWords(child, start + child.letter, startIndex + 1, result);
		}
	}
}
