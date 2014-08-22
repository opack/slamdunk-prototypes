package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.JsonComponentBuilder;

/**
 * Classe abstraite qui sert de mère aux layout widgets. Ce sont des
 * widgets qui contiennent d'autres widgets.
 */
public abstract class LayoutJsonBuilder extends JsonComponentBuilder {
	private JsonUIBuilder creator;
	
	public LayoutJsonBuilder(JsonUIBuilder creator) {
		this.creator = creator;
	}
	
	public JsonUIBuilder getCreator() {
		return creator;
	}
}
