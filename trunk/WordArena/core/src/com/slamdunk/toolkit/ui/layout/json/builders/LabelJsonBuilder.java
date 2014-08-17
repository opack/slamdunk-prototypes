package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class LabelJsonBuilder extends JsonComponentBuilder {
	private String language;
	
	public LabelJsonBuilder(String language) {
		this.language = language;
	}
	
	@Override
	protected Label createEmpty(Skin skin, String style) {
		if (style == null) {
			return new Label("", skin);
		}
		return new Label("", skin, style);
	}
	
	@Override
	public Label build(Skin skin) {
		// Gère les propriétés basiques du widget
		Label label = (Label)super.build(skin);
		
		// Gère les propriétés spécifiques du Label
		parseTextKey(label);
		parseText(label);
		
		parseAlignKey(label);
		parseAlign(label);
		
		parseWrapKey(label);
		parseWrap(label);
		
		return label;
	}

	protected void parseText(Label label) {
		if (hasProperty("text")) {
			label.setText(actorDescription.getString("text"));
		}
	}
	
	protected void parseTextKey(Label label) {
		if (hasProperty("text-key")) {
			String key = actorDescription.getString("text-key");
			label.setText(getValueString(key, language));
		}
	}
	
	protected void parseAlign(Label label) {
		if (hasProperty("align")) {
			applyAlign(label, actorDescription.getString("align"));
		}
	}
	
	protected void parseAlignKey(Label label) {
		if (hasProperty("align-key")) {
			String key = actorDescription.getString("align-key");
			applyAlign(label, values.getString(key));
		}
	}
	
	protected void applyAlign(Label label, String align) {
		int alignInt = 0;
		if (align.contains("top")) {
			alignInt |= Align.top;
		} else if (align.contains("bottom")) {
			alignInt |= Align.bottom;
		}
		if (align.contains("left")) {
			alignInt |= Align.left;
		} else if (align.contains("right")) {
			alignInt |= Align.right;
		}
		if (align.contains("center")) {
			alignInt |= Align.center;
		}
		label.setAlignment(alignInt);
	}
	
	protected void parseWrap(Label label) {
		if (hasProperty("wrap")) {
			label.setWrap(actorDescription.getBoolean("wrap"));
		}
	}
	
	protected void parseWrapKey(Label label) {
		if (hasProperty("wrap-key")) {
			String key = actorDescription.getString("wrap-key");
			label.setWrap(values.getBoolean(key));
		}
	}
}
