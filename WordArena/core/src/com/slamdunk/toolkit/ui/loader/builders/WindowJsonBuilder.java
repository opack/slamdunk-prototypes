package com.slamdunk.toolkit.ui.loader.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class WindowJsonBuilder extends TableJsonBuilder {

	public WindowJsonBuilder(JsonUIBuilder creator) {
		super(creator);
	}

	@Override
	protected Window createEmpty(Skin skin, String style) {
		return new Window("", skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// G�re la propri�t� layout.
		// Cela doit �tre fait AVANT l'appel � super.build() car
		// si un layout est sp�cifi� alors on va inclure son contenu
		// dans actorDescription de fa�on � d�l�guer la cr�ation
		// des widgets � TableJsonBuilder.
		parseLayout();
		
		// G�re les propri�t�s basiques du widget et celles de Table
		Window window = (Window)super.build(skin);
		
		// G�re la propri�t� title
		parseTitle(window);
		
		// G�re la propri�t� title-align
		parseTitleAlign(window);
		
		// G�re la propri�t� movable
		parseMovable(window);
		
		// G�re la propri�t� modal
		parseModal(window);
		
		// G�re la propri�t� keep-within-stage
		parseKeepWithinStage(window);
		
		// G�re la propri�t� resizable
		parseResizable(window);
		
		// G�re la propri�t� resize-border
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
			
			// Recherche de la derni�re propri�t� de actorDescription
			JsonValue lastActorDescriptionEntry;
			for (lastActorDescriptionEntry = actorDescription.child; lastActorDescriptionEntry.next != null; lastActorDescriptionEntry = lastActorDescriptionEntry.next);
			
			// Ajout du layout � la fin de actorDescription
			for (JsonValue entry = root.child; entry != null; entry = entry.next) {
				lastActorDescriptionEntry.next = entry;
				lastActorDescriptionEntry = entry;
			}
		}
	}
}
