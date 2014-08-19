package com.slamdunk.toolkit.settings;

public class MusicActivatedSetting extends BooleanSetting {

	public MusicActivatedSetting(PreferencesManager preferences) {
		super(preferences, "MUSIC_ACTIVATED", true);
	}
	
}
