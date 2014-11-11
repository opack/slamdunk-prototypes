package com.slamdunk.wordarena.units;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

/**
 * Un projectile qui est lancé et suit sa course jusqu'à sortir de l'écran
 */
public class ProjectileUnit extends OffensiveUnit {
	private boolean launched;
	
	public ProjectileUnit(GameScreen game, Factions faction, Directions direction, int damage, float attackDelay) {
		super(game, faction, 0, 0, damage, attackDelay);
		setDirection(direction);
		launched = false;
	}
	
	@Override
	protected void performAttack(float delta) {
		super.performAttack(delta);
		
		// Une fois la cible touchée, le projectile disparaît.
		// On joue une animation de mort car le projectile peut
		// par exemple exploser à l'impact.
		setState(States.DYING);
	}
	
	@Override
	protected void performMove(float delta) {
		// Si le projectile n'a pas encore été lancé, on le déplace
		// jusqu'en dehors de l'écran
		if (!launched) {
			launched = true;
			Vector2 destination = new Vector2(getX(), getY());
			switch (getDirection()) {
			case UP:
				break;
			case DOWN:
				break;
			case LEFT:
				break;
			case RIGHT:
				break;
			}
			float timeToArrival = destination.dst(getX(), getY()) / getSpeed();
			addAction(Actions.moveTo(destination.x, destination.y, timeToArrival));
		}
		// Si le projectile a été lancé et que son déplacement est achevé,
		// alors il est hors de l'écran : on le détruit
		else if (getActions().size == 0) {
			setState(States.DYING);
		}
		
		// Si le projectile est en déplacement, on vérifie s'il
		// touche un ennemi
		if (getState() == States.MOVING) {
			searchAndAttackEnnemy();
		}
	}
}
