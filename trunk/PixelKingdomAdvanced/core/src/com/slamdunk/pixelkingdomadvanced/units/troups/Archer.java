package com.slamdunk.pixelkingdomadvanced.units.troups;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.pixelkingdomadvanced.ai.States;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.GameScreen;
import com.slamdunk.pixelkingdomadvanced.units.RangedUnit;
import com.slamdunk.pixelkingdomadvanced.units.Units;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.world.Directions4;

public class Archer extends RangedUnit {
	public static final String IMAGE_SPAWN_BUTTON = "textures/ranger_spawn.png";
	
	private static final Animation ANIM_MOVING_UP = AnimationFactory.create("textures/ranger_moving.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVING_RIGHT = AnimationFactory.create("textures/ranger_moving.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVING_DOWN = AnimationFactory.create("textures/ranger_moving.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVING_LEFT = AnimationFactory.create("textures/ranger_moving.png", 3, 4, 0.25f, 9, 10, 11);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationFactory.create("textures/ranger_attacking.png", 4, 4, 0.25f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationFactory.create("textures/ranger_attacking.png", 4, 4, 0.25f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationFactory.create("textures/ranger_attacking.png", 4, 4, 0.25f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationFactory.create("textures/ranger_attacking.png", 4, 4, 0.25f, 12, 13, 14, 15);
	
	public Archer(GameScreen game) {
		super(game, Units.ARCHER);
		
		setHp(10);
		setSpeed(30);
		
		setRange(0, 100);
		setDamage(0.75f);
		setAttackInterval(1f);
		
		setProjectileUnit(Units.ARROW);
		
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
	
	@Override
	public String toString() {
		return "Archer " + getHp() + "HP" + getPosition();
	}
}
