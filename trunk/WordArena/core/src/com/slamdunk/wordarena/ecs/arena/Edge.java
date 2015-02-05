package com.slamdunk.wordarena.ecs.arena;

import com.badlogic.gdx.math.Vector2;

/**
 * Représente un côté "virtuel" d'une cellule. Les positions
 * des points sont ici des points "virtuels" en ce sens qu'ils
 * représentent les coordonnées des points si les cellules
 * étaient celles d'une grilles sans espaces. En réalité,
 * les cellules sont espacées et les coordonnées réelles
 * des points (en pixels) ne sont pas celles indiquées ici.
 * Cette "triche" est nécessaire pour s'assurer que 2
 * cellules adjacentes partagent bien le même côté.
 * Par convention, les coordonnées des cellules représentent
 * les points suivants de la cellule :
 *   - Côté gauche : p1 = bas-gauche, p2 = haut gauche
 *   - Côté haut : p1 = haut gauche, p2 = 
 */
public class Edge {
	public Vector2 p1;
	public Vector2 p2;
}
