package com.slamdunk.pixelkingdomadvanced.units.troups;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.slamdunk.pixelkingdomadvanced.ai.States;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.GameScreen;
import com.slamdunk.pixelkingdomadvanced.units.OffensiveUnit;
import com.slamdunk.pixelkingdomadvanced.units.Units;
import com.slamdunk.toolkit.gameparts.creators.AnimationFactory;
import com.slamdunk.toolkit.world.Directions4;

/**
 * Unité de tank. Le paladin a la particularité de s'arrêter sur place quand le joueur
 * le touche. Cela permet de placer une ligne de défense à un endroit précis.
 */
public class Paladin extends OffensiveUnit {
	public static final String IMAGE_SPAWN_BUTTON = "textures/warrior_spawn.png";
	
	private static final Animation ANIM_MOVING_UP = AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, 0, 1, 2);
	private static final Animation ANIM_MOVING_RIGHT = AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, 3, 4, 5);
	private static final Animation ANIM_MOVING_DOWN = AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, 6, 7, 8);
	private static final Animation ANIM_MOVING_LEFT = AnimationFactory.create("textures/warrior_moving.png", 3, 4, 0.25f, 9, 10, 11);
	
	private static final Animation ANIM_ATTACKING_UP = AnimationFactory.create("textures/warrior_attacking.png", 4, 4, 0.125f, 0, 1, 2, 3);
	private static final Animation ANIM_ATTACKING_RIGHT = AnimationFactory.create("textures/warrior_attacking.png", 4, 4, 0.125f, 4, 5, 6, 7);
	private static final Animation ANIM_ATTACKING_DOWN = AnimationFactory.create("textures/warrior_attacking.png", 4, 4, 0.125f, 8, 9, 10, 11);
	private static final Animation ANIM_ATTACKING_LEFT = AnimationFactory.create("textures/warrior_attacking.png", 4, 4, 0.125f, 12, 13, 14, 15);
	
	public Paladin(GameScreen game) {
		super(game, Units.PALADIN);
		
		setHp(15);
		setSpeed(30);
		
		setRange(0, 20);
		setDamage(1);
		setAttackInterval(0.5f);
		
		initAnimationRendering(32, 32);
		setAnimation(States.MOVING, Directions4.RIGHT, ANIM_MOVING_RIGHT);
		setAnimation(States.MOVING, Directions4.UP, ANIM_MOVING_UP);
		setAnimation(States.MOVING, Directions4.LEFT, ANIM_MOVING_LEFT);
		setAnimation(States.MOVING, Directions4.DOWN, ANIM_MOVING_DOWN);

		setAnimation(States.ATTACKING, Directions4.RIGHT, ANIM_ATTACKING_RIGHT);
		setAnimation(States.ATTACKING, Directions4.UP, ANIM_ATTACKING_UP);
		setAnimation(States.ATTACKING, Directions4.LEFT, ANIM_ATTACKING_LEFT);
		setAnimation(States.ATTACKING, Directions4.DOWN, ANIM_ATTACKING_DOWN);
		
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
