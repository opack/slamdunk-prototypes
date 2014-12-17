package com.slamdunk.wordarena.units.other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.OffensiveUnit;

public class Ninja extends OffensiveUnit {
	private static final Animation ANIM_MOVING_UP = AnimationCreator.create("textures/ninja_moving.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVING_RIGHT = AnimationCreator.create("textures/ninja_moving.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVING_DOWN = AnimationCreator.create("textures/ninja_moving.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVING_LEFT = AnimationCreator.create("textures/ninja_moving.png", 3, 4, 0.25f, 9, 10, 11);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationCreator.create("textures/ninja_attacking.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationCreator.create("textures/ninja_attacking.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationCreator.create("textures/ninja_attacking.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationCreator.create("textures/ninja_attacking.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	public Ninja(GameScreen game) {
		super(game);
		setFaction(Factions.OTHER);
		
		setHp(10);
		setSpeed(50);

		setRange(0, 20);
		setDamage(1);
		setAttackInterval(1);
		
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
	
	@Override
	public String toString() {
		return "Ninja " + getHp() + "HP" + getPosition();
	}
}
