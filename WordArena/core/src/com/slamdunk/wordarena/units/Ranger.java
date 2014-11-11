package com.slamdunk.wordarena.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

public class Ranger extends OffensiveUnit {
	private static final Animation ANIM_MOVE_UP = AnimationCreator.create("textures/ranger_moving.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVE_RIGHT = AnimationCreator.create("textures/ranger_moving.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVE_DOWN = AnimationCreator.create("textures/ranger_moving.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVE_LEFT = AnimationCreator.create("textures/ranger_moving.png", 3, 4, 0.25f, 9, 10, 11);
	
	private static final Animation ANIM_ATTACK_UP = AnimationCreator.create("textures/ranger_attacking.png", 4, 4, 0.375f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACK_RIGHT = AnimationCreator.create("textures/ranger_attacking.png", 4, 4, 0.375f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACK_DOWN = AnimationCreator.create("textures/ranger_attacking.png", 4, 4, 0.375f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACK_LEFT = AnimationCreator.create("textures/ranger_attacking.png", 4, 4, 0.375f, 12, 13, 14, 15);
	
	public Ranger(GameScreen game) {
		super(game, Factions.PLAYER, 1, 4, 1, 1.5f);
		setHp(10);
		setSpeed(2);

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
	
	@Override
	public String toString() {
		return "Ranger " + getHp() + "HP" + getPosition();
	}
}
