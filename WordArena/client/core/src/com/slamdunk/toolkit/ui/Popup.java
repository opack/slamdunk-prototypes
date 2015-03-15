package com.slamdunk.toolkit.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Popup extends Window {
	private Label messageWidget;
	private TextButton okWidget;
	private TextButton cancelWidget;
	private boolean centerOnShow;

	public Popup(String title, Skin skin) {
		super(title, skin);
	}
	
	public Popup(String title, Skin skin, String styleName) {
		super(title, skin, styleName);
	}

	public Popup(String title, WindowStyle style) {
		super(title, style);
	}

	public Label getMessageWidget() {
		return messageWidget;
	}

	public void setMessageWidget(Label messageWidget) {
		this.messageWidget = messageWidget;
	}

	public TextButton getOkWidget() {
		return okWidget;
	}

	public void setOkWidget(TextButton okWidget) {
		this.okWidget = okWidget;
	}

	public TextButton getCancelWidget() {
		return cancelWidget;
	}

	public void setCancelWidget(TextButton cancelWidget) {
		this.cancelWidget = cancelWidget;
	}

	public void setMessage(String message) {
		messageWidget.setText(message);
	}
	
	public boolean isCenterOnShow() {
		return centerOnShow;
	}

	public void setCenterOnShow(boolean centerOnShow) {
		this.centerOnShow = centerOnShow;
	}

	public void show() {
		pack();
		if (centerOnShow) {
			centerOnScreen();
		}
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}

	public void centerOnScreen() {
		Stage stage = getParent().getStage();
		setPosition(
			(stage.getWidth() - getWidth()) / 2,
			(stage.getHeight() - getHeight()) / 2
		);
	}
}
