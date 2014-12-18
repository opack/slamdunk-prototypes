package com.slamdunk.wordarena.screens.game.goals;

public class GoalManagerFactory {
	public static GoalManager create(Goals objective) {
		GoalManager manager = null;
		switch (objective) {
		case ELIMINATION:
			manager = new EliminationGoal();
			break;
		}
		return manager;
	}
}
