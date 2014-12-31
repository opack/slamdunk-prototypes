package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class UISliderPart extends UIComponent {
	public float minValue;
	public float currentValue;
	public float maxValue;
	public float stepSize;
	
	/**
	 * Utilisée uniquement lors de la création du widget,
	 * non modifiable à runtime.
	 */
	public boolean verticalOriented;
	
	private Slider slider;
	
	@Override
	public void reset() {
		minValue = 0;
		currentValue = 0;
		maxValue = 100;
		stepSize = 1;
		verticalOriented = false;
	}
	
	@Override
	public void init() {
		slider = new Slider(minValue, maxValue, stepSize, verticalOriented, skin);
		slider.setValue(currentValue);
		actor = slider;
		super.init();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		currentValue = slider.getValue();
	}

	@Override
	public void updateWidget() {
		// Attention ! L'ordre de ces méthodes est important !
		if (slider.getStepSize() != stepSize) {
			slider.setStepSize(stepSize);
		}
		if (slider.getMinValue() != minValue
		|| slider.getMaxValue() != maxValue) {
			slider.setRange(minValue, maxValue);
		}
		if (slider.getValue() != currentValue) {
			slider.setValue(currentValue);
		}
	}
}
