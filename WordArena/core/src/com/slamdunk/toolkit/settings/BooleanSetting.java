package com.slamdunk.toolkit.settings;

public class BooleanSetting {
	private PreferencesManager preferences;
	private final String key;
	private final boolean defaultValue;
	
	public BooleanSetting(PreferencesManager preferences, String key, boolean defaultValue) {
		this.preferences = preferences;
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public void set(boolean value) {
		preferences.putBoolean(key, value);
		preferences.flush();
	}
	public boolean get() {
		return preferences.getBoolean(key, defaultValue);
	}
}
