package com.slamdunk.wordarena.units.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.ProjectileUnit;

public class Arrow extends ProjectileUnit {
	private static final Animation ANIM_MOVING_UP = AnimationCreator.create("textures/arrow_moving.png", 4, 1, 0.25f, 0);
	private static final Animation ANIM_MOVING_RIGHT = AnimationCreator.create("textures/arrow_moving.png", 4, 1, 0.25f, 1);
	private static final Animation ANIM_MOVING_DOWN = AnimationCreator.create("textures/arrow_moving.png", 4, 1, 0.25f, 2);
	private static final Animation ANIM_MOVING_LEFT = AnimationCreator.create("textures/arrow_moving.png", 4, 1, 0.25f, 3);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationCreator.create("textures/arrow_attacking.png", 4, 1, 0.125f, 0);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationCreator.create("textures/arrow_attacking.png", 4, 1, 0.125f, 1);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationCreator.create("textures/arrow_attacking.png", 4, 1, 0.125f, 2);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationCreator.create("textures/arrow_attacking.png", 4, 1, 0.125f, 3);
	
	public Arrow(GameScreen game) {
		super(game);
		setSpeed(300);
		
		// La portée de la flèche est la moitié de la taille de la flèche. En effet, lorsqu'une unité
		// est à cette distance du centre de la flèche alors c'est que la flèche touche cette unité.
		setRange(0, 32 / 2);
		setDamage(1);
		setAttackInterval(0);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions.RIGHT, ANIM_MOVING_RIGHT);
		setAnimation(States.MOVING, Directions.UP_RIGHT, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions.UP_LEFT, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions.UP, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions.LEFT, ANIM_MOVING_LEFT);
		setAnimation(States.MOVING, Directions.DOWN_LEFT, ANIM_MOVING_DOWN);
		setAnimation(States.MOVING, Directions.DOWN, ANIM_MOVING_DOWN);
		setAnimation(States.MOVING, Directions.DOWN_RIGHT, ANIM_MOVING_DOWN);

		setAnimation(States.ATTACKING, Directions.RIGHT, ANIM_ATTACKING_RIGHT);
		setAnimation(States.ATTACKING, Directions.UP_RIGHT, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions.UP_LEFT, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions.UP, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions.LEFT, ANIM_ATTACKING_LEFT);
		setAnimation(States.ATTACKING, Directions.DOWN_LEFT, ANIM_ATTACKING_DOWN);
		setAnimation(States.ATTACKING, Directions.DOWN, ANIM_ATTACKING_DOWN);
		setAnimation(States.ATTACKING, Directions.DOWN_RIGHT, ANIM_ATTACKING_DOWN);
	}
}
