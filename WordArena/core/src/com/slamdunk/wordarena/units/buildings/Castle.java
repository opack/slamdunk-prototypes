package com.slamdunk.wordarena.units.buildings;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.UnitManager;
import com.slamdunk.wordarena.units.Units;

/**
 * Représente un château : celui du joueur ou de l'ennemi.
 */
public class Castle extends SimpleUnit {

	private static final Animation ANIM_IDLE = AnimationFactory.create("textures/castle_idle.png", 8, 1, 0.0675f);
	static {
		ANIM_IDLE.setPlayMode(PlayMode.LOOP);
	}
	
	private Rectangle tmpEnemyBounds;
	private Rectangle tmpCastleBounds;
	private Collection<SimpleUnit> tmpEnemies;
	
	public Castle(GameScreen game) {
		super(game, Units.CASTLE);
		
		setHp(1);
		
		initAnimationRendering(128, 128);
		setAnimation(States.IDLE, Directions4.RIGHT, ANIM_IDLE);
		
		setState(States.IDLE);
		
		tmpEnemyBounds = new Rectangle();
		tmpCastleBounds = new Rectangle();
		tmpEnemies = new ArrayList<SimpleUnit>();
	}
	
	@Override
	public String toString() {
		return "Castle " + getHp() + "HP" + getPosition();
	}
	
	@Override
	protected void performIdle(float delta) {
		super.performIdle(delta);
		
		// Vérifie si une unité ennemie pénètre dans le château
		final Factions enemyFaction = Factions.enemyOf(getFaction());
		Collection<SimpleUnit> enemies = UnitManager.getInstance().getUnits(enemyFaction);
		if (enemies == null || enemies.isEmpty()) {
			return;
		}
		
		// On travaille sur une copie de la liste des unités car si une unité
		// doit être supprimée, on veut éviter une ConcurrentModificationException
		tmpEnemies.clear();
		tmpEnemies.addAll(enemies);
		
		// On vérifie si chaque unité est sur une des positions dans la portée.
		updateBounds(tmpCastleBounds);
		for (SimpleUnit enemy : tmpEnemies) {
			if (!enemy.isDead()) {
				enemy.updateBounds(tmpEnemyBounds);
				if (tmpEnemyBounds.overlaps(tmpCastleBounds)) {
					// L'ennemi pénètre le château !!! On perd 1 HP et on retire l'unité
					handleEventReceiveDamage(enemy, 1);
					enemy.onBuildingEntered(this);
					
					// Prévient le gestionnaire d'objectif pour voir si la partie est terminée
					getGame().getObjectiveManager().onBuildingAttacked(this, enemy);
				}
			}
		}
	}
}
