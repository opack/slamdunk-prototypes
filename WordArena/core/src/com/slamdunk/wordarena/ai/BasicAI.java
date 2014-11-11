package com.slamdunk.wordarena.ai;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.wordarena.screens.GameScreen;
import com.slamdunk.wordarena.units.SimpleUnit;
import com.slamdunk.wordarena.units.Units;

/**
 * AI simple : spawn une unité à des moments aléatoires
 */
public class BasicAI implements AI {
	private static final float MIN_SPAWN_INTERVAL = 1;
	private static final float MAX_SPAWN_INTERVAL = 5;
	
	private GameScreen game;
	private List<Path> paths;
	
	private float nextSpawn;
	private float interval;
	
	public BasicAI(GameScreen game, List<Path> paths) {
		this.game = game;
		this.paths = paths;
		
		nextSpawn = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL);
	}
	
	@Override
	public void act(float delta) {
		interval += delta;
		if (interval > nextSpawn && !paths.isEmpty()) {
			// Choix d'une unité au hasard
			int choosenUnit = MathUtils.random(9);
			SimpleUnit unit;
			if (choosenUnit == 0) {
				// 1 chance sur 10 de créer un Imp
				unit = Units.IMP.create(game);
			} else {
				unit = Units.NINJA.create(game);
			}
			// Choix d'un chemin au hasard
			int choosenPathIndex = MathUtils.random(paths.size() - 1);
			
			// Ajout de l'unité dans le monde
			game.spawnUnit(unit, paths.get(choosenPathIndex));
			
			// Choix du prochain moment de spawn
			nextSpawn = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL);
			interval = 0;
		}
	}

}
