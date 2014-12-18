package com.slamdunk.wordarena.screens.game.objectives;

public class ObjectiveManagerFactory {
	public static ObjectiveManager create(Objectives objective) {
		ObjectiveManager manager = null;
		switch (objective) {
		case ELIMINATION:
			manager = new EliminationObjective();
			break;
		}
		return manager;
	}
}
