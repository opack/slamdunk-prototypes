package com.slamdunk.wordarena.units.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;
import com.slamdunk.wordarena.units.Units;

public class Arrow extends ProjectileUnit {
	private static final Animation ANIM_MOVING_UP = AnimationFactory.create("textures/arrow_moving.png", 4, 1, 0.25f, 0);
	private static final Animation ANIM_MOVING_RIGHT = AnimationFactory.create("textures/arrow_moving.png", 4, 1, 0.25f, 1);
	private static final Animation ANIM_MOVING_DOWN = AnimationFactory.create("textures/arrow_moving.png", 4, 1, 0.25f, 2);
	private static final Animation ANIM_MOVING_LEFT = AnimationFactory.create("textures/arrow_moving.png", 4, 1, 0.25f, 3);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationFactory.create("textures/arrow_attacking.png", 4, 1, 0.125f, 0);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationFactory.create("textures/arrow_attacking.png", 4, 1, 0.125f, 1);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationFactory.create("textures/arrow_attacking.png", 4, 1, 0.125f, 2);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationFactory.create("textures/arrow_attacking.png", 4, 1, 0.125f, 3);
	
	public Arrow(GameScreen game) {
		super(game, Units.ARROW);
		setSpeed(300);
		
		// La portée de la flèche est la moitié de la taille de la flèche. En effet, lorsqu'une unité
		// est à cette distance du centre de la flèche alors c'est que la flèche touche cette unité.
		setRange(0, 32 / 2);
		setAttackInterval(0);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions4.RIGHT, ANIM_MOVING_RIGHT);
		setAnimation(States.MOVING, Directions4.UP, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions4.LEFT, ANIM_MOVING_LEFT);
		setAnimation(States.MOVING, Directions4.DOWN, ANIM_MOVING_DOWN);

		setAnimation(States.ATTACKING, Directions4.RIGHT, ANIM_ATTACKING_RIGHT);
		setAnimation(States.ATTACKING, Directions4.UP, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions4.LEFT, ANIM_ATTACKING_LEFT);
		setAnimation(States.ATTACKING, Directions4.DOWN, ANIM_ATTACKING_DOWN);
	}
}
