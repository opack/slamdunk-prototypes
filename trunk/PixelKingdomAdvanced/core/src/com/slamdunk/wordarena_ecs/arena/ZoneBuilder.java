package com.slamdunk.wordarena_ecs.arena;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.wordarena_ecs.components.BoundsComponent;
import com.slamdunk.wordarena_ecs.components.ShapeComponent;
import com.slamdunk.wordarena_ecs.components.TransformComponent;
import com.slamdunk.wordarena_ecs.components.ZoneComponent;
import com.slamdunk.wordarena_ecs.systems.ComponentMappers;

/**
 * Construit une zone. Pour ce faire, on ajoute toutes les cellules qui font
 * partie de la zone. Ensuite, chaque côté de chaque cellule est ajouté dans
 * une table. Tous les doublons sont alors supprimés, et il ne reste donc
 * que les côtés extérieurs, communs avec aucune autre cellule.
 * On crée alors le polygone en partant d'un côté et en cherchant le côté
 * adjacent.
 */
public class ZoneBuilder {
	/**
	 * Table qui stocke le nombre de fois que se présente un point
	 */
	private Map<Vector2, Integer> pointsCount;
	
	/**
	 * Identifiant du joueur qui contrôle la zone par défaut
	 */
	private String defaultOwner;
	
	public ZoneBuilder() {
		defaultOwner = "";
		pointsCount = new HashMap<Vector2, Integer>();
	}
	
	public String getDefaultOwner() {
		return defaultOwner;
	}

	public void setDefaultOwner(String defaultOwner) {
		this.defaultOwner = defaultOwner;
	}

	/**
	 * Ajoute la cellule indiquée à la zone
	 * @param letterCell
	 */
	public void add(Entity letterCell) {
		TransformComponent transform = ComponentMappers.TRANSFORM.get(letterCell);
		BoundsComponent bounds = ComponentMappers.BOUNDS.get(letterCell);
		
		// Premier point : en bas à gauche
		addPoint(transform.pos.x, transform.pos.y);
		
		// Second point : en haut à gauche
		addPoint(transform.pos.x, transform.pos.y + bounds.bounds.height);
		
		// Troisième point : en haut à droite
		addPoint(transform.pos.x + bounds.bounds.width, transform.pos.y + bounds.bounds.height);
		
		// Quatrième point : en bas à droite
		addPoint(transform.pos.x + bounds.bounds.width, transform.pos.y);
	}

	/**
	 * Ajoute le point indiqué à la table des points, ou incrémente le nombre
	 * d'occurrence de ce point s'il était déjà dans la table.
	 * @param point
	 */
	private void addPoint(float x, float y) {
		Vector2 point = new Vector2(x, y);
		
		// Récupère le nombre d'occurrences de ce point
		Integer count = pointsCount.get(point);
		
		// S'il n'y en a pas, on l'ajoute
		if (count == null) {
			pointsCount.put(point, 1);
		}
		// S'il y en a, on incrémente le compte
		else {
			pointsCount.put(point, count + 1);
		}
	}
	
	/**
	 * Crée le polygone regroupant toutes les cellules ajoutées
	 * @return
	 */
	public Entity build() {
		// Assemble dans un array les positions des points uniques
		Array<Vector2> uniquePoints = new Array<Vector2>();
		Vector2 point;
		Integer count;
		for (Map.Entry<Vector2, Integer> points : pointsCount.entrySet()) {
			count = points.getValue();
			if (count == 1) {
				point = points.getKey();
				uniquePoints.add(point);
			}
		}
		
		// Transforme ce tableau de points en liste de flottants correspondant
		// à l'abscisse et l'ordonnée des différentes points
		float[] vertices = new float[uniquePoints.size];
		for (int curVertex = 0; curVertex < uniquePoints.size; curVertex += 2) {
			vertices[curVertex] = uniquePoints.get(curVertex).x;
			vertices[curVertex + 1] = uniquePoints.get(curVertex).y;
		}
		
		// Crée le polygone qui permettra de dessiner la zone
		ShapeComponent shape = new ShapeComponent();
		shape.polygon.setVertices(vertices);
		shape.drawStyle = ShapeType.Filled;
		shape.color = new Color(Color.RED);
		
		// Crée le transform qui stockera la position de la zone
		TransformComponent transform = new TransformComponent();
		transform.pos.set(shape.polygon.getX(), shape.polygon.getY(), 1);
		
		// Crée le composant qui contiendra les informations sur la zone
		ZoneComponent zone = new ZoneComponent();
		zone.owner = defaultOwner;
		
		// Crée l'entité correspondant à la zone
		Entity entity = new Entity();
		entity.add(transform);
		entity.add(shape);
		entity.add(zone);
		return entity;
	}
	
	public void reset() {
		pointsCount.clear();
	}
}
