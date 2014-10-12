package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.slamdunk.toolkit.ui.Popup;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class PopupJsonBuilder extends WindowJsonBuilder {

	public PopupJsonBuilder(JsonUIBuilder creator) {
		super(creator);
	}

	@Override
	protected Window createEmpty(Skin skin, String style) {
		return new Popup("", skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget, celles de Table et de Window
		Popup popup = (Popup)super.build(skin);
		
		// Gère la propriété message-widget
		parseMessageWidget(popup);
		
		// Gère la propriété ok-widget
		parseOkWidget(popup);
		
		// Gère la propriété cancel-widget
		parseCancelWidget(popup);
		
		// Gère la propriété center-on-show
		parseCenterOnShow(popup);
		
		return popup;
	}
	
	private void parseCenterOnShow(Popup popup) {
		if (hasProperty("center-on-show")) {
			popup.setCenterOnShow(getBooleanProperty("center-on-show"));
		}
	}

	private void parseMessageWidget(Popup popup) {
		if (hasProperty("message-widget")) {
			Actor messageWidget = popup.findActor(getStringProperty("message-widget"));
			if (messageWidget != null) {
				popup.setMessageWidget((Label)messageWidget);
			}
		}
	}
	
	private void parseOkWidget(Popup popup) {
		if (hasProperty("ok-widget")) {
			Actor okWidget = popup.findActor(getStringProperty("ok-widget"));
			if (okWidget != null) {
				popup.setOkWidget((TextButton)okWidget);
			}
		}
	}
	
	private void parseCancelWidget(Popup popup) {
		if (hasProperty("cancel-widget")) {
			Actor cancelWidget = popup.findActor(getStringProperty("cancel-widget"));
			if (cancelWidget != null) {
				popup.setCancelWidget((TextButton)cancelWidget);
			}
		}
	}
}
