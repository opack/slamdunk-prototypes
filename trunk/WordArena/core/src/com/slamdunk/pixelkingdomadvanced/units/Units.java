package com.slamdunk.pixelkingdomadvanced.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.pixelkingdomadvanced.units.troups.Archer;
import com.slamdunk.pixelkingdomadvanced.units.troups.Paladin;

public enum Units {
// Unités du joueur
	PALADIN {
		@Override
		public Image getButtonImage() {
			return new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Paladin.IMAGE_SPAWN_BUTTON))));
		}
	},
	ARCHER {
		@Override
		public Image getButtonImage() {
			return new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Archer.IMAGE_SPAWN_BUTTON))));
		}
	},

// Unités ennemies
	NINJA,
	IMP,

// Projectiles
	ARROW,

// Bâtiments
	CASTLE;
	
	public Image getButtonImage() {
		return null;
	}
}
