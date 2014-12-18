package com.slamdunk.wordarena.units.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.OffensiveUnit;

/**
 * Un projectile qui est lancé et suit sa course jusqu'à sortir de l'écran
 */
public class ProjectileUnit extends OffensiveUnit {
	private boolean launched;
	
	public ProjectileUnit(GameScreen game) {
		super(game);
		// Par défaut, on touche à l'impact
		setRange(0, 0);
		// Par défaut, on fait 1 point de dégât
		setDamage(1);
		// Par défaut, on frappe dès qu'on passe en mode ATTACKING, donc à l'impact
		setAttackInterval(0);
		
		launched = false;
	}
	
	@Override
	protected void performAttack(float delta) {
		super.performAttack(delta);
		
		// Une fois la cible touchée, le projectile disparaît.
		// On joue une animation de mort car le projectile peut
		// par exemple exploser à l'impact.
		setState(States.DYING);
	}
	
	@Override
	protected void performMove(float delta) {
		// Si le projectile n'a pas encore été lancé, on le déplace
		// jusqu'en dehors de l'écran
		if (!launched) {
			launched = true;
			// On va déterminer un point hors de la carte qui est sur la trajectoire vers la cible.
			// C'est là qu'on enverra la flèche avant de la faire disparaître si elle n'a rien touché.
			// Pour cela, on va utiliser la droite qui passe par le centre du projectile et de la cible.
			Vector2 projectileCenter = getCenterPosition();
			Vector2 targetCenter = getTarget().getCenterPosition();
			
			// Détermine les valeurs de a et b dans l'équation de droite y=ax+b
			float a = (targetCenter.y - projectileCenter.y) / (targetCenter.x - projectileCenter.x);
			float b = targetCenter.y - targetCenter.x * a;
			
			// Calcule l'endroit où la flèche sortira de la carte
			Image map = (Image)getGame().getObjectsOverlay().getWorld().findActor("background");
			Vector2 out = new Vector2();
			if (targetCenter.x < projectileCenter.x) {
				// Le projectile ira vers la gauche. Il va donc croiser le bord
				// gauche de la carte. On calcule donc son y pour x=map.getX()
				out.x = map.getX();
			} else {
				// Le projectile ira vers la droite. Il va donc croiser le bord
				// droit de la carte. On calcule donc son y pour x=map.getX() + map.getWidth()
				out.x = map.getX() + map.getWidth();
			}
			out.y = a * out.x + b;
			float timeToArrival = out.dst(projectileCenter.x, projectileCenter.y) / getSpeed();
			addAction(Actions.moveTo(out.x - getWidth() / 2, out.y - getHeight() / 2, timeToArrival));
			
			// Tourne la flèche dans la direction de la cible
			setRotation(targetCenter.sub(projectileCenter).angle());
		}
		// Si le projectile a été lancé et que son déplacement est achevé,
		// alors il est hors de l'écran : on le détruit
		else if (getActions().size == 0) {
			setState(States.DYING);
		}
		
		// Si le projectile est en déplacement, on vérifie s'il
		// touche un ennemi
		if (getState() == States.MOVING) {
			searchAndAttackEnnemy();
		}
	}
}
