package com.slamdunk.wordarena.units.buildings;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.Factions;
import com.slamdunk.wordarena.units.SimpleUnit;

/**
 * Représente un château : celui du joueur ou de l'ennemi.
 */
public class Castle extends SimpleUnit {

	private static final Animation ANIM_IDLE = AnimationCreator.create("textures/castle_idle.png", 8, 1, 0.0675f);
	static {
		ANIM_IDLE.setPlayMode(PlayMode.LOOP);
	}
	
	public Castle(GameScreen game) {
		super(game);
		setFaction(Factions.PLAYER);
		
		setHp(15);
		
		initAnimationRendering(128, 128);
		setAnimation(States.IDLE, Directions4.RIGHT, ANIM_IDLE);
		
		setState(States.IDLE);
	}
	
	@Override
	public String toString() {
		return "Castle " + getHp() + "HP" + getPosition();
	}
}
