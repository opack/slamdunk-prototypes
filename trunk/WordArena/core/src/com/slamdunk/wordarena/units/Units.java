package com.slamdunk.wordarena.units;

import com.slamdunk.wordarena.screens.GameScreen;

public enum Units {
	PALADIN {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Paladin(game);
		}
	},
	RANGER {
		@Override
		public SimpleUnit create(GameScreen game) {
			return new Ranger(game);
		}
	},
	
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
	};
	
	public abstract SimpleUnit create(GameScreen game);
}
