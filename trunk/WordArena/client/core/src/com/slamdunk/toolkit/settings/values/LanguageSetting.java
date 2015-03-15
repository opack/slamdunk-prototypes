package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.PreferencesManager;
import com.slamdunk.toolkit.settings.StringSetting;

public class LanguageSetting extends StringSetting {
	public static final String DEFAULT = "en";

	public LanguageSetting(PreferencesManager preferences) {
		super(preferences, "LANGUAGE", DEFAULT);
	}
	
}
