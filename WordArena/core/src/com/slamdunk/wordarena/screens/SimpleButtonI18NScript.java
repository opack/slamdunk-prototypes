package com.slamdunk.wordarena.screens;

import com.slamdunk.wordarena.Assets;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

/**
 * Récupère la propriété i18n pour obtenir le libellé correspondant à la langue
 * et le ranger dans la propriété text pour que le SimpleButtonScript affiche le
 * texte du bouton 
 */
public class SimpleButtonI18NScript extends SimpleButtonScript {
	@Override
	public void init(CompositeItem item) {
		// Récupère la clé
		String i18nKey = item.getCustomVariables().getStringVariable("i18n-key");
		if (i18nKey != null) {
			String text = Assets.i18nBundle.get(i18nKey);
			if (text != null) {
				// Affecte le texte correspondant
				item.getCustomVariables().setVariable("text", text);
			}
		}
        
		// Laisse la classe mère faire son initialisation pour lire le texte
		super.init(item);
	}
}
