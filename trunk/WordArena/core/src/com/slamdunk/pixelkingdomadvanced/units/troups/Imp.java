package com.slamdunk.pixelkingdomadvanced.units.troups;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.pixelkingdomadvanced.ai.States;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.GameScreen;
import com.slamdunk.pixelkingdomadvanced.units.OffensiveUnit;
import com.slamdunk.pixelkingdomadvanced.units.Units;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.world.Directions4;

public class Imp extends OffensiveUnit {
	private static final Animation ANIM_MOVING_UP = AnimationFactory.create("textures/imp_moving.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_MOVING_LEFT = AnimationFactory.create("textures/imp_moving.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_MOVING_DOWN = AnimationFactory.create("textures/imp_moving.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_MOVING_RIGHT = AnimationFactory.create("textures/imp_moving.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationFactory.create("textures/imp_attacking.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationFactory.create("textures/imp_attacking.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationFactory.create("textures/imp_attacking.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationFactory.create("textures/imp_attacking.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	private static final Animation ANIM_DYING = AnimationFactory.create("textures/imp_dying.png", 7, 1, 0.25f);
	
	public Imp(GameScreen game) {
		super(game, Units.IMP);
		
		setHp(30);
		setSpeed(15);

		setRange(0, 20);
		setDamage(2);
		setAttackInterval(1);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions4.RIGHT, ANIM_MOVING_RIGHT);
		setAnimation(States.MOVING, Directions4.UP, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions4.LEFT, ANIM_MOVING_LEFT);
		setAnimation(States.MOVING, Directions4.DOWN, ANIM_MOVING_DOWN);

		setAnimation(States.ATTACKING, Directions4.RIGHT, ANIM_ATTACKING_RIGHT);
		setAnimation(States.ATTACKING, Directions4.UP, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions4.LEFT, ANIM_ATTACKING_LEFT);
		setAnimation(States.ATTACKING, Directions4.DOWN, ANIM_ATTACKING_DOWN);
		
		setAnimation(States.DYING, Directions4.RIGHT, ANIM_DYING);
		setAnimation(States.DYING, Directions4.UP, ANIM_DYING);
		setAnimation(States.DYING, Directions4.LEFT, ANIM_DYING);
		setAnimation(States.DYING, Directions4.DOWN, ANIM_DYING);
	}
	
	@Override
	public String toString() {
		return "Imp " + getHp() + "HP" + getPosition();
	}
}
