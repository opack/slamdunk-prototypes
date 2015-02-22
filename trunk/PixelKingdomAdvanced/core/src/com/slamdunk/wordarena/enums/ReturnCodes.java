package com.slamdunk.wordarena.enums;

public enum ReturnCodes {
	/**
	 * Opération effectuée
	 */
	OK,
	
	/**
	 * Mot inconnu dans le dictionnaire
	 */
	WORD_UNKNOWN,
	
	/**
	 * Mot déjà joué au cours de ce round
	 */
	WORD_ALREADY_PLAYED;
}
