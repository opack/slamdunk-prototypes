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
 * Classe abstraite qui sert de m�re aux layout widgets. Ce sont des
 * widgets qui contiennent d'autres widgets.
 * Les LayoutJsonBuilder ont notamment la possibilit� de d�clarer
 * leur contenu dans un autre fichier json. Ce fichier est d�sign�
 * au moyen de la propri�t� <code>layout</code>. Les descriptions
 * trouv�es dans ce fichier seront ajout�es � celles lues dans le
 * Json du Layout ; c'est donc une inclusion qui est r�alis�e.
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
		// G�re la propri�t� layout.
		// Cela doit �tre fait AVANT l'appel � super.build() car
		// si un layout est sp�cifi� alors on va inclure son contenu
		// dans actorDescription de fa�on � d�l�guer la cr�ation
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
			
			// Recherche de la derni�re propri�t� de actorDescription
			JsonValue lastActorDescriptionEntry;
			for (lastActorDescriptionEntry = getWidgetDescription().child; lastActorDescriptionEntry.next != null; lastActorDescriptionEntry = lastActorDescriptionEntry.next);
			
			// Ajout du layout � la fin de actorDescription
			for (JsonValue entry = root.child; entry != null; entry = entry.next) {
				lastActorDescriptionEntry.next = entry;
				lastActorDescriptionEntry = entry;
			}
		}
	}
}
