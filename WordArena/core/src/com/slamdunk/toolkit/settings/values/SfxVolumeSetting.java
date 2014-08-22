package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.FloatSetting;
import com.slamdunk.toolkit.settings.PreferencesManager;

public class SfxVolumeSetting extends FloatSetting {

	public SfxVolumeSetting(PreferencesManager preferences) {
		super(preferences, "SFX_VOLUME", 1);
	}
	
}
