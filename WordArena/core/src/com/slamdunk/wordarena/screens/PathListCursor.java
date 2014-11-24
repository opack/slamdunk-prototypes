package com.slamdunk.wordarena.screens;


/**
 * Un curseur qui se déplace sur les chemins d'un PathList
 */
public class PathListCursor<T> {
	/**
	 * Chemin sur lequel se déplace le curseur
	 */
	private PathList<T> path;
	
	/**
	 * Indice du chemin courant dans la liste
	 */
	private int current;
	
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
	
	public PathListCursor(PathList<T> path) {
		this.path = path;
	}
	
	public PathListCursor(PathList<T> path, float speed) {
		this(path);
		this.speed = speed;
		reset();
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
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
		current = 0;
		laps = 0;
		position = 0;
		segmentTime = path.getLength(current) / speed;
	}

	/**
	 * Déplace l'objet sur le chemin en le positionnant
	 * là où se trouve en fonction du temps écoulé
	 * @param actor
	 * @param delta
	 */
	public void move(float delta) {
		position += segmentTime * delta;
		while (position >= 1f) {
			// Passe au chemin suivant dans la liste
			current++;
			if (current >= path.size) {
				// On a fait 1 tour ! On revient au début
				laps++;
				current = 0;
			}
			// Adapte le temps de parcours du segment pour que la vitesse
			// reste constante
			segmentTime = path.getLength(current) / speed;
			
			// La position est remise entre les bornes d'un chemin normal
			position -= 1f;
		}
	}
}
