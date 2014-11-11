package com.slamdunk.wordarena.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.toolkit.world.pathfinder.PathCursor;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

/**
 * Une unité qui va attaquer dès qu'une unité ennemie est à portée
 */
public class OffensiveUnit extends SimpleUnit {
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
	private int damage;
	
	/**
	 * La cible à attaquer
	 */
	private SimpleUnit target;
	
	public OffensiveUnit(GameScreen game, Factions faction, 
			int minRange, int maxRange, int damage, float attackInterval) {
		super(game, faction);
		this.damage = damage;
		this.attackInterval = attackInterval;
		
		rangeBounds = new Rectangle(
			getX() - maxRange,
			getY() - maxRange,
			getWidth() + maxRange * 2,
			getHeight() + maxRange * 2);
		enemyBounds = new Rectangle();
	}

	@Override
	protected void performAttack(float delta) {
		// Attend entre 2 frappes
		waitTime += delta;
		if (waitTime < attackInterval) {
			return;
		}
		
		// On a assez attendu ! RAZ temps d'attente
		waitTime -= attackInterval;
		
		// Frappe la cible
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
		
		// Vérifie s'il y a des ennemis à attaquer à portée
		List<SimpleUnit> nearbyEnemies = findAttackableEnemies();
		
		// S'il y en a un, on l'attaque
		if (nearbyEnemies != null && !nearbyEnemies.isEmpty()) {
			target = nearbyEnemies.get(0);
			waitTime = attackInterval; // On fait la première frappe directement
			setState(States.ATTACKING);
		}
	}
	
	@Override
	protected void handleEventReceiveDamage(SimpleUnit attacker, int damage) {
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
	private List<SimpleUnit> findAttackableEnemies() {
		// S'il n'y a pas de curseur, alors pas de position donc pas d'ennemis proches
		PathCursor pathCursor = getPathCursor();
		if (pathCursor == null) {
			return null;
		}
		// S'il n'y a pas d'ennemis, alors il n'y a personne à attaquer
		final Factions enemyFaction = Factions.enemyOf(getFaction());
		Collection<SimpleUnit> enemies = UnitManager.getInstance().getUnits(enemyFaction);
		if (enemies == null || enemies.isEmpty()) {
			return null;
		}
		
		// Ajustement de la portée
		rangeBounds.setPosition(getX(), getY());
		
		// On vérifie si chaque unité est sur une des positions dans la portée
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
	 * Indique si l'unité spécifiée est à portée de cette unité
	 * @param unit
	 * @return
	 */
	public boolean isInRange(SimpleUnit unit) {
		enemyBounds.set(unit.getX(), unit.getY(), unit.getWidth(), unit.getHeight());
		return enemyBounds.overlaps(rangeBounds);
	}
}
