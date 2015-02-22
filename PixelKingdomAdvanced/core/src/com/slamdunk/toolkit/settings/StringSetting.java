package com.slamdunk.toolkit.settings;

public class StringSetting {
	private PreferencesManager preferences;
	private final String key;
	private final String defaultValue;
	
	public StringSetting(PreferencesManager preferences, String key, String defaultValue) {
		this.preferences = preferences;
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public void set(String value) {
		preferences.putString(key, value);
		preferences.flush();
	}
	public String get() {
		return preferences.getString(key, defaultValue);
	}
}
