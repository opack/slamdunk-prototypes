package com.slamdunk.toolkit.gameparts.scene;

import com.badlogic.gdx.math.WindowedMean;

public class TimeAccumulator {
	public float tickTime;
	
	private float elapsedTime;
	private float deltaTime;
	private float lastTickCall;
	private float lastTickTime;
	private WindowedMean workTime;
	
	public TimeAccumulator() {
		workTime = new WindowedMean(5);
	}
	
	public void accumulate(float time) {
		elapsedTime += time;
	}
	
	public boolean tick() {
		// Met à jour le temps de travail, estimé
		// comme étant le temps entre 2 appels à tick()
		workTime.addValue(elapsedTime - lastTickCall);
		lastTickCall = elapsedTime;
		
		// Détermine si on a passé le prochain tick
		if (elapsedTime >= tickTime) {
			deltaTime = elapsedTime - lastTickTime;
			elapsedTime -= tickTime;
			lastTickTime = elapsedTime;
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne le temps écoulé depuis le dernier tick
	 * (décompté depuis le dernier appel à tick() retournant
	 * true)
	 * @return
	 */
	public float getDeltaTime() {
		return deltaTime;
	}
	
	/**
	 * Retourne le temps restant avant le prochain tick
	 * @return
	 */
	public float getTimeToTick() {
		return tickTime - elapsedTime;
	}
	
	/**
	 * Retourne le temps de travail, càd le temps entre 2
	 * appels à tick(), quelle que soit la valeur retournée
	 * par cette méthode.
	 * @return Le temps moyen entre 2 appels à tick(),
	 * ou 0 s'il n'y a pas encore eut assez d'appels pour
	 * calculer ce temps
	 */
	public float getWorkTime() {
		return workTime.getMean();
	}
}
