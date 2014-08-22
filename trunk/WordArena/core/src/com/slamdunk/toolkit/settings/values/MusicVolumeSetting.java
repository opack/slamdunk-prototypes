package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.FloatSetting;
import com.slamdunk.toolkit.settings.PreferencesManager;

public class MusicVolumeSetting extends FloatSetting {

	public MusicVolumeSetting(PreferencesManager preferences) {
		super(preferences, "MUSIC_VOLUME", 1);
	}
	
}
