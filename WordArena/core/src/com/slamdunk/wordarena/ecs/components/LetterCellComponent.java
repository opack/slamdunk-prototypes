package com.slamdunk.wordarena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.slamdunk.wordarena.ecs.CellStates;
import com.slamdunk.wordarena.ecs.Letters;

public class LetterCellComponent extends Component {
	public CellStates type = CellStates.NORMAL;
	public Letters letter = Letters.A;
}
