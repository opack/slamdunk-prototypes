package com.slamdunk.wordarena.components;

import com.badlogic.ashley.core.Component;
import com.slamdunk.wordarena.CellStates;
import com.slamdunk.wordarena.letters.Letters;

public class LetterCellComponent extends Component {
	public CellStates type = CellStates.NORMAL;
	public Letters letter = Letters.A;
}
