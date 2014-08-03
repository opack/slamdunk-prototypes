package com.slamdunk.wordarena.old.numberduel;

public enum Rules {
	FREE {
		@Override
		public String getRule() {
			return "Chiffres par ordre croissant";
		}
		
		@Override
		public int getRepresentation() {
			return 7;
		}
	},
	EVEN {
		@Override
		public boolean isValidCell(GridCell cell) {
			return cell.value.getValue() % 2 == 0;
		}

		@Override
		public String getRule() {
			return "Chiffres pairs";
		}
	},
	ODD {
		@Override
		public boolean isValidCell(GridCell cell) {
			return cell.value.getValue() % 2 == 1;
		}

		@Override
		public String getRule() {
			return "Chiffres impairs";
		}
	},
	MAX_5 {
		@Override
		public boolean isValidCell(GridCell cell) {
			return cell.value.getValue() <= 5;
		}

		@Override
		public String getRule() {
			return "Chiffres inf�rieurs ou �gaux � 5";
		}
	},
	MIN_5 {
		@Override
		public boolean isValidCell(GridCell cell) {
			return cell.value.getValue() >= 5;
		}

		@Override
		public String getRule() {
			return "Chiffres sup�rieurs ou �gaux � 5";
		}
	},
	PLUS {
		@Override
		public boolean isValidNextPosition(GridCell previousCell, GridCell cell) {
			return ((cell.x == previousCell.x && Math.abs(cell.y - previousCell.y) == 1)
				|| (cell.y == previousCell.y && Math.abs(cell.x - previousCell.x) == 1));
		}

		@Override
		public String getRule() {
			return "Choisir des chiffres en +";
		}
	},
	X {
		@Override
		public boolean isValidNextPosition(GridCell previousCell, GridCell cell) {
			return Math.abs(cell.x - previousCell.x) == 1
				&& Math.abs(cell.y - previousCell.y) == 1;
		}

		@Override
		public String getRule() {
			return "Choisir des chiffres en diagonale";
		}
	},
	NEXT {
		@Override
		public boolean isValidNextValue(GridCell previousCell, GridCell cell) {
			return Math.abs(cell.value.getValue() - previousCell.value.getValue()) == 1;
		}

		@Override
		public String getRule() {
			return "Chiffres imm�diatement apr�s";
		}
	};
	
	/**
	 * Indique si la cellule peut �tre s�lectionn�e pour la r�gle. C'est principalement
	 * un test sur sa valeur qui est effectu� ici.
	 * @param cell
	 * @return
	 */
	public boolean isValidCell(GridCell cell) {
		return true;
	}
	
	/**
	 * Indique si la cellule cell peut �tre s�lectionn�e apr�s la cellule previousCell.
	 * On suppose que isValidCell() a �t� appel�e pour les 2 cellules.
	 * C'est principalement un test de position d'une cellule par rapport � l'autre
	 * et de leur valeur l'une par rapport � l'autre qui sont effectu�s ici.
	 * Par d�faut, on s'assure que la nouvelle cellule est autour de la pr�c�dente
	 * et que sa valeur est sup�rieure � la pr�c�dente (ou qu'elle a un bonus INFERIOR).
	 * @param previousCell
	 * @param cell
	 * @return
	 */
	public boolean isValidNextCell(GridCell previousCell, GridCell cell) {
		return isValidNextPosition(previousCell, cell) && isValidNextValue(previousCell, cell);
	}
	
	/**
	 * Indique si la position de la nouvelle cellule est valide par rapport � celle
	 * de la pr�c�dente.
	 * Par d�faut, elle est valide si elle est autour de la pr�c�dente.
	 * @param previousCell
	 * @param cell
	 * @return
	 */
	protected boolean isValidNextPosition(GridCell previousCell, GridCell cell) {
		// La cellule est-elle autour de la pr�c�dente ?
		return previousCell.isNeighbor(cell);
	}
	
	/**
	 * Indique si la valeur de la nouvelle cellule est valide par rapport � celle
	 * de la pr�c�dente.
	 * Par d�faut, elle est valide si elle est sup�rieure ou que c'est un bonus
	 * INFERIOR
	 * @param previousCell
	 * @param cell
	 * @return
	 */
	protected boolean isValidNextValue(GridCell previousCell, GridCell cell) {
		// La valeur est-elle sup�rieure � la pr�c�dente
		return (previousCell.value.getValue() < cell.value.getValue()
		// Ou est-ce un bonus INFERIOR ?
			|| cell.bonus == Bonus.INFERIOR);
	}
	
	public abstract String getRule();

	public int getRepresentation() {
		return 1;
	}
}
