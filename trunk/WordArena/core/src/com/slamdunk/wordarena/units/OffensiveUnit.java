package com.slamdunk.wordarena.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.pathfinder.PathCursor;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

/**
 * Une unité qui va attaquer dès qu'une unité ennemie est à portée
 */
public class OffensiveUnit extends SimpleUnit {
	/**
	 * Portée minimale des attaques
	 */
	private final int minRange;
	
	/**
	 * Portée maximale des attaques
	 */
	private final int maxRange;
	
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
	
	private List<Point> attackablePositions;
	
	/**
	 * La cible à attaquer
	 */
	private SimpleUnit target;
	
	public OffensiveUnit(GameScreen game, Factions faction, 
			int minRange, int maxRange, int damage, float attackInterval) {
		super(game, faction);
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.damage = damage;
		this.attackInterval = attackInterval;
		attackablePositions = new ArrayList<Point>();
	}

	@Override
	protected void handleEventMovedOnePosition() {
		// L'unité s'est déplacée. On détermine les positions sur
		// lesquelles l'unité peut désormais attaquer.
		computeAttackablePositions();
		
		// Une fois le déplacement effectué, on cherche si un ennemi se trouve à portée
		List<SimpleUnit> nearbyEnemies = findAttackableEnemies();
		
		// S'il y en a un, on l'attaque
		if (nearbyEnemies != null && !nearbyEnemies.isEmpty()) {
			target = nearbyEnemies.get(0);
			waitTime = attackInterval; // On fait la première frappe directement
			setState(States.ATTACKING);
		}
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
		if (target.getHp() <= 0) {
			target = null;
			// La cible est morte, on poursuit le déplacement vers le château adverse
			setState(States.MOVING);
		}
	}
	
	@Override
	protected void handleEventReceiveDamage(SimpleUnit attacker, int damage) {
		// Gère les dégâts recçus
		super.handleEventReceiveDamage(attacker, damage);
		
		// Si on n'est pas mort, on se défend !
		if (getHp() > 0) {
			// Attaque l'assaillant s'il est à portée et qu'on n'est pas
			// déjà en train d'attaquer quelqu'un d'autre
			if (target == null
			&& attackablePositions.contains(attacker.getPosition())) {
				target = attacker;
				waitTime = attackInterval; // On fait la première frappe directement
				setState(States.ATTACKING);
			}
		}
	}

	/**
	 * Calcule la liste des positions qui sont à portée pour attaquer
	 */
	private void computeAttackablePositions() {
		final int curIndex = getPathCursor().getIndex();
		int minIndex = curIndex + minRange;
		int maxIndex = curIndex + maxRange;
		
		final Path path = getPathCursor().getPath();
		attackablePositions.clear();
		for (int index = minIndex; index <= maxIndex; index++) {
			attackablePositions.add(path.getPosition(index));
		}
	}

	/**
	 * Recherche les ennemis à portée. Ce sont ceux qui ne sont pas dans
	 * la même faction et sont situés sur des positions attaquables (entre
	 * l'unité et la fin du chemin et à une distance inférieure ou égale à
	 * range).
	 * @param range
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
		
		// On vérifie si chaque unité est sur une des positions dans la portée
		List<SimpleUnit> nearbyEnemies = new ArrayList<SimpleUnit>();
		Point position;
		for (SimpleUnit enemy : enemies) {
			position = enemy.getPosition();
			if (attackablePositions.contains(position)) {
				nearbyEnemies.add(enemy);
			}
		}
		return nearbyEnemies;
	}
}
