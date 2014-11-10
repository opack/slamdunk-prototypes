package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.screens.GameScreen;

public class Paladin extends OffensiveUnit {
	public Paladin(GameScreen game) {
		super(game, Factions.PLAYER, "paladin.png", 1, 1, 2, 0.5f);
		setHp(10);
		setSpeed(2);
	}
	
	@Override
	public String toString() {
		return "Paladin " + getHp() + "HP" + getPosition();
	}
}
