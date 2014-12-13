package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;

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
		ProjectileUnit projectile = (ProjectileUnit)projectileUnit.create(getGame());
		projectile.setFaction(getFaction());
		projectile.setCenterPosition(getCenterX(), getCenterY());
		UnitManager.getInstance().addUnit(projectile);
		
		// Envoi du projectile sur l'adversaire visé
		projectile.setTarget(getTarget());
		projectile.setState(States.MOVING);
		
		// Le personnage reprend son chemin s'il n'y a plus d'ennemi
		// à proximité
		if (!searchAndAttackEnnemy()) {
			setState(States.MOVING);
		}
	}
}
