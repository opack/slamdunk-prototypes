package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.BooleanSetting;
import com.slamdunk.toolkit.settings.PreferencesManager;

public class MusicActivatedSetting extends BooleanSetting {

	public MusicActivatedSetting(PreferencesManager preferences) {
		super(preferences, "MUSIC_ACTIVATED", true);
	}
	
}
