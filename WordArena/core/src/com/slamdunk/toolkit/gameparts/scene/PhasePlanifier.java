package com.slamdunk.toolkit.gameparts.scene;

/**
 * Objet chargé de déterminé quand il est temps d'appeler la méthode
 * physics() et la méthode update()
 */
public class PhasePlanifier {
	/**
	 * Nombre de màj de la physique qu'on souhaite seconde.
	 * Cela détermine quand physicsTick() renvoie true.
	 */
	public float physicsRate;
	
	/**
	 * Nombre de frames qu'on souhaite afficher par seconde.
	 * Cela détermine quand frameTick() renvoie true.
	 */
	public float frameRate;
	
	/**
	 * Met à jour les compteurs du planificateurs
	 * en fonction du temps qui s'est écoulé
	 * @param rawDeltaTime
	 */
	public void update(float rawDeltaTime) {
	}
	
	/**
	 * Indique s'il est temps de mettre à jour la physique,
	 * ce qui implique l'appel à physics()
	 * @return
	 */
	public boolean physicsTick() {
		return false;
	}
	
	/**
	 * Retourne le temps écoulé depuis le dernier physicsTick
	 * @return
	 */
	public float getPhysicsDeltaTime() {
		return 0;
	}

	/**
	 * Indique s'il est temps de redessiner une nouvelle
	 * frame, ce qui implique l'appel à update() puis render()
	 * @return
	 */
	public boolean frameTick() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Retourne le temps écoulé depuis le dernier frameTick
	 * @return
	 */
	public float getFrameDeltaTime() {
		return 0;
	}
}
