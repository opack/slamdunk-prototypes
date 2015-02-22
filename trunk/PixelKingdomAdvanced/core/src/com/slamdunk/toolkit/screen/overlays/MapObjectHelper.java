package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.maps.MapObject;

/**
 * Simplifie l'accès aux propriétés d'un MapObject
 */
public class MapObjectHelper {
	/**
	 * Retourne la propriété indiquée en int.
	 * Attention ! La propriété doit être int et pas float
	 * ou autre
	 * @param property
	 * @return
	 */
	public static int getIntProperty(MapObject mapObject, String property) {
		return (Integer)mapObject.getProperties().get(property);
	}
	
	/**
	 * Retourne la propriété indiquée en float.
	 * @param property
	 * @return
	 */
	public static float getFloatProperty(MapObject mapObject, String property) {
		return (Float)mapObject.getProperties().get(property);
	}
	
	/**
	 * Retourne la position x de l'objet
	 * @param mapObject
	 * @return
	 */
	public static float getX(MapObject mapObject) {
		return (Float)mapObject.getProperties().get("x");
	}
	
	/**
	 * Retourne la position y de l'objet
	 * @param mapObject
	 * @return
	 */
	public static float getY(MapObject mapObject) {
		return (Float)mapObject.getProperties().get("y");
	}

	/**
	 * Retourne true si l'objet a la valeur indiquée pour la
	 * propriété spécifiée
	 * @param object
	 * @param property
	 * @param value
	 * @return
	 */
	public static boolean hasValue(MapObject object, String property, Object value) {
		Object readValue = object.getProperties().get(property);
		return readValue != null && readValue.equals(value);
	}
}
