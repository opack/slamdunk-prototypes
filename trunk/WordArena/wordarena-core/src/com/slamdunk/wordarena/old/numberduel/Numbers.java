package com.slamdunk.wordarena.old.numberduel;

public enum Numbers {
	ONE {
		@Override
		public int getValue() {
			return 1;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	TWO {
		@Override
		public int getValue() {
			return 2;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	THREE {
		@Override
		public int getValue() {
			return 3;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	FOUR {
		@Override
		public int getValue() {
			return 4;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	FIVE {
		@Override
		public int getValue() {
			return 5;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	SIX {
		@Override
		public int getValue() {
			return 6;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	SEVEN {
		@Override
		public int getValue() {
			return 7;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	EIGHT {
		@Override
		public int getValue() {
			return 8;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	},
	NINE {
		@Override
		public int getValue() {
			return 9;
		}

		@Override
		public int getRepresentation() {
			return 10;
		}
	};
	
	public abstract int getValue();
	public abstract int getRepresentation();
}
