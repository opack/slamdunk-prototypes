package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

/**
 * Comportement d'une unité qui envoie des projectiles
 */
public class RangedUnit extends OffensiveUnit {

	private Units projectileUnit;
	
	public RangedUnit(GameScreen game) {
		super(game);
	}
	
	public Units getProjectileUnit() {
		return projectileUnit;
	}

	public void setProjectileUnit(Units projectileUnit) {
		this.projectileUnit = projectileUnit;
	}

	@Override
	protected void performHit() {
		// Création d'un projectile
		SimpleUnit projectile = projectileUnit.create(getGame());
		projectile.setFaction(getFaction());
		projectile.setCenterPosition(getCenterX(), getCenterY());
		UnitManager.getInstance().addUnit(projectile);
		
		// Envoi du projectile en face
		projectile.setDirection(getDirection());
		projectile.setState(States.MOVING);
		
		// Le personnage reprend son chemin s'il n'y a plus d'ennemi
		// à proximité
		if (!searchAndAttackEnnemy()) {
			setState(States.MOVING);
		}
	}
}
