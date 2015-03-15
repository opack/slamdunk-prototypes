package com.slamdunk.toolkit.settings;

public class FloatSetting {
	private PreferencesManager preferences;
	private final String key;
	private final float defaultValue;
	
	public FloatSetting(PreferencesManager preferences, String key, float defaultValue) {
		this.preferences = preferences;
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public void set(float value) {
		preferences.putFloat(key, value);
		preferences.flush();
	}
	public float get() {
		return preferences.getFloat(key, defaultValue);
	}
}
