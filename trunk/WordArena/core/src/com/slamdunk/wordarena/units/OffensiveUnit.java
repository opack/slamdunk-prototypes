package com.slamdunk.wordarena.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;

/**
 * Une unité qui va attaquer dès qu'une unité ennemie est à portée
 */
public class OffensiveUnit extends SimpleUnit {
	
	/**
	 * Portée minimale
	 */
	private float rangeMin;
	
	/**
	 * Portée maximale
	 */
	private float rangeMax;
	
	/**
	 * La zone dans laquelle l'unité peut attaquer
	 */
	private Rectangle rangeBounds;
	
	/**
	 * Rectangle de travail pour mettre à jour la position de l'ennemi
	 */
	private Rectangle enemyBounds;
	
	/**
	 * Interval de temps entre 2 attaques (en secondes)
	 */
	private float attackInterval;
	
	/**
	 * Temps d'attente entre 2 attaques
	 */
	private float waitTime;
	
	/**
	 * Dégâts causés à chaque attaque
	 */
	private float damage;
	
	/**
	 * La cible à attaquer
	 */
	private SimpleUnit target;
	
	public OffensiveUnit(GameScreen game) {
		super(game);
		// Par défaut on cause 1 point de dégât
		damage = 1;
		// Par défaut on attaque 1 fois par seconde
		attackInterval = 1;
		
		rangeBounds = new Rectangle();
		enemyBounds = new Rectangle();
	}
	
	public void setRange(float min, float max) {
		rangeMin = min;
		rangeMax = max;
	}
	
	/**
	 * Met à jour la position et la dimension du rectangle de bounds
	 * qui permet de déterminée si une unité ennemie est à portée
	 * d'attaque.
	 * La portée est évaluée en face de l'unité. Les propriétés du
	 * rectangle dépendent donc de la direction vers laquelle fait
	 * face l'unité.
	 */
	protected void updateRangeBounds() {
		final float rangeLength = rangeMax - rangeMin;
		switch (getDirection()) {
		case UP:
			rangeBounds.x = getX();
			rangeBounds.y = getY() + getHeight() + rangeMin;
			rangeBounds.width = 1; // 1 case de large
			rangeBounds.height = rangeLength;
			break;
		case DOWN:
			rangeBounds.x = getX();
			rangeBounds.y = getY() - rangeMin - rangeMax;
			rangeBounds.width = 1; // 1 case de large
			rangeBounds.height = rangeLength;
			break;
		case LEFT:
			rangeBounds.x = getX() - rangeMin - rangeMax;
			rangeBounds.y = getY();
			rangeBounds.width = rangeLength;
			rangeBounds.height = 1; // 1 case de large
			break;
		case RIGHT:
			rangeBounds.x = getX() + getWidth() + rangeMin;
			rangeBounds.y = getY();
			rangeBounds.width = rangeLength;
			rangeBounds.height = 1; // 1 case de large
			break;
		}
	}
	
	public float getAttackInterval() {
		return attackInterval;
	}

	public void setAttackInterval(float attackInterval) {
		this.attackInterval = attackInterval;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public SimpleUnit getTarget() {
		return target;
	}

	public void setTarget(SimpleUnit target) {
		this.target = target;
	}

	@Override
	protected void performAttack(float delta) {
		// Si on vient d'entrer en ATTACKING, alors on attaque
		// directement sans attendre
		if (getPreviousState() != States.ATTACKING) {
			waitTime = attackInterval;
		}
		
		// Attend entre 2 frappes
		waitTime += delta;
		if (waitTime < attackInterval) {
			return;
		}
		
		// On a assez attendu ! RAZ temps d'attente
		waitTime -= attackInterval;
		
		// Frappe la cible
		performHit();
	}
	
	/**
	 * Frappe effectivement la cible (ou envoie un projectile le cas échéant).
	 * Cette méthode n'est appelée qu'après l'intervalle d'attente entre 2
	 * frappes et effectue les actions liées à la frappe.
	 */
	protected void performHit() {
		target.handleEventReceiveDamage(this, damage);
		if (target.isDead()) {
			target = null;
			// La cible est morte, on poursuit le déplacement vers le château adverse
			setState(States.MOVING);
		}
	}
	
	@Override
	protected void performMove(float delta) {
		super.performMove(delta);
		
		// Cherche un ennemi à portée et l'attaque le cas échéant
		searchAndAttackEnnemy();
	}
	
	/**
	 * Recherche un ennemi à portée et l'attaque
	 * @return true si un ennemi attaquable a été trouvé
	 */
	protected boolean searchAndAttackEnnemy() {
		// Vérifie s'il y a des ennemis à attaquer à portée
		List<SimpleUnit> nearbyEnemies = findAttackableEnemies();
		
		// S'il y en a un, on l'attaque
		if (nearbyEnemies != null && !nearbyEnemies.isEmpty()) {
			target = nearbyEnemies.get(0);
			setState(States.ATTACKING);
			return true;
		}
		
		return false;
	}

	@Override
	protected void handleEventReceiveDamage(SimpleUnit attacker, float damage) {
		// Gère les dégâts recçus
		super.handleEventReceiveDamage(attacker, damage);
		
		// Si on n'est pas mort, on se défend !
		if (!isDead()) {
			// Attaque l'assaillant s'il est à portée et qu'on n'est pas
			// déjà en train d'attaquer quelqu'un d'autre
			if (target == null
			&& isInRange(attacker)) {
				target = attacker;
				waitTime = attackInterval; // On fait la première frappe directement
				setState(States.ATTACKING);
			}
		}
	}

	/**
	 * Recherche les ennemis à portée. Ce sont ceux qui ne sont pas dans
	 * la même faction et sont situés sur des positions attaquables (entre
	 * l'unité et la fin du chemin et à une distance inférieure ou égale à
	 * range).
	 * @param rangeBounds
	 * @return
	 */
	protected List<SimpleUnit> findAttackableEnemies() {
		// S'il n'y a pas d'ennemis, alors il n'y a personne à attaquer
		final Factions enemyFaction = Factions.enemyOf(getFaction());
		Collection<SimpleUnit> enemies = UnitManager.getInstance().getUnits(enemyFaction);
		if (enemies == null || enemies.isEmpty()) {
			return null;
		}
		
		// Ajustement de la portée
		updateRangeBounds();
		
		// On vérifie si chaque unité est sur une des positions dans la portée.
		List<SimpleUnit> nearbyEnemies = new ArrayList<SimpleUnit>();
		for (SimpleUnit enemy : enemies) {
			if (!enemy.isDead()
			&& isInRange(enemy)) {
				nearbyEnemies.add(enemy);
			}
		}
		return nearbyEnemies;
	}
	
	/**
	 * Indique si l'unité spécifiée est à portée de cette unité.
	 * On s'assure aussi que cette position est sur le chemin de l'unité
	 * pour éviter qu'elle n'attaque des ennemis sur des chemins voisins.
	 * @param unit
	 * @return
	 */
	public boolean isInRange(SimpleUnit unit) {
		enemyBounds.set(unit.getX(), unit.getY(), unit.getWidth(), unit.getHeight());
		return enemyBounds.overlaps(rangeBounds)
		// Soit l'unité n'est pas sur un chemin (typiquement pour les
		// projectiles) soit elle est sur un chemin et on s'assure que
		// l'ennemi est sur le trajet de l'unité, sans quoi il est
		// interdit qu'elle l'attaque
		&& (getPath() == null || getPath().contains(unit.getPosition()));
	}
}
