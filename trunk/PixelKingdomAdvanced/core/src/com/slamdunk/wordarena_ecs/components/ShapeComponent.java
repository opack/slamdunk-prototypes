package com.slamdunk.wordarena_ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

/**
 * Une forme à dessiner
 */
public class ShapeComponent extends Component {
	public ShapeType drawStyle;
	public Color color;
	public final Polygon polygon = new Polygon();
}
