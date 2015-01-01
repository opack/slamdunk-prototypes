package com.slamdunk.toolkit.gameparts.components.position;

import com.slamdunk.toolkit.gameparts.components.Component;

public class SizePart extends Component {
	public float width;
	public float height;
	public float depth;
	
	@Override
	public void reset() {
		width = 0;
		height = 0;
		depth = 0;
	}
}
