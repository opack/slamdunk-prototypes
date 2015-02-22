package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Représente une barre de progression ou un slider
 */
public class UIProgressBarPart extends UIComponent {
	public float minValue;
	public float currentValue;
	public float maxValue;
	public float stepSize;
	
	/**
	 * Utilisée uniquement lors de la création du widget,
	 * non modifiable à runtime.
	 */
	public boolean verticalOriented;
	
	/**
	 * Utilisée uniquement lors de la création du widget,
	 * non modifiable à runtime.
	 */
	public boolean isSlider;
	
	private ProgressBar progressBar;
	
	@Override
	public void reset() {
		minValue = 0;
		currentValue = 0;
		maxValue = 100;
		stepSize = 1;
		verticalOriented = false;
		isSlider = false;
	}
	
	@Override
	public void init() {
		if (isSlider) {
			progressBar = new Slider(minValue, maxValue, stepSize, verticalOriented, skin);
		} else {
			progressBar = new ProgressBar(minValue, maxValue, stepSize, verticalOriented, skin);
		}
		progressBar.setValue(currentValue);
		actor = progressBar;
		super.init();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		currentValue = progressBar.getValue();
	}

	@Override
	public void updateWidget() {
		// Attention ! L'ordre de ces méthodes est important !
		if (progressBar.getStepSize() != stepSize) {
			progressBar.setStepSize(stepSize);
		}
		if (progressBar.getMinValue() != minValue
		|| progressBar.getMaxValue() != maxValue) {
			progressBar.setRange(minValue, maxValue);
		}
		if (progressBar.getValue() != currentValue) {
			progressBar.setValue(currentValue);
		}
	}
}
