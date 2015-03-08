package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.graphics.SpriteBatchUtils;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.data.WallData;

/**
 * Représente un mur de l'arène
 */
public class ArenaWall extends SpritedActor {
	private WallData data;
	
	public ArenaWall(ArenaCell cell1, ArenaCell cell2) {
		super(buildSprite(cell1, cell2));
		data = new WallData(cell1, cell2);
	}
	
	public WallData getData() {
		return data;
	}

	/**
	 * Crée le Sprite pour le mur entre ces 2 cellules
	 * @param cell1
	 * @param cell2
	 * @return
	 */
	private static Sprite buildSprite(ArenaCell cell1, ArenaCell cell2) {
		// Récupère la taille de la texture de façon à centrer le mur sur le bord commun entre les 2 cellules
		final float halfWallThickness = Assets.wall.getHeight() / 2;
		
		// Trouve les 2 points en commun entre ces cellules
		Vector2 p1 = new Vector2();
		Vector2 p2 = new Vector2();
		Point pos1 = cell1.getData().position;
		Point pos2 = cell2.getData().position;
		// Teste si les cellules sont sur la même colonne
		if (pos1.getX() == pos2.getX()) {
			// Teste si cell1 est en-dessous de cell2
			if (pos1.getY() < pos2.getY()) {
				// Leur côté en commun est donc le côté bas de cell2
				p1.set(cell2.getX(), cell2.getY() - halfWallThickness);
				p2.set(cell2.getRight(), cell2.getY() - halfWallThickness);
			}
			// Teste si cell1 est au-dessus de cell2
			else if (pos1.getY() > pos2.getY()) {
				// Leur côté en commun est donc le côté bas de cell1
				p1.set(cell1.getX(), cell1.getY() - halfWallThickness);
				p2.set(cell1.getRight(), cell1.getY() - halfWallThickness);
			}
			// Les cellules sont à la même position
			else {
				throw new IllegalArgumentException("Supplied cells must not be at the same position !");
			}
		}
		// Teste si les cellules sont sur la même ligne
		else if (pos1.getY() == pos2.getY()) {
			// Teste si cell1 est à gauche de cell2
			if (pos1.getX() < pos2.getX()) {
				// Leur côté en commun est donc le côté gauche de cell2
				p1.set(cell2.getX() - halfWallThickness, cell2.getY());
				p2.set(cell2.getX() - halfWallThickness, cell2.getTop());
			}
			// Teste si cell1 est à droite de cell2
			else if (pos1.getX() > pos2.getX()) {
				// Leur côté en commun est donc le côté gauche de cell1
				p1.set(cell1.getX() - halfWallThickness, cell1.getY());
				p2.set(cell1.getX() - halfWallThickness, cell1.getTop());
			}
			// Les cellules sont à la même position
			else {
				throw new IllegalArgumentException("Supplied cells must not be at the same position !");
			}
		} else {
			// Les cellules ne sont pas adjacentes : il ne peut pas y avoir de mur
			throw new IllegalArgumentException("Supplied cells must be adjacent !");
		}
		
		// Crée le sprite du mur
		return SpriteBatchUtils.createSpritedLine(Assets.wall, p1, p2);
	}
}
