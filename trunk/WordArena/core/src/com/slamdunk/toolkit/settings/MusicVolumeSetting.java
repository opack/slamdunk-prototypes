package com.slamdunk.toolkit.settings;

public class MusicVolumeSetting extends FloatSetting {

	public MusicVolumeSetting(PreferencesManager preferences) {
		super(preferences, "MUSIC_VOLUME", 1);
	}
	
}
