package com.slamdunk.toolkit.gameparts.components;

import com.badlogic.gdx.math.Vector3;


public class TransformComponent extends Component {
	public Vector3 position;
	public Vector3 rotation;
	public Vector3 scale;
	public Vector3 origin;
	
	public TransformComponent() {
		position = new Vector3();
		rotation = new Vector3();
		scale = new Vector3(1, 1, 1);
		origin = new Vector3();
	}
}
