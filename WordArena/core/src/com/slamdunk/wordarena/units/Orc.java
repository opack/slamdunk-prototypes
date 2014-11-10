package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.screens.GameScreen;

public class Orc extends OffensiveUnit {
	public Orc(GameScreen game) {
		super(game, Factions.OTHER, "enemy.png", 1, 1, 1, 0.3f);
		setHp(10);
		setSpeed(2);
	}
	
	@Override
	public String toString() {
		return "Orc " + getHp() + "HP" + getPosition();
	}
}
