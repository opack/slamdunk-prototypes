package com.slamdunk.wordarena.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.screens.GameScreen;

public class Paladin extends OffensiveUnit {
	private static final Animation ANIM_MOVE_UP = AnimationCreator.create("textures/warrior_m.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVE_RIGHT = AnimationCreator.create("textures/warrior_m.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVE_DOWN = AnimationCreator.create("textures/warrior_m.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVE_LEFT = AnimationCreator.create("textures/warrior_m.png", 3, 4, 0.25f, 9, 10, 11);
	
	public Paladin(GameScreen game) {
		super(game, Factions.PLAYER, 1, 1, 2, 0.5f);
		setHp(10);
		setSpeed(2);
		
		initAnimationRendering(38, 38);
		setMoveAnimation(Directions.UP, ANIM_MOVE_UP);
		setMoveAnimation(Directions.RIGHT, ANIM_MOVE_RIGHT);
		setMoveAnimation(Directions.DOWN, ANIM_MOVE_DOWN);
		setMoveAnimation(Directions.LEFT, ANIM_MOVE_LEFT);
		
	}
	
	@Override
	public String toString() {
		return "Paladin " + getHp() + "HP" + getPosition();
	}
}
