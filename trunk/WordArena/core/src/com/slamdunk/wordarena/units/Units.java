package com.slamdunk.wordarena.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.wordarena.screens.game.GameScreen;
import com.slamdunk.wordarena.units.buildings.Castle;
import com.slamdunk.wordarena.units.projectiles.Arrow;
import com.slamdunk.wordarena.units.troups.Archer;
import com.slamdunk.wordarena.units.troups.Imp;
import com.slamdunk.wordarena.units.troups.Ninja;
import com.slamdunk.wordarena.units.troups.Paladin;

public enum Units {
// Unités du joueur
	PALADIN {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Paladin(game);
		}
		
		@Override
		public Image getButtonImage() {
			return new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Paladin.IMAGE_SPAWN_BUTTON))));
		}
	},
	ARCHER {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Archer(game);
		}
		

		@Override
		public Image getButtonImage() {
			return new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Archer.IMAGE_SPAWN_BUTTON))));
		}
	},

// Unités ennemies
	NINJA {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Ninja(game);
		}
	},
	IMP {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Imp(game);
		}
	},

// Projectiles
	ARROW {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Arrow(game);
		}
	},

// Bâtiments
	CASTLE {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Castle(game);
		}
	};
	
	public abstract SimpleUnit create(GameScreen game);
	public Image getButtonImage() {
		return null;
	}
}
