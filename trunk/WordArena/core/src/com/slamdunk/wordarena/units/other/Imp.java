package com.slamdunk.wordarena.units.other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.OffensiveUnit;

public class Imp extends OffensiveUnit {
	private static final Animation ANIM_MOVING_UP = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_MOVING_LEFT = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_MOVING_DOWN = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_MOVING_RIGHT = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_DYING = AnimationCreator.create("textures/imp_dying.png", 7, 1, 0.25f);
	
	public Imp(GameScreen game) {
		super(game);
		setFaction(Factions.OTHER);
		
		setHp(30);
		setSpeed(15);

		setRange(0, 20);
		setDamage(2);
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
		
		setAnimation(States.DYING, Directions.RIGHT, ANIM_DYING);
		setAnimation(States.DYING, Directions.UP_RIGHT, ANIM_DYING);
		setAnimation(States.DYING, Directions.UP_LEFT, ANIM_DYING);
		setAnimation(States.DYING, Directions.UP, ANIM_DYING);
		setAnimation(States.DYING, Directions.LEFT, ANIM_DYING);
		setAnimation(States.DYING, Directions.DOWN_LEFT, ANIM_DYING);
		setAnimation(States.DYING, Directions.DOWN, ANIM_DYING);
		setAnimation(States.DYING, Directions.DOWN_RIGHT, ANIM_DYING);
	}
	
	@Override
	public String toString() {
		return "Imp " + getHp() + "HP" + getPosition();
	}
}
