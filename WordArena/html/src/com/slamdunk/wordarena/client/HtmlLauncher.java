package com.slamdunk.wordarena.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.slamdunk.wordarena.WordArenaGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new WordArenaGame();
        }
}