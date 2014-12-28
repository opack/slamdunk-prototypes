package com.slamdunk.toolkit.gameparts.scene;

/**
 * Objet chargé de déterminé quand il est temps d'appeler la méthode
 * physics() et la méthode update()
 */
public class PhasePlanifier {
	private TimeAccumulator physicsTimer;
	private TimeAccumulator frameTimer;
	
	/**
	 * 
	 * @param physicsRate Nombre de màj de la physique qu'on souhaite seconde.
	 * Cela détermine quand physicsTick() renvoie true.
	 * @param frameRate Nombre de frames qu'on souhaite afficher par seconde.
	 * Cela détermine quand frameTick() renvoie true.
	 */
	public PhasePlanifier(float physicsRate, float frameRate) {
		physicsTimer = new TimeAccumulator();
		physicsTimer.tickTime = 1 / physicsRate;
		
		frameTimer = new TimeAccumulator();
		frameTimer.tickTime = 1 / frameRate;
	}
	
	/**
	 * Met à jour les compteurs du planificateurs
	 * en fonction du temps qui s'est écoulé
	 * @param rawDeltaTime
	 */
	public void update(float rawDeltaTime) {
		physicsTimer.accumulate(rawDeltaTime);
		frameTimer.accumulate(rawDeltaTime);
	}
	
	/**
	 * Indique s'il est temps de mettre à jour la physique,
	 * ce qui implique l'appel à physics().
	 * Cette méthode prend aussi en compte le temps de
	 * travail des calculs physiques de façon à ne pas
	 * faire un tick() s'il ne reste pas assez de temps
	 * avant le prochain frameTick
	 * @return
	 */
	public boolean physicsTick() {
		return physicsTimer.tick()
			/*&& physicsTimer.getWorkTime() < frameTimer.getTimeToTick()*/;
	}
	
	/**
	 * Retourne le temps écoulé depuis le dernier physicsTick
	 * @return
	 */
	public float getPhysicsDeltaTime() {
		return physicsTimer.getDeltaTime();
	}

	/**
	 * Indique s'il est temps de redessiner une nouvelle
	 * frame, ce qui implique l'appel à update() puis render()
	 * @return
	 */
	public boolean frameTick() {
		return frameTimer.tick();
	}

	/**
	 * Retourne le temps écoulé depuis le dernier frameTick
	 * @return
	 */
	public float getFrameDeltaTime() {
		return frameTimer.getDeltaTime();
	}
}
