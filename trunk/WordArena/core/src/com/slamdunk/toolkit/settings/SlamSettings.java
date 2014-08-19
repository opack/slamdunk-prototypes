package com.slamdunk.toolkit.settings;


/**
 * Contient les diff�rents r�glages de l'application
 * (activation des SFX, de la musique, nom du compte FB...).
 * 
 * Le d�veloppeur r�cup�re ou d�finit une pr�f�rence via
 * SlamSettings.NOM_PREF.get()/set(). Exemple :
 * SlamSettings.SFX_ACTIVATED.set(true);
 * 
 * Pour ajouter d'autres pr�f�rences, il suffit de :
 *    1. Cr�er une nouvelle classe xxxSetting qui �tends le BooleanSetting,
 *    StringSetting, FloatSetting...
 *    2. Etendre SlamSettings
 *    3. D�clarer un nouveau champ public static du nouveau type de Setting
 *    4. Red�finir la m�thode init() pour appeler super.init() et construire
 *    le Setting
 */
public class SlamSettings {
	public static SfxActivatedSetting SFX_ACTIVATED;
	public static SfxVolumeSetting SFX_VOLUME;
	public static MusicActivatedSetting MUSIC_ACTIVATED;
	public static MusicVolumeSetting MUSIC_VOLUME;
	public static FacebookAccountSetting FACEBOOK_ACCOUNT;
	
	public static void init(String preferencesTag) {
		PreferencesManager preferences = new PreferencesManager(preferencesTag);
		
		SFX_ACTIVATED = new SfxActivatedSetting(preferences);
		SFX_VOLUME = new SfxVolumeSetting(preferences);
		MUSIC_ACTIVATED = new MusicActivatedSetting(preferences);
		MUSIC_VOLUME = new MusicVolumeSetting(preferences);
		FACEBOOK_ACCOUNT = new FacebookAccountSetting(preferences);
	}
}
