package com.slamdunk.wordarena.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;

/**
 * Une unité qui va attaquer dès qu'une unité ennemie est à portée
 */
public class OffensiveUnit extends SimpleUnit {
	
	/**
	 * Portée
	 */
	private Circle range;
	
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
	
	/**
	 * Variable de travail utilisée pour calculer les bounds de l'ennemi
	 */
	private Rectangle tmpEnemyBounds;
	
	public OffensiveUnit(GameScreen game, Units type) {
		super(game, type);
		// Par défaut on cause 1 point de dégât
		damage = 1;
		// Par défaut on attaque 1 fois par seconde
		attackInterval = 1;
		// Par défaut, on a une portée de 0
		range = new Circle();
		// Initialisation du rectangle de travail
		tmpEnemyBounds = new Rectangle();
	}
	
	protected Circle getRange() {
		return range;
	}
	
	public void setRange(float min, float max) {
		range.radius = max;
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
		
		// Met à jour la portée de détection
		range.setPosition(getCenterX(), getCenterY());
		
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
	 * @param target
	 * @return
	 */
	public boolean isInRange(SimpleUnit target) {
		// Si l'unité est sur un chemin et qu'il est différent de celui
		// de la cible, alors la cible est considérée comme étant hors
		// de portée.
		// Si l'unité n'est pas sur un chemin (typiquement pour les
		// projectiles), alors on continue.
		if (getPath() != null
		&& getPath() != target.getPath()) {
			return false;
		}
		
		// L'ennemi doit être à portée
		target.updateBounds(tmpEnemyBounds);
		return Intersector.overlaps(range, tmpEnemyBounds);
	}
}
