package com.slamdunk.toolkit.settings;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Gestionnaire de préférence qui simplifie l'accès aux préférences
 * persistantes de l'application
 */
public class PreferencesManager {
	private Preferences prefs;
	
	/**
	 * 
	 * @param preferencesTag Tag des préférences, habituellement
	 * le nom de l'application
	 */
	public PreferencesManager(String preferencesTag) {
		prefs = Gdx.app.getPreferences(preferencesTag);
	}

	public void putBoolean(String key, boolean val) {
		prefs.putBoolean(key, val);
	}

	public void putInteger(String key, int val) {
		prefs.putInteger(key, val);
	}

	public void putLong(String key, long val) {
		prefs.putLong(key, val);
	}

	public void putFloat(String key, float val) {
		prefs.putFloat(key, val);
	}

	public void putString(String key, String val) {
		prefs.putString(key, val);
	}

	public void put(Map<String, ?> vals) {
		prefs.put(vals);
	}

	public boolean getBoolean(String key) {
		return prefs.getBoolean(key);
	}

	public int getInteger(String key) {
		return prefs.getInteger(key);
	}

	public long getLong(String key) {
		return prefs.getLong(key);
	}

	public float getFloat(String key) {
		return prefs.getFloat(key);
	}

	public String getString(String key) {
		return prefs.getString(key);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return prefs.getBoolean(key, defValue);
	}

	public int getInteger(String key, int defValue) {
		return prefs.getInteger(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return prefs.getLong(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return prefs.getFloat(key, defValue);
	}

	public String getString(String key, String defValue) {
		return prefs.getString(key, defValue);
	}

	public Map<String, ?> get() {
		return prefs.get();
	}

	public boolean contains(String key) {
		return prefs.contains(key);
	}

	public void clear() {
		prefs.clear();
	}

	public void remove(String key) {
		prefs.remove(key);
	}

	public void flush() {
		prefs.flush();
	}
}
