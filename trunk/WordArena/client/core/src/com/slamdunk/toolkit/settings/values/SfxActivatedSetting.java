package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.BooleanSetting;
import com.slamdunk.toolkit.settings.PreferencesManager;

public class SfxActivatedSetting extends BooleanSetting {

	public SfxActivatedSetting(PreferencesManager preferences) {
		super(preferences, "SFX_ACTIVATED", true);
	}
	
}
