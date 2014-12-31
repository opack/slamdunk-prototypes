package com.slamdunk.wordarena.gameparts.scripts;

import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.ui.UISliderPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class PathSpeedTweakerScript extends Component {
	public GameObject pathObject;
	
	private UISliderPart slider;
	private PathFollowerScript path;
	
	private float oldValue;
	
	@Override
	public void init() {
		slider = gameObject.getComponent(UISliderPart.class);
		if (slider == null) {
			throw new IllegalStateException("PathSpeedTweakerScript cannot work properly since the GameObject it is attached to does not have a UISliderPart.");
		}
		
		if (pathObject == null) {
			throw new IllegalArgumentException("Missin pathObject parameter.");
		}
		path = pathObject.getComponent(PathFollowerScript.class);
		if (path == null) {
			throw new IllegalArgumentException("PathSpeedTweakerScript cannot work properly since the referenced pathObject does not have a PathScript.");
		}
		
		slider.currentValue = path.speed;
		oldValue = slider.currentValue;
	}
	
	@Override
	public void update(float deltaTime) {
		if (slider.currentValue != oldValue) {
			path.speed = slider.currentValue;
			path.updateCursor();
			oldValue = slider.currentValue;
		}
	}
}
