package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.layout.json.JsonUICreator;

public class WindowJsonBuilder extends TableJsonBuilder {

	public WindowJsonBuilder(JsonUICreator creator) {
		super(creator);
	}

	@Override
	protected Window createEmpty(Skin skin, String style) {
		return new Window("", skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère la propriété layout.
		// Cela doit être fait AVANT l'appel à super.build() car
		// si un layout est spécifié alors on va inclure son contenu
		// dans actorDescription de façon à déléguer la création
		// des widgets à TableJsonBuilder.
		parseLayout();
		
		// Gère les propriétés basiques du widget et celles de Table
		Window window = (Window)super.build(skin);
		
		// Gère la propriété title
		parseTitle(window);
		
		// Gère la propriété title-align
		parseTitleAlign(window);
		
		// Gère la propriété movable
		parseMovable(window);
		
		// Gère la propriété modal
		parseModal(window);
		
		// Gère la propriété keep-within-stage
		parseKeepWithinStage(window);
		
		// Gère la propriété resizable
		parseResizable(window);
		
		// Gère la propriété resize-border
		parseResizeBorder(window);
		
		return window;
	}

	private void parseResizeBorder(Window window) {
		if (hasProperty("resize-border")) {
			window.setResizeBorder(actorDescription.getInt("resize-border"));
		}
	}

	private void parseResizable(Window window) {
		if (hasProperty("resizable")) {
			window.setResizable(actorDescription.getBoolean("resizable"));
		}
	}

	private void parseKeepWithinStage(Window window) {
		if (hasProperty("keep-within-stage")) {
			window.setKeepWithinStage(actorDescription.getBoolean("keep-within-stage"));
		}
	}

	private void parseModal(Window window) {
		if (hasProperty("modal")) {
			window.setModal(actorDescription.getBoolean("modal"));
		}
	}

	private void parseMovable(Window window) {
		if (hasProperty("movable")) {
			window.setMovable(actorDescription.getBoolean("movable"));
		}
	}

	private void parseTitle(Window window) {
		if (hasProperty("title")) {
			window.setTitle(actorDescription.getString("title"));
		}
	}
	
	private void parseTitleAlign(Window window) {
		if (hasProperty("title-align")) {
			String align = actorDescription.getString("title-align");
			int alignInt = 0;
			if ("left".equals(align)) {
				alignInt = Align.left;
			} else if ("right".equals(align)) {
				alignInt = Align.right;
			} else if ("center".equals(align)) {
				alignInt = Align.center;
			}
			window.setTitleAlignment(alignInt);
		}
	}

	private void parseLayout() {
		if (hasProperty("layout")) {
			// Ouverture du fichier
			String layout = actorDescription.getString("layout");
			FileHandle file = Gdx.files.internal(layout);
			
			// Lecture de la racine
			JsonValue root = new JsonReader().parse(file);
			
			// Recherche de la dernière propriété de actorDescription
			JsonValue lastActorDescriptionEntry;
			for (lastActorDescriptionEntry = actorDescription.child; lastActorDescriptionEntry.next != null; lastActorDescriptionEntry = lastActorDescriptionEntry.next);
			
			// Ajout du layout à la fin de actorDescription
			for (JsonValue entry = root.child; entry != null; entry = entry.next) {
				lastActorDescriptionEntry.next = entry;
				lastActorDescriptionEntry = entry;
			}
		}
	}
}
