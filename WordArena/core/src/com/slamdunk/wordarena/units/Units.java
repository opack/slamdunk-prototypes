package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.units.other.Imp;
import com.slamdunk.wordarena.units.other.Ninja;
import com.slamdunk.wordarena.units.player.Paladin;
import com.slamdunk.wordarena.units.player.Archer;
import com.slamdunk.wordarena.units.projectiles.Arrow;

public enum Units {
// Unités du joueur
	PALADIN {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Paladin(game);
		}
	},
	ARCHER {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Archer(game);
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
	};
	
	public abstract SimpleUnit create(GameScreen game);
}
