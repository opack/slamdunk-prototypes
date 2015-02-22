package com.slamdunk.toolkit.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire de fichiers de propriétés, capable de stocker plusieurs
 * fichiers
 */
public class PropertiesManager {
	
	private static Map<String, TypedProperties> propertiesMap;
	
	static {
		propertiesMap = new HashMap<String, TypedProperties>();
	}

	private PropertiesManager () {
	}
	
	public static void init(String propertiesKey) {
		TypedProperties properties = new TypedProperties("properties/" + propertiesKey + ".properties");
		if (properties.isLoaded()) {
			propertiesMap.put(propertiesKey, properties);
		}
	}
	
	public static boolean getBoolean(String propertiesKey, String name, boolean fallback) {
		TypedProperties properties = propertiesMap.get(propertiesKey);
		if (properties == null) {
			return fallback;
		}
		return properties.getBooleanProperty(name, fallback);
	}

	public static float getFloat(String propertiesKey, String name, float fallback) {
		TypedProperties properties = propertiesMap.get(propertiesKey);
		if (properties == null) {
			return fallback;
		}
		return properties.getFloatProperty(name, fallback);
	}

	public static int getInteger(String propertiesKey, String name, int fallback) {
		TypedProperties properties = propertiesMap.get(propertiesKey);
		if (properties == null) {
			return fallback;
		}
		return properties.getIntegerProperty(name, fallback);
	}

	public static String getString(String propertiesKey, String name, String fallback) {
		TypedProperties properties = propertiesMap.get(propertiesKey);
		if (properties == null) {
			return fallback;
		}
		return properties.getStringProperty(name, fallback);
	}
}
