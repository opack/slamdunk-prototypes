package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
			window.setResizeBorder(getIntProperty("resize-border"));
		}
	}

	private void parseResizable(Window window) {
		if (hasProperty("resizable")) {
			window.setResizable(getBooleanProperty("resizable"));
		}
	}

	private void parseKeepWithinStage(Window window) {
		if (hasProperty("keep-within-stage")) {
			window.setKeepWithinStage(getBooleanProperty("keep-within-stage"));
		}
	}

	private void parseModal(Window window) {
		if (hasProperty("modal")) {
			window.setModal(getBooleanProperty("modal"));
		}
	}

	private void parseMovable(Window window) {
		if (hasProperty("movable")) {
			window.setMovable(getBooleanProperty("movable"));
		}
	}

	private void parseTitle(Window window) {
		if (hasProperty("title")) {
			window.setTitle(getStringProperty("title"));
		}
	}
	
	private void parseTitleAlign(Window window) {
		if (hasProperty("title-align")) {
			String align = getStringProperty("title-align");
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
}
