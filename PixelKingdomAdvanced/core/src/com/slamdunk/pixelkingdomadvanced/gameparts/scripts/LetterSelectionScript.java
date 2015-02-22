package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TouchHandlerPart.TouchHandler;
import com.slamdunk.toolkit.gameparts.components.position.GridPositionPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

public class LetterSelectionScript extends Component implements TouchHandler {
	private List<GameObject> selected;
	private GameObject grid;
	private GameObject lastSelected;
	
	public LetterSelectionScript() {
		selected = new ArrayList<GameObject>();
	}
	
	@Override
	public void init() {
		grid = gameObject.parent;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		gameObject.getComponent(LetterChooserScript.class).state = LetterCaseState.SELECTED;
		lastSelected = gameObject;
		selected.add(gameObject);
		return true;
	}

	@Override
	public boolean touchDragged(float x, float y, int pointer) {
		GameObject letterCase = grid.hit(x, y);
		if (letterCase != null
		&& lastSelected != letterCase
		&& isSelectable(letterCase)) {
			letterCase.getComponent(LetterChooserScript.class).state = LetterCaseState.SELECTED;
			lastSelected = letterCase;
			
			selected.add(letterCase);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Indique si la case est sélectionnable
	 * @param candidate
	 * @return
	 */
	private boolean isSelectable(GameObject candidate) {
		if (!candidate.hasComponent(LetterChooserScript.class)
		|| !candidate.hasComponent(GridPositionPart.class)) {
			return false;
		}
		GridPositionPart lastPosition = lastSelected.getComponent(GridPositionPart.class);
		GridPositionPart candidatePosition = candidate.getComponent(GridPositionPart.class);
		return ( (candidatePosition.column == lastPosition.column) && (Math.abs(candidatePosition.row - lastPosition.row) == 1) )
			|| ( (candidatePosition.row == lastPosition.row) && (Math.abs(candidatePosition.column - lastPosition.column) == 1) );
	}

	@Override
	public boolean touchUp(float x, float y, int pointer, int button) {
		StringBuilder word = new StringBuilder();
		for (GameObject letterCase : selected) {
			word.append(letterCase.getComponent(LetterChooserScript.class).letter);
			letterCase.getComponent(LetterChooserScript.class).state = LetterCaseState.NORMAL;
		}
		System.out.println(word.toString());
		return true;
	}
}
