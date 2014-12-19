package com.slamdunk.wordarena.screens.battlefield.goals;

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
