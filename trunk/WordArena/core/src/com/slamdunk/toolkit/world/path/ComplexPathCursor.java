package com.slamdunk.toolkit.world.path;

import com.badlogic.gdx.math.Vector2;

/**
 * Un curseur qui se déplace sur les chemins d'un ComplexPath
 * ATTENTION ! Il y a une limitation avec les courbes de Bézier cubiques :
 * le déplacement fait avec move() ne se fait pas à vitesse constante. Comme
 * un incrément constant de t ne donne pas des points séparés par la même
 * distance, la vitesse n'est pas contrôlée. Ex : si valueAt(t=0.1) et
 * valueAt(t=0.2) retournent des positions séparés de 50 pixels, rien ne
 * garantit que valueAt(t=0.2) et valueAt(t=0.3) retourneront aussi des points
 * espacés de 50 pixels. Suivant le profil de la courbe, cet écart peut être
 * plus ou moins élevé. Il n'y a pas de moyen simple et rapide (coût CPU) pour
 * trouver la valeur t qui permet d'obtenir le point de la courbe situé à
 * x pixels de la position actuelle. Le workaround le plus simple consiste
 * à n'utiliser que des chemins linéaires, donc des Béziers avec 2 points.
 * La méthode PathUtils.simplify() permet de créer des chemins linéaires
 * à partir d'une courbe de bézier.
 */
public class ComplexPathCursor {
	/**
	 * Chemin sur lequel se déplace le curseur
	 */
	private ComplexPath path;
	
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
	
	public ComplexPathCursor(ComplexPath path, float speed, CursorMode mode) {
		this.path = path;
		this.speed = speed;
		this.mode = mode;
		reset();
	}
	
	public ComplexPathCursor(ComplexPath path, float speed) {
		this(path, speed, CursorMode.FORWARD);
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
	 * Change le mode et éventuellement la direction du curseur
	 * pour qu'il se dirige vers l'extrémité du chemin nommée
	 * comme le paramètre endName
	 */
	public boolean setDestination(String extremityName) {
		float destinationT = PathUtils.getExtremityByName(path, extremityName);
		if (destinationT == -1) {
			return false;
		}
		
		// Le curseur se dirige vers l'extrémité en t=0
		if (destinationT == 0) {
			// Place le curseur dans le bon sens
			direction = -1;
			
			// Change le mode pour s'accorder avec la nouvelle direction
			switch (mode) {
			case FORWARD:
				mode = CursorMode.BACKWARD;
				break;
			case LOOP_FORWARD:
				mode = CursorMode.LOOP_BACKWARD;
				break;
			case BACKWARD:
			case LOOP_BACKWARD:
			case LOOP_PINGPONG:
				// Rien à faire pour les autres modes car on se dirige déjà
				// dans la bonne direction
				break;
			}
		}
		// Le curseur se dirige vers l'extrémité en t=1
		else {
			// Place le curseur dans le bon sens
			direction = 1;
			
			// Change le mode pour s'accorder avec la nouvelle direction
			switch (mode) {
			case BACKWARD:
				mode = CursorMode.FORWARD;
				break;
			case LOOP_BACKWARD:
				mode = CursorMode.LOOP_FORWARD;
				break;
			case FORWARD:
			case LOOP_FORWARD:
			case LOOP_PINGPONG:
				// Rien à faire pour les autres modes car on se dirige déjà
				// dans la bonne direction
				break;
			}
		}
		
		return true;
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

	public ComplexPath getPath() {
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
		// En FORWARD, LOOP_FORWARD et LOOP_PINGPONG, on commence au début et on va vers la fin du chemin.
		case FORWARD:
		case LOOP_FORWARD:
		case LOOP_PINGPONG:
			position = 0;
			currentSegmentIndex = 0;
			direction = 1;
			break;
		// En BACKWARD et LOOP_BACKWARD, on commence de la fin et on va vers le début du chemin.
		case BACKWARD:
		case LOOP_BACKWARD:
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
	 * @param delta
	 */
	public void move(float delta) {
		move(delta, null);
	}

	/**
	 * Déplace l'objet sur le chemin en le positionnant
	 * là où se trouve en fonction du temps écoulé
	 * @param delta
	 * @param newPosition Coordonnées de la nouvelle position après le
	 * déplacement ; équivaut à un appel à valueAt()
	 */
	public void move(float delta, Vector2 newPosition) {
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
				case FORWARD:
					position = 1;
					currentSegmentIndex = path.size - 1;
					direction = 0;
					return;
				// On ne fait rien d'autre, on s'arrête au début du chemin
				case BACKWARD:
					position = 0;
					currentSegmentIndex = 0;
					direction = 0;
					return;
				// Retour au début et on continue à parcourir le chemin
				case LOOP_FORWARD:
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
				case LOOP_BACKWARD:
					currentSegmentIndex = path.size - 1;
				}
			}
			
			// Adapte le temps de parcours du segment pour que la vitesse
			// reste constante
			segmentTime = path.getLength(currentSegmentIndex) / speed;
			
			// La position est remise entre les bornes d'un chemin normal
			position -= direction;
		}
		
		// Met à jour le vecteur contenant la position, s'il est fourni
		if (newPosition != null) {
			valueAt(newPosition);
		}
	}

	/**
	 * Remplit result avec la valeur du segment correspondant
	 * à la position curseur.
	 * Identique à {@link ComplexPath.valueAt}
	 * @param result
	 */
	public void valueAt(Vector2 result) {
		path.valueAt(currentSegmentIndex, position, result);
	}
	
	/**
	 * Indique si le curseur est arrivé au bout du chemin.
	 * Attention ! Cette méthode n'a pas de sens et retournera toujours
	 * false si le curseur est en mode LOOP_FORWARD, LOOP_BACKWARD ou LOOP_PINGPONG.
	 * @return
	 */
	public boolean isArrivalReached() {
		return (mode == CursorMode.FORWARD && position == 1)
			|| (mode == CursorMode.BACKWARD && position == 0);
	}

	/**
	 * Place le curseur à l'extrémité du chemin dont le nom est spécifié
	 * @param string
	 */
	public void setPosition(String extremityName) {
		float newPosition = PathUtils.getExtremityByName(path, extremityName);
		if (newPosition == -1) {
			return;
		}
		
		this.position = newPosition;
	}
}
