package com.slamdunk.pixelkingdomadvanced.ai;

/**
 * Représente une intelligence artificielle, qui prend des décisions
 * en fonction de situations
 */
public interface AI {
	/**
	 * Fait penser et agir l'AI
	 * @param delta
	 */
	void act(float delta);
}
