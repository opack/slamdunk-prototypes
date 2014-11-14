package com.slamdunk.wordarena.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.pathfinder.Directions;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.OffensiveUnit;

/**
 * Unité de tank. Le paladin a la particularité de s'arrêter sur place quand le joueur
 * le touche. Cela permet de placer une ligne de défense à un endroit précis.
 */
public class Paladin extends OffensiveUnit {
	private static final Animation ANIM_MOVE_UP = AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVE_RIGHT = AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVE_DOWN = AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVE_LEFT = AnimationCreator.create("textures/warrior_moving.png", 3, 4, 0.25f, 9, 10, 11);
	
	private static final Animation ANIM_ATTACK_UP = AnimationCreator.create("textures/warrior_attacking.png", 4, 4, 0.125f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACK_RIGHT = AnimationCreator.create("textures/warrior_attacking.png", 4, 4, 0.125f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACK_DOWN = AnimationCreator.create("textures/warrior_attacking.png", 4, 4, 0.125f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACK_LEFT = AnimationCreator.create("textures/warrior_attacking.png", 4, 4, 0.125f, 12, 13, 14, 15);
	
	public Paladin(GameScreen game) {
		super(game);
		setFaction(Factions.PLAYER);
		
		setHp(10);
		setSpeed(2);
		
		setRange(0, 0);
		setDamage(1);
		setAttackInterval(0.5f);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions.UP, ANIM_MOVE_UP);
		setAnimation(States.MOVING, Directions.RIGHT, ANIM_MOVE_RIGHT);
		setAnimation(States.MOVING, Directions.DOWN, ANIM_MOVE_DOWN);
		setAnimation(States.MOVING, Directions.LEFT, ANIM_MOVE_LEFT);

		setAnimation(States.ATTACKING, Directions.UP, ANIM_ATTACK_UP);
		setAnimation(States.ATTACKING, Directions.RIGHT, ANIM_ATTACK_RIGHT);
		setAnimation(States.ATTACKING, Directions.DOWN, ANIM_ATTACK_DOWN);
		setAnimation(States.ATTACKING, Directions.LEFT, ANIM_ATTACK_LEFT);
		
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// Retourne true pour être notifié des touchUp() suivants ce touchDown()
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				States currentState = getState();
				if (currentState == States.MOVING) {
					setState(States.IDLE);
				} else if (currentState == States.IDLE) {
					setState(States.MOVING);
				}
			}
		});
	}
	
	@Override
	public String toString() {
		return "Paladin " + getHp() + "HP" + getPosition();
	}
}
