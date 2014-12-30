package com.slamdunk.wordarena.units;

import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.battlefield.GameScreen;
import com.slamdunk.wordarena.units.projectiles.ProjectileUnit;

/**
 * Comportement d'une unité qui envoie des projectiles
 */
public class RangedUnit extends OffensiveUnit {

	private Units projectileUnit;
	
	public RangedUnit(GameScreen game, Units type) {
		super(game, type);
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
		ProjectileUnit projectile = UnitFactory.createProjectile(getGame(), projectileUnit);
		projectile.setDamage(getDamage()); // Les dégâts du projectile dépendent de ceux de l'unité
		projectile.setFaction(getFaction());
		projectile.setPosition(getX(Align.center), getY(Align.center), Align.center);
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