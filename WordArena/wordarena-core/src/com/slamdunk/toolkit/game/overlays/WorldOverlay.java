package com.slamdunk.toolkit.game.overlays;

import com.slamdunk.toolkit.world.SlamWorld;


public class WorldOverlay extends SlamStageOverlay {
	
	private SlamWorld world;
	
	public SlamWorld getWorld() {
		return world;
	}

	@Override
	public void createStage(float width, float height) {
		super.createStage(width, height);
		world = new SlamWorld(0, 0, width, height);
	}
}
