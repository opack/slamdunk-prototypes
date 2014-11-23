package com.slamdunk.wordarena.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FixedResolutionStrategy;
import com.slamdunk.wordarena.WordArenaGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.resolutionStrategy = new FixedResolutionStrategy(800, 480);
		config.hideStatusBar = true;
		initialize(new WordArenaGame(), config);
	}
}
