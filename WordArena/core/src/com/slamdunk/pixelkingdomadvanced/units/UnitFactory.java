package com.slamdunk.pixelkingdomadvanced.units;

import com.slamdunk.pixelkingdomadvanced.screens.battlefield.GameScreen;
import com.slamdunk.pixelkingdomadvanced.units.buildings.Castle;
import com.slamdunk.pixelkingdomadvanced.units.projectiles.Arrow;
import com.slamdunk.pixelkingdomadvanced.units.projectiles.ProjectileUnit;
import com.slamdunk.pixelkingdomadvanced.units.troups.Archer;
import com.slamdunk.pixelkingdomadvanced.units.troups.Imp;
import com.slamdunk.pixelkingdomadvanced.units.troups.Ninja;
import com.slamdunk.pixelkingdomadvanced.units.troups.Paladin;

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
