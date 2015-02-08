/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.slamdunk.wordarena_ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.settings.SlamSettings;

public class Assets {
	public static Skin skin;
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static DoubleEntryArray<Letters, CellStates, TextureRegion> letterData;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		skin = new Skin(Gdx.files.internal("skins/wordarena/uiskin.json"));
		loadLetters();
		background = loadTexture("textures/background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
	}

	public static void playSound (Sound sound) {
		if (SlamSettings.SFX_ACTIVATED.get()) {
			sound.play(SlamSettings.SFX_VOLUME.get());
		}
	}
	
	private static void loadLetters() {
		letterData = new DoubleEntryArray<Letters, CellStates, TextureRegion>();
		final TextureRegion[][] textures = splitSpriteSheet("textures/letters.png", Letters.values().length, CellStates.values().length);
		for (CellStates state : CellStates.values()) {
			for (Letters letter : Letters.values()) {
				letterData.put(letter, state, textures[state.ordinal()][letter.ordinal()]);
			}
		}
	}
	
	private static TextureRegion[][] splitSpriteSheet(String file, int nbCols, int nbRows) {
		Texture spriteSheet = new Texture(Gdx.files.internal(file));
		return TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / nbCols,
				spriteSheet.getHeight() / nbRows);
	}
}
