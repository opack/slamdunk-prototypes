package com.slamdunk.toolkit.settings;

import com.slamdunk.toolkit.settings.values.FacebookAccountSetting;
import com.slamdunk.toolkit.settings.values.LanguageSetting;
import com.slamdunk.toolkit.settings.values.MusicActivatedSetting;
import com.slamdunk.toolkit.settings.values.MusicVolumeSetting;
import com.slamdunk.toolkit.settings.values.SfxActivatedSetting;
import com.slamdunk.toolkit.settings.values.SfxVolumeSetting;


/**
 * Contient les différents réglages de l'application
 * (activation des SFX, de la musique, nom du compte FB...).
 * 
 * Le développeur récupère ou définit une préférence via
 * SlamSettings.NOM_PREF.get()/set(). Exemple :
 * SlamSettings.SFX_ACTIVATED.set(true);
 * 
 * Pour ajouter d'autres préférences, il suffit de :
 *    1. Créer une nouvelle classe xxxSetting qui étends le BooleanSetting,
 *    StringSetting, FloatSetting...
 *    2. Etendre SlamSettings
 *    3. Déclarer un nouveau champ public static du nouveau type de Setting
 *    4. Redéfinir la méthode init() pour appeler super.init() et construire
 *    le Setting
 */
public class SlamSettings {
	public static SfxActivatedSetting SFX_ACTIVATED;
	public static SfxVolumeSetting SFX_VOLUME;
	public static MusicActivatedSetting MUSIC_ACTIVATED;
	public static MusicVolumeSetting MUSIC_VOLUME;
	public static FacebookAccountSetting FACEBOOK_ACCOUNT;
	public static LanguageSetting LANGUAGE;
	
	public static void init(String preferencesTag) {
		PreferencesManager preferences = new PreferencesManager(preferencesTag);
		
		SFX_ACTIVATED = new SfxActivatedSetting(preferences);
		SFX_VOLUME = new SfxVolumeSetting(preferences);
		MUSIC_ACTIVATED = new MusicActivatedSetting(preferences);
		MUSIC_VOLUME = new MusicVolumeSetting(preferences);
		FACEBOOK_ACCOUNT = new FacebookAccountSetting(preferences);
		LANGUAGE = new LanguageSetting(preferences);
	}
}
