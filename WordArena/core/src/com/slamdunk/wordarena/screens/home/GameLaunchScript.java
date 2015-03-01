package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class GameLaunchScript extends SimpleButtonI18NScript {
	private HomeScreen screen;
	
	public GameLaunchScript(HomeScreen screen) {
		this.screen = screen;
	}

	@Override
	public void init(final CompositeItem item) {
		super.init(item);
		
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final String arenaFile = item.getCustomVariables().getStringVariable("arenaFile");
				if (arenaFile == null
				|| arenaFile.isEmpty()) {
					return;
				}
				screen.launchGame(arenaFile);
			}
		});
	}
}
