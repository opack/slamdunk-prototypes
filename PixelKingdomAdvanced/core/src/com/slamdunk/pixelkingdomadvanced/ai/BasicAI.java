package com.slamdunk.pixelkingdomadvanced.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.pixelkingdomadvanced.screens.battlefield.GameScreen;
import com.slamdunk.pixelkingdomadvanced.units.Factions;
import com.slamdunk.pixelkingdomadvanced.units.Units;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.CursorMode;

/**
 * AI simple : spawn une unité à des moments aléatoires
 */
public class BasicAI implements AI {
	private static final float MIN_SPAWN_INTERVAL = 1;
	private static final float MAX_SPAWN_INTERVAL = 5;
	
	private GameScreen game;
	private Array<ComplexPath> paths;
	
	private float nextSpawn;
	private float interval;
	
	public BasicAI(GameScreen game, Array<ComplexPath> paths) {
		this.game = game;
		this.paths = paths;
		
		nextSpawn = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL);
	}

	@Override
	public void act(float delta) {
		interval += delta;
		if (interval > nextSpawn && paths.size > 0) {
			// Choix d'une unité au hasard
			int choosenUnit = MathUtils.random(9);
			Units unit;
			if (choosenUnit == 0) {
				// 1 chance sur 10 de créer un Imp
				unit = Units.IMP;
			} else {
				unit = Units.NINJA;
			}
			// Choix d'un chemin au hasard
			int choosenPathIndex = MathUtils.random(paths.size - 1);
			
			// Ajout de l'unité dans le monde et la fait partir depuis la fin d'un chemin
			// vers le début, c'est-à-dire vers le château du joueur
			game.getObjectsOverlay().spawnUnit(unit, Factions.ENEMY, paths.get(choosenPathIndex), 1, CursorMode.BACKWARD);
			
			// Choix du prochain moment de spawn
			nextSpawn = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL);
			interval = 0;
		}
	}

}
