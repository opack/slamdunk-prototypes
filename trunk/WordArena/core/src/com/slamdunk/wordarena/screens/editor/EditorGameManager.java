package com.slamdunk.wordarena.screens.editor;

import com.slamdunk.wordarena.GameManager;
import com.slamdunk.wordarena.WordSelectionHandler;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.GameStates;

public class EditorGameManager extends GameManager {
	@Override
	public void changeState(GameStates newState) {
	}
	
	@Override
	public WordSelectionHandler getWordSelectionHandler() {
		return null;
	}
	
	@Override
	public void nextRound() {
	}
	
	@Override
	public void validateWord() {
	}
	
	@Override
	public void cancelWord() {
	}
	
	@Override
	public void refreshStartingZone() {
	}
	
	@Override
	public void requestBack() {
	}
	
	@Override
	public void pause() {
	}
	
	@Override
	public void zoneChangedOwner(Player oldOwner, Player newOwner) {
	}
}
