package com.slamdunk.wordarena.units.other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.OffensiveUnit;

public class Imp extends OffensiveUnit {
	private static final Animation ANIM_MOVE_UP = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_MOVE_LEFT = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_MOVE_DOWN = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_MOVE_RIGHT = AnimationCreator.create("textures/imp_moving.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_ATTACK_UP = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACK_LEFT = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACK_DOWN = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACK_RIGHT = AnimationCreator.create("textures/imp_attacking.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_DYING = AnimationCreator.create("textures/imp_dying.png", 7, 1, 0.25f);
	
	public Imp(GameScreen game) {
		super(game);
		setFaction(Factions.OTHER);
		
		setHp(30);
		setSpeed(1);

		setRange(0, 0);
		setDamage(2);
		setAttackInterval(1);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions.UP, ANIM_MOVE_UP);
		setAnimation(States.MOVING, Directions.RIGHT, ANIM_MOVE_RIGHT);
		setAnimation(States.MOVING, Directions.DOWN, ANIM_MOVE_DOWN);
		setAnimation(States.MOVING, Directions.LEFT, ANIM_MOVE_LEFT);
		
		setAnimation(States.ATTACKING, Directions.UP, ANIM_ATTACK_UP);
		setAnimation(States.ATTACKING, Directions.RIGHT, ANIM_ATTACK_RIGHT);
		setAnimation(States.ATTACKING, Directions.DOWN, ANIM_ATTACK_DOWN);
		setAnimation(States.ATTACKING, Directions.LEFT, ANIM_ATTACK_LEFT);
		
		setAnimation(States.DYING, Directions.LEFT, ANIM_DYING);
		setAnimation(States.DYING, Directions.RIGHT, ANIM_DYING);
		setAnimation(States.DYING, Directions.DOWN, ANIM_DYING);
		setAnimation(States.DYING, Directions.LEFT, ANIM_DYING);
	}
	
	@Override
	public String toString() {
		return "Imp " + getHp() + "HP" + getPosition();
	}
}