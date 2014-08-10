package com.slamdunk.wordarena.old.numberduel;

public enum Bonus {
	SIMPLE_LETTER {
		@Override
		public String getNormalStyle() {
			return "grid-number-1L";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-1L-selected";
		}
	},
	DOUBLE_LETTER {
		@Override
		public int getLetterValue(int letterValue) {
			return letterValue * 2;
		}

		@Override
		public String getNormalStyle() {
			return "grid-number-2L";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-2L-selected";
		}
	},
	TRIPLE_LETTER {
		@Override
		public int getLetterValue(int letterValue) {
			return letterValue * 3;
		}

		@Override
		public String getNormalStyle() {
			return "grid-number-3L";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-3L-selected";
		}
	},
	DOUBLE_WORD {
		@Override
		public int getTotalValue(int currentTotal) {
			return currentTotal * 2;
		}

		@Override
		public String getNormalStyle() {
			return "grid-number-2M";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-2M-selected";
		}
	},
	TRIPLE_WORD {
		@Override
		public int getTotalValue(int currentTotal) {
			return currentTotal * 3;
		}

		@Override
		public String getNormalStyle() {
			return "grid-number-3M";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-3M-selected";
		}
	},
	HEAL {
		@Override
		public int getTotalValue(int currentTotal) {
			return currentTotal * -1;
		}

		@Override
		public String getNormalStyle() {
			return "grid-number-heal";
		}

		@Override
		public String getSelectedStyle() {
			return "grid-number-heal-selected";
		}
	},
	INFERIOR {
		@Override
		public String getNormalStyle() {
			return "grid-number-inferior";
		}
		
		@Override
		public String getSelectedStyle() {
			return "grid-number-inferior-selected";
		}
	};
	
	public int getLetterValue(int letterValue) {
		return letterValue;
	}
	public int getTotalValue(int currentTotal) {
		return currentTotal;
	}
	public abstract String getNormalStyle();
	public abstract String getSelectedStyle();
}
