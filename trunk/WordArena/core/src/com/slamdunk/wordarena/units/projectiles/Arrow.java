package com.slamdunk.wordarena.units.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.units.ProjectileUnit;

public class Arrow extends ProjectileUnit {
	private static final Animation ANIM_MOVE_UP = AnimationCreator.create("textures/arrow_moving.png", 8, 1, 0.25f, 2);
	private static final Animation ANIM_MOVE_RIGHT = AnimationCreator.create("textures/arrow_moving.png", 8, 1, 0.25f, 4);
	private static final Animation ANIM_MOVE_DOWN = AnimationCreator.create("textures/arrow_moving.png", 8, 1, 0.25f, 6);
	private static final Animation ANIM_MOVE_LEFT = AnimationCreator.create("textures/arrow_moving.png", 8, 1, 0.25f, 0);
	
	private static final Animation ANIM_ATTACK_UP = AnimationCreator.create("textures/arrow_attacking.png", 8, 1, 0.125f, 2);
	private static final Animation ANIM_ATTACK_RIGHT = AnimationCreator.create("textures/arrow_attacking.png", 8, 1, 0.125f, 4);
	private static final Animation ANIM_ATTACK_DOWN = AnimationCreator.create("textures/arrow_attacking.png", 8, 1, 0.125f, 6);
	private static final Animation ANIM_ATTACK_LEFT = AnimationCreator.create("textures/arrow_attacking.png", 8, 1, 0.125f, 0);
	
	public Arrow(GameScreen game) {
		super(game);
		setSpeed(4);
		
		setRange(0, 0);
		setDamage(1);
		setAttackInterval(0);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions.UP, ANIM_MOVE_UP);
		setAnimation(States.MOVING, Directions.RIGHT, ANIM_MOVE_RIGHT);
		setAnimation(States.MOVING, Directions.DOWN, ANIM_MOVE_DOWN);
		setAnimation(States.MOVING, Directions.LEFT, ANIM_MOVE_LEFT);

		setAnimation(States.ATTACKING, Directions.UP, ANIM_ATTACK_UP);
		setAnimation(States.ATTACKING, Directions.RIGHT, ANIM_ATTACK_RIGHT);
		setAnimation(States.ATTACKING, Directions.DOWN, ANIM_ATTACK_DOWN);
		setAnimation(States.ATTACKING, Directions.LEFT, ANIM_ATTACK_LEFT);
	}
}
