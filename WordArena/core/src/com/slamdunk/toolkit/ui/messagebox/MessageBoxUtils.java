package com.slamdunk.toolkit.ui.messagebox;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slamdunk.toolkit.ui.ButtonClickListener;
import com.slamdunk.toolkit.ui.messagebox.MessageBox.MessageBoxStyle;

public class MessageBoxUtils {
	private static boolean isDisplayingBox;
	private static ClickListener toggleDisplayBoxBoolean;
	private static MessageBoxStyle messageBoxStyle;
	private static MessageBoxStyle confirmBoxStyle;
	
	public static void init(
			TextButtonStyle leftButtonStyle, TextButtonStyle rightButtonStyle,
			LabelStyle messageStyle, WindowStyle windowStyle) {
		
		toggleDisplayBoxBoolean = new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				isDisplayingBox = false;
			}
		};
		
		messageBoxStyle = new MessageBoxStyle();
		messageBoxStyle.leftButtonStyle = leftButtonStyle;
		messageBoxStyle.messageStyle = messageStyle;
		messageBoxStyle.windowStyle = windowStyle;
		messageBoxStyle.buttonWidth = 120;
		messageBoxStyle.buttonHeight = 60;
		
		confirmBoxStyle = new MessageBoxStyle();
		confirmBoxStyle.leftButtonStyle = leftButtonStyle;
		confirmBoxStyle.rightButtonStyle = rightButtonStyle;
		confirmBoxStyle.messageStyle = messageStyle;
		confirmBoxStyle.windowStyle = windowStyle;
		confirmBoxStyle.buttonWidth = 120;
		confirmBoxStyle.buttonHeight = 60;
	}
	
	/**
	 * 
	 * @param message
	 * @param stage
	 * @param listener
	 * @return true si la boîte est affichée, false si une autre est déjà en cours d'affichage
	 */
	public static boolean showMessage(String message, Stage stage, InputListener listener) {
		if (isDisplayingBox) {
			return false;
		}
		isDisplayingBox = true;
		
		MessageBoxBuilder builder = new MessageBoxBuilder(stage);
		builder.setStyle(messageBoxStyle);
		builder.addLeftButtonListener(toggleDisplayBoxBoolean);
		if (listener != null) {
			builder.addLeftButtonListener(listener);
		}
		
		MessageBox msg = builder.createMessage("", message, "OK");
		msg.setVisible(true);
		return true;
	}
	
	/**
	 * 
	 * @param message
	 * @param stage
	 * @param okListener
	 * @param backspaceListener
	 * @return true si la boîte est affichée, false si une autre est déjà en cours d'affichage
	 */
	public static boolean showConfirm(String message, Stage stage, EventListener okListener, EventListener backspaceListener) {
		if (isDisplayingBox) {
			return false;
		}
		isDisplayingBox = true;
		MessageBoxBuilder builder = new MessageBoxBuilder(stage);
		builder.setStyle(confirmBoxStyle);
		builder.addLeftButtonListener(toggleDisplayBoxBoolean);
		if (okListener != null) {
			builder.addLeftButtonListener(okListener);
		}
		builder.addRightButtonListener(toggleDisplayBoxBoolean);
		if (backspaceListener != null) {
			builder.addRightButtonListener(backspaceListener);
		}
		
		MessageBox msg = builder.createConfirm("", message, "OK", "Annuler");
		msg.setVisible(true);
		return true;
	}
}
