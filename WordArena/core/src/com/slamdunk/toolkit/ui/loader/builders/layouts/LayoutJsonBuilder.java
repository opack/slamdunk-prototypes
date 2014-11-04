package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.JsonComponentBuilder;

/**
 * Classe abstraite qui sert de mère aux layout widgets. Ce sont des
 * widgets qui contiennent d'autres widgets.
 * Les LayoutJsonBuilder ont notamment la possibilité de déclarer
 * leur contenu dans un autre fichier json. Ce fichier est désigné
 * au moyen de la propriété <code>layout</code>. Les descriptions
 * trouvées dans ce fichier seront ajoutées à celles lues dans le
 * Json du Layout ; c'est donc une inclusion qui est réalisée.
 */
public abstract class LayoutJsonBuilder extends JsonComponentBuilder {
	private JsonUIBuilder creator;
	
	public LayoutJsonBuilder(JsonUIBuilder creator) {
		this.creator = creator;
	}
	
	public JsonUIBuilder getCreator() {
		return creator;
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère la propriété layout.
		// Cela doit être fait AVANT l'appel à super.build() car
		// si un layout est spécifié alors on va inclure son contenu
		// dans actorDescription de façon à déléguer la création
		// des widgets.
		parseLayout();
		
		return super.build(skin);
	}
	
	private void parseLayout() {
		if (hasProperty("layout")) {
			// Ouverture du fichier
			String layout = getStringProperty("layout");
			FileHandle file = Gdx.files.internal(layout);
			
			// Lecture de la racine
			JsonValue root = new JsonReader().parse(file);
			
			// Recherche de la dernière propriété de actorDescription
			JsonValue lastActorDescriptionEntry;
			for (lastActorDescriptionEntry = getWidgetDescription().child; lastActorDescriptionEntry.next != null; lastActorDescriptionEntry = lastActorDescriptionEntry.next);
			
			// Ajout du layout à la fin de actorDescription
			for (JsonValue entry = root.child; entry != null; entry = entry.next) {
				lastActorDescriptionEntry.next = entry;
				lastActorDescriptionEntry = entry;
			}
		}
	}
}
