package com.slamdunk.toolkit.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Classe de propriétés étendues permettant de récupérer directement le bon
 * type de donnée plutôt qu'un String.
 */
public class TypedProperties extends Properties {
    private static final long serialVersionUID = 5448778341923435054L;

    /**
     * Indique si le fichier .properties a correctement été lu et chargé
     * dans cet objet
     */
    private boolean loaded;
    
    /**
     * Crée un nouvel objet en le remplissant avec les données du fichier spécifié
     * @param fh
     */
    public TypedProperties(FileHandle fileHandle) {
    	InputStream inStream = fileHandle.read();
		try {
			load(inStream);
			inStream.close();
			loaded = true;
		} catch (IOException e) {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
    
    /**
     * Crée un nouvel objet en le remplissant avec les données du fichier spécifié
     * @param fh
     */
    public TypedProperties(String filename) {
    	this(Gdx.files.internal(filename));
    }
    
	public boolean isLoaded() {
		return loaded;
	}

	public boolean getBooleanProperty(String name, boolean fallback) {
        String v = getProperty(name);
        if (v == null) return fallback;
        return Boolean.parseBoolean(v);
    }

    public float getFloatProperty(String name, float fallback) {
        String v = getProperty(name);
        if (v == null) return fallback;
        return Float.parseFloat(v);
    }

    public int getIntegerProperty(String name, int fallback) {
        String v = getProperty(name);
        if (v == null) return fallback;
        return Integer.parseInt(v);
    }

    public String getStringProperty(String name, String fallback) {
        String v = getProperty(name);
        if (v == null) return fallback;
        return v;
    }
    
    public String[] getStringArrayProperty(String name, String regex) {
        String v = getProperty(name);
        if (v == null) return null;
        return v.split(regex);
    }
    
    public int[] getIntegerArrayProperty(String name, String regex) {
        String v = getProperty(name);
        if (v == null) return null;
        String[] stringArray = v.split(regex);
        int[] intArray = new int[stringArray.length];
        for (int curValue = 0; curValue < stringArray.length; curValue++) {
        	intArray[curValue] = Integer.parseInt(stringArray[curValue]);
        }
        return intArray;
    }
}