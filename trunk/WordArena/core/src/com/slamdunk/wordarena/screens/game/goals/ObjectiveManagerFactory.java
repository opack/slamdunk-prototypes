package com.slamdunk.wordarena.screens.game.goals;

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
