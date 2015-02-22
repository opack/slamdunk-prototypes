package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.toolkit.world.SlamWorld;


public class WorldOverlay extends StageOverlay {
	
	private SlamWorld world;
	
	public SlamWorld getWorld() {
		return world;
	}

	@Override
	public void createStage(Viewport viewport) {
		super.createStage(viewport);
		world = new SlamWorld(viewport);
		getStage().addActor(world);
	}
	
	@Override
	public boolean isProcessInputs() {
		return true;
	}
}
