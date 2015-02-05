package com.slamdunk.wordarena.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;
import com.slamdunk.wordarena.ecs.WordArenaGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.resolutionStrategy = new RatioResolutionStrategy(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		config.hideStatusBar = true;
		initialize(new WordArenaGame(), config);
	}
}
