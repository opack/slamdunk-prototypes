package com.slamdunk.pixelkingdomadvanced.gameparts.prefabs;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.slamdunk.toolkit.gameparts.components.logic.AnimatorPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class Castle extends GameObject {
	
	public Castle() {
		AnimatorPart animator = addComponent(AnimatorPart.class);
		animator.spriteSheet = "textures/castle_idle.png";
		animator.nbCols = 8;
		animator.nbRows = 1;
		animator.frameDuration = 0.0675f;
		animator.playMode = PlayMode.LOOP;
	}
}
