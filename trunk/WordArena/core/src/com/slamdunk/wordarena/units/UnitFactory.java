package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.buildings.Castle;
import com.slamdunk.wordarena.units.projectiles.Arrow;
import com.slamdunk.wordarena.units.projectiles.ProjectileUnit;
import com.slamdunk.wordarena.units.troups.Archer;
import com.slamdunk.wordarena.units.troups.Imp;
import com.slamdunk.wordarena.units.troups.Ninja;
import com.slamdunk.wordarena.units.troups.Paladin;

public class UnitFactory {
	public static SimpleUnit create(GameScreen gameScreen, Units type) {
		SimpleUnit unit = null;
		switch (type) {
		case ARCHER:
			unit = new Archer(gameScreen);
			break;
		case CASTLE:
			unit = new Castle(gameScreen);
			break;
		case IMP:
			unit = new Imp(gameScreen);
			break;
		case NINJA:
			unit = new Ninja(gameScreen);
			break;
		case PALADIN:
			unit = new Paladin(gameScreen);
			break;
		case ARROW:
			unit = new Arrow(gameScreen);
			break;
		default:
			break;
		}
		return unit;
	}
	
	public static ProjectileUnit createProjectile(GameScreen gameScreen, Units type) {
		return (ProjectileUnit)create(gameScreen, type);
	}
}
