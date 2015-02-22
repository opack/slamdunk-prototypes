package com.slamdunk.toolkit.gameparts.components.position;

import com.slamdunk.toolkit.gameparts.components.Component;

public class BoundsPart extends Component {
	public float x;
	public float y;
	public float width;
	public float height;
	
	@Override
	public void reset() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	public boolean contains (float x, float y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}
}
