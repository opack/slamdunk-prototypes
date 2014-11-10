package com.slamdunk.wordarena.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.slamdunk.toolkit.graphics.drawers.AnimationCreator;
import com.slamdunk.wordarena.screens.GameScreen;

public class Paladin extends OffensiveUnit {
	public Paladin(GameScreen game) {
		super(game, Factions.PLAYER, "paladin.png", 1, 1, 2, 0.5f);
		setHp(10);
		setSpeed(2);
		
		Animation animation = AnimationCreator.create("textures/warrior_m.png", 3, 4, 0.25f, 6, 7, 8);
		createDrawers(false, true, false);
		getTextureDrawer().setActive(false);
		getAnimationDrawer().setAnimation(animation, true, true);
		
	}
	
	@Override
	public String toString() {
		return "Paladin " + getHp() + "HP" + getPosition();
	}
}
