package com.slamdunk.toolkit.gameparts.components.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class UISliderPart extends UIComponent {
	public float minValue;
	public float currentValue;
	public float maxValue;
	public float stepValue;
	public boolean verticalOriented;
	
	private Slider slider;
	
	@Override
	public void reset() {
		minValue = 0;
		currentValue = 0;
		maxValue = 100;
		stepValue = 1;
		verticalOriented = false;
	}
	
	@Override
	public void init() {
		slider = new Slider(minValue, maxValue, stepValue, verticalOriented, skin);
		slider.setValue(currentValue);
		actor = slider;
		super.init();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		currentValue = slider.getValue();
	}
}
