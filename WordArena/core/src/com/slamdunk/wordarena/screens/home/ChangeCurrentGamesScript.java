package com.slamdunk.wordarena.screens.home;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slamdunk.wordarena.enums.GameTypes;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

public class ChangeCurrentGamesScript extends SimpleButtonScript {
	private HomeUI ui;
	
	public ChangeCurrentGamesScript(HomeUI ui) {
		this.ui = ui;
	}

	@Override
	public void init(final CompositeItem item) {
		super.init(item);
		
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final String gameType = item.getCustomVariables().getStringVariable("gameType");
				if (gameType == null
				|| gameType.isEmpty()) {
					return;
				}
				ui.loadCurrentGames(GameTypes.valueOf(gameType));
			}
		});
	}
}
