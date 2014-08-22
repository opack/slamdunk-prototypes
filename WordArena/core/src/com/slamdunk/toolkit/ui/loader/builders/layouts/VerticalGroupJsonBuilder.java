package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class VerticalGroupJsonBuilder extends LayoutJsonBuilder {
	
	public VerticalGroupJsonBuilder(JsonUIBuilder creator) {
		super(creator);
	}

	@Override
	protected VerticalGroup createEmpty(Skin skin, String style) {
		return new VerticalGroup();
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		VerticalGroup verticalGroup = (VerticalGroup)super.build(skin);
		
		// Gère la propriété widgets
		parseWidgets(verticalGroup);
		
		// Gère la propriété fillParent
		parseFillParent(verticalGroup);
		
		// Gère la propriété align
		parseAlign(verticalGroup);
		
		// Gère la propriété pad
		parsePad(verticalGroup);
		
		// Gère la propriété pad-tlbr
		parsePadTLBR(verticalGroup);
		
		// Gère la propriété space
		parseSpace(verticalGroup);
		
		// Gère la propriété reverse
		parseReverse(verticalGroup);
		
		// Gère la propriété fill
		parseFill(verticalGroup);
		
		verticalGroup.pack();
		return verticalGroup;
	}
	
	/**
	 * La propriété fill accepte une valeur boolean ou float.
	 * Si float, indique le pourcentage (0.0-1.0) de la largeur
	 * à occuper.
	 * Si boolean, true représente 1.0 et false représente 0.0.
	 */
	private void parseFill(VerticalGroup verticalGroup) {
		if (hasProperty("fill")) {
			JsonValue jsonValue = getJsonProperty("fill");
			float fill = 0;
			if (jsonValue.isNumber()) {
				fill = jsonValue.asFloat();
			} else if (jsonValue.isBoolean() && jsonValue.asBoolean()) {
				fill = 1;
			}
			verticalGroup.fill(fill);
		}
	}
	
	private void parseReverse(VerticalGroup verticalGroup) {
		if (hasProperty("reverse")) {
			verticalGroup.reverse(getBooleanProperty("reverse"));
		}
	}
	
	private void parseSpace(VerticalGroup verticalGroup) {
		if (hasProperty("space")) {
			verticalGroup.space(getFloatProperty("space"));
		}
	}
	
	private void parsePad(VerticalGroup verticalGroup) {
		if (hasProperty("pad")) {
			verticalGroup.pad(getFloatProperty("pad"));
		}
	}
	
	private void parsePadTLBR(VerticalGroup verticalGroup) {
		if (hasProperty("pad-tlbr")) {
			JsonValue padValues = getJsonProperty("pad-tlbr");
			verticalGroup.pad(padValues.getFloat(0), padValues.getFloat(1), padValues.getFloat(2), padValues.getFloat(3));
		}
	}

	private void parseWidgets(VerticalGroup verticalGroup) {
		if (hasProperty("widgets")) {
			JsonValue widgets = getJsonProperty("widgets");
			JsonValue widget;
			for (int curCell = 0; curCell < widgets.size; curCell++) {
				widget = widgets.get(curCell);
				Actor actor = getCreator().build(widget);
				verticalGroup.addActor(actor);
			}
		}
	}
	
	private void parseFillParent(VerticalGroup verticalGroup) {
		if (hasProperty("fill-parent")) {
			verticalGroup.setFillParent(getBooleanProperty("fill-parent"));
		}
	}
	
	protected void parseAlign(VerticalGroup verticalGroup) {
		if (hasProperty("align")) {
			applyAlign(verticalGroup, getStringProperty("align"));
		}
	}
	
	protected void applyAlign(VerticalGroup verticalGroup, String align) {
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
		verticalGroup.align(alignInt);
	}
}
