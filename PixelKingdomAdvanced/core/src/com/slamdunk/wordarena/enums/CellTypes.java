package com.slamdunk.wordarena.enums;

public enum CellTypes {
	/**
	 * Letter : cellule contenant une lettre
	 */
	L,
	
	/**
	 * Void : cellule vide, ne contenant rien
	 */
	V {
		@Override
		public boolean isSelectable() {
			return false;
		}
		
		@Override
		public boolean hasLetter() {
			return false;
		}
		
		@Override
		public boolean canBeOwned() {
			return false;
		}
		
		@Override
		public boolean hasPower() {
			return false;
		}
	},
	
	/**
	 * Joker : case pouvant représenter n'importe quelle lettre
	 * mais ayant une valeur de 0
	 */
	J {
		@Override
		public boolean hasLetter() {
			return false;
		}
		
		@Override
		public boolean hasPower() {
			return false;
		}
	},
	
	/**
	 * Glass : case en verre, contenant une lettre mais ne
	 * pouvant pas être incluse dans un mot tant que le verre
	 * n'aura pas été brisé en faisant un mot à côté.
	 */
	G {
		@Override
		public boolean isSelectable() {
			// Le verre n'est pas sélectionnable. Lorsqu'il sera
			// cassé, la cellule prendra un type normal (L) et
			// sera alors sélectionnable.
			return false;
		}

		@Override
		public boolean canBeOwned() {
			return false;
		}
	},
	
	/**
	 * Bomb : case supprimant la domination adverse dans toutes les
	 * cases voisines la première fois qu'elle est utilisée
	 */
	B,
	
	/**
	 * Special : case donnant un pouvoir spécial au hasard
	 */
	S {
		@Override
		public boolean hasPower() {
			return false;
		}
	};
	
	/**
	 * Indique si la cellule peut être incluse dans un mot
	 * @return
	 */
	public boolean isSelectable() {
		return true;
	}
	
	/**
	 * Indique si la cellule peut contenir une lettre
	 * @return
	 */
	public boolean hasLetter() {
		return true;
	}
	
	/**
	 * Indique si la cellule peut appartenir à un joueur
	 * @return
	 */
	public boolean canBeOwned() {
		return true;
	}
	
	/**
	 * Indique si la cellule peut avoir une puissance
	 * @return
	 */
	public boolean hasPower() {
		return true;
	}
}
