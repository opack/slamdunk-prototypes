package com.slamdunk.wordarena_ecs.components;

import com.badlogic.ashley.core.Component;
import com.slamdunk.wordarena_ecs.CellStates;
import com.slamdunk.wordarena_ecs.Letters;

public class LetterCellComponent extends Component {
	public CellStates type = CellStates.NORMAL;
	public Letters letter = Letters.A;
}
