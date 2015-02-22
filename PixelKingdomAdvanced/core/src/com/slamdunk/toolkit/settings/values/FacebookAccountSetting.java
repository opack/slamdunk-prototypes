package com.slamdunk.toolkit.settings.values;

import com.slamdunk.toolkit.settings.PreferencesManager;
import com.slamdunk.toolkit.settings.StringSetting;

public class FacebookAccountSetting extends StringSetting {

	public FacebookAccountSetting(PreferencesManager preferences) {
		super(preferences, "FACEBOOK_ACCOUNT", "");
	}
	
}
