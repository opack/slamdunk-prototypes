package com.slamdunk.wordarena.gameparts.scripts;

import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.ui.UIProgressBarPart;

public class PathSpeedTweakerScript extends Component {
	public PathFollowerScript pathFollower;
	
	private UIProgressBarPart slider;
	private float oldSliderValue;
	
	@Override
	public void init() {
		slider = gameObject.getComponent(UIProgressBarPart.class);
		if (slider == null) {
			throw new IllegalStateException("PathSpeedTweakerScript cannot work properly since the GameObject it is attached to does not have a UISliderPart.");
		}
		
		if (pathFollower == null) {
			throw new IllegalArgumentException("Missin pathFollower parameter.");
		}
		
		updateSliderFromFollower();
	}
	
	@Override
	public void update(float deltaTime) {
		// Si la valeur du slider a changé
		if (slider.currentValue != oldSliderValue) {
			updateFollowerFromSlider();
		}
		// Si la vitesse a été changée dans le followerscript
		if (pathFollower.speed != oldSliderValue) {
			updateSliderFromFollower();
		}
	}
	
	/**
	 * Met à jour la vitesse du follower à partir de la valeur du slider
	 */
	private void updateFollowerFromSlider() {
		pathFollower.speed = slider.currentValue;
		pathFollower.updateCursor();
		oldSliderValue = slider.currentValue;
	}
	
	/**
	 * Met à jour la valeur du slider à partir de la vitesse du follower
	 */
	private void updateSliderFromFollower() {
		slider.currentValue = pathFollower.speed;
		slider.updateWidget();
		oldSliderValue = slider.currentValue;
	}
}
