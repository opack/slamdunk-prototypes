package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Vector;


/**
 * Un curseur qui se déplace sur les chemins d'un PathList
 */
public class PathListCursor<T extends Vector<T>> {
	/**
	 * Chemin sur lequel se déplace le curseur
	 */
	private PathList<T> path;
	
	/**
	 * Indice du chemin courant dans la liste
	 */
	private int currentSegmentIndex;
	
	/** TODO DBG Ceci est la vitesse que met le curseur à parcourir un segment du chemin.
	 * C'est déjà pas terrible sur un chemin, mais lorsqu'on a plusieurs segments c'est
	 * carrément nul car l'objet va plus vite sur les longs segments car le temps de
	 * parcours est constant, pas la vitesse. Il faut donc changer la gestion de la vitesse
	 * pour que ce soit bien la vitesse qui soit constante.
	 */
	/**
	 * Vitesse de déplacement du curseur
	 */
	private float speed;
	
	/**
	 * Le temps que mettra le curseur à parcourir le segment courant du chemin
	 * en fonction de la vitesse
	 */
	private float segmentTime;
	
	/**
	 * Indique la position du curseur sur le chemin (entre 0.0f et 1.0f)
	 */
	private float position;
	
	/**
	 * Nombre de fois qu'on a fait le tour du chemin
	 */
	private int laps;
	
	/**
	 * Mode de boucle, qui indique où il faut aller une fois que le chemin
	 * est fini
	 */
	private CursorMode mode;
	
	/**
	 * 1 ou -1, pour indiquer si on va du début vers la fin du chemin ou l'inverse
	 */
	private float direction;
	
	public PathListCursor(PathList<T> path, float speed, CursorMode mode) {
		this.path = path;
		this.speed = speed;
		this.mode = mode;
		reset();
	}
	
	public PathListCursor(PathList<T> path, float speed) {
		this(path, speed, CursorMode.NORMAL);
	}
	
	public CursorMode getMode() {
		return mode;
	}

	public void setMode(CursorMode mode) {
		this.mode = mode;
	}

	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Vitesse exprimée en pixels par seconde
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public int getCurrentSegmentIndex() {
		return currentSegmentIndex;
	}

	public void setCurrentSegmentIndex(int current) {
		this.currentSegmentIndex = current;
	}

	public float getPosition() {
		return position;
	}
	
	/**
	 * Définit la position actuelle du curseur sur le chemin courant.
	 * Borné entre 0 et 1.
	 * @param position
	 */
	public void setPosition(float position) {
		if (position <= 0) {
			position = 0;
		} else if (position > 1) {
			position = 1;
		}
		
		this.position = position;
	}

	public PathList<T> getPath() {
		return path;
	}

	public int getLaps() {
		return laps;
	}
	
	/**
	 * Réinitialise le curseur et le replace au début du chemin
	 */
	public void reset() {
		switch (mode) {
		// En NORMAL, LOOP et LOOP_PINGPONG, on commence au début et on va vers la fin du chemin.
		case NORMAL:
		case LOOP:
		case LOOP_PINGPONG:
			position = 0;
			currentSegmentIndex = 0;
			direction = 1;
			break;
		// En REVERSED et LOOP_REVERSED, on commence de la fin et on va vers le début du chemin.
		case REVERSED:
		case LOOP_REVERSED:
			position = 1;
			currentSegmentIndex = path.size - 1;
			direction = -1;
			break;
		}
		laps = 0;
		segmentTime = path.getLength(currentSegmentIndex) / speed;
	}

	/**
	 * Déplace l'objet sur le chemin en le positionnant
	 * là où se trouve en fonction du temps écoulé
	 * @param actor
	 * @param delta
	 */
	public void move(float delta) {
		// Si on a fini le parcours du chemin ou qu'il ne s'est écoulé aucun
		// temps, alors on ne se déplace pas
		if (direction == 0 || delta == 0) {
			return;
		}
		// Avance en fonction de la direction, du temps écoulé
		// et du temps à parcourir sur le segment
		position += direction * (delta / segmentTime);
		
		// Passe au chemin suivant
		while (position < 0 || position >= 1f) {
			// Passe au chemin suivant dans la liste
			currentSegmentIndex += (int)direction;
			if (currentSegmentIndex < 0 || currentSegmentIndex >= path.size) {
				// On a fait 1 tour !
				laps++;
				// Suivant le mode de parcours, on revient au début ou pas
				switch (mode) {
				// On ne fait rien d'autre, on s'arrête à la fin du chemin
				case NORMAL:
					position = 1;
					currentSegmentIndex = path.size - 1;
					direction = 0;
					return;
				// On ne fait rien d'autre, on s'arrête au début du chemin
				case REVERSED:
					position = 0;
					currentSegmentIndex = 0;
					direction = 0;
					return;
				// Retour au début et on continue à parcourir le chemin
				case LOOP:
					currentSegmentIndex = 0;
					break;
				// On repart dans l'autre sens
				case LOOP_PINGPONG:
					position = direction + 1 - position;
					direction *= -1;
					currentSegmentIndex += (int)direction;
					segmentTime = path.getLength(currentSegmentIndex) / speed;
					return;
				// Retour à la fin et on continue à parcourir le chemin
				case LOOP_REVERSED:
					currentSegmentIndex = path.size - 1;
				}
			}
			
			// Adapte le temps de parcours du segment pour que la vitesse
			// reste constante
			segmentTime = path.getLength(currentSegmentIndex) / speed;
			
			// La position est remise entre les bornes d'un chemin normal
			position -= direction;
		}
	}
}
