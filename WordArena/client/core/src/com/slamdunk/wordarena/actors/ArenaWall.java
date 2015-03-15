package com.slamdunk.wordarena.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.enums.Borders;

/**
 * Représente un mur de l'arène
 */
public class ArenaWall {
	/**
	 * Crée le Sprite pour le mur entre ces 2 cellules
	 * @param cell1
	 * @param cell2
	 * @return null si aucun mur ne doit être dessiné (2 cellules en diagonale)
	 */
	public static Image buildWall(ArenaCell cell1, ArenaCell cell2) {
		// Trouve les 2 points en commun entre ces cellules
		Vector2 corner = new Vector2();
		Point pos1 = cell1.getData().position;
		Point pos2 = cell2.getData().position;
		Image image;
		Borders border;
		// Teste si les cellules sont sur la même colonne
		if (pos1.getX() == pos2.getX()) {
			image = new Image(Assets.wall_h);
			// Teste si cell1 est en-dessous de cell2
			if (pos1.getY() < pos2.getY()) {
				// Leur côté en commun est donc le côté haut de cell1
				border = Borders.TOP;
				corner.set(cell1.getX(), cell1.getTop());
			}
			// Teste si cell1 est au-dessus de cell2
			else if (pos1.getY() > pos2.getY()) {
				// Leur côté en commun est donc le côté bas de cell1
				border = Borders.BOTTOM;
				corner.set(cell1.getX(), cell1.getY());
			}
			// Les cellules sont à la même position
			else {
				throw new IllegalArgumentException("Supplied cells must not be at the same position !");
			}
		}
		// Teste si les cellules sont sur la même ligne
		else if (pos1.getY() == pos2.getY()) {
			image = new Image(Assets.wall_v);
			// Teste si cell1 est à gauche de cell2
			if (pos1.getX() < pos2.getX()) {
				// Leur côté en commun est donc le côté droit de cell1
				border = Borders.RIGHT;
				corner.set(cell1.getRight(), cell1.getY());
			}
			// Teste si cell1 est à droite de cell2
			else if (pos1.getX() > pos2.getX()) {
				// Leur côté en commun est donc le côté gauche de cell1
				border = Borders.LEFT;
				corner.set(cell1.getX(), cell1.getY());
			}
			// Les cellules sont à la même position
			else {
				throw new IllegalArgumentException("Supplied cells must not be at the same position !");
			}
		} else {
			// Les cellules ne sont pas adjacentes : il ne peut pas y avoir de mur dessiné
			return null;
		}
		
		// Aligne l'image sur le bord
		ActorHelper.alignOnBorder(border, corner, image);
		return image;
	}
}
