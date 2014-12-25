package com.slamdunk.wordarena.withgameparts.prefabs;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.gameparts.components.AnimatorComponent;
import com.slamdunk.toolkit.gameparts.components.SpriteRendererComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class Castle extends GameObject {
	
	public Castle() {
		AnimatorComponent animator = new AnimatorComponent();
		animator.spriteSheet = "textures/castle_idle.png";
		animator.nbCols = 8;
		animator.nbRows = 1;
		animator.frameDuration = 0.0675f;
		animator.playMode = PlayMode.LOOP;
		addComponent(animator);
		
		addComponent(new SpriteRendererComponent());
	}
}
