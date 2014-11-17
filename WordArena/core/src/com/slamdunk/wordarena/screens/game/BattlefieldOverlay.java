package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.world.pathfinder.Path;

public class BattlefieldOverlay extends TiledMapOverlay {

	private List<Path> playerPaths;
	private List<Path> enemyPaths;
	
	public BattlefieldOverlay() {
		playerPaths = new ArrayList<Path>();
	}
	
	public void init(String mapFile) {
		// Chargement d'une tiledmap
		load(mapFile);
		// Définit le gestionnaire des entrées utilisateur
		setTileInputProcessor(this);
		// Initialise le pathfinder
		initPathfinder(false);
		setWalkables("markers", RectangleMapObject.class, "type", "path");
		
		// Recherche les chemins depuis les points de spawn vers le château adverse
		playerPaths = searchPaths("castle1", "castle2");
		playerPaths.addAll(searchPaths("castle1", "castle3"));
		
		enemyPaths = searchPaths("castle2", "castle1");
		enemyPaths.addAll(searchPaths("castle3", "castle1"));
		
		// Place la camera à l'endroit du premier château
		setCameraOnObject("markers", "castle1");
	}

	public List<Path> getPlayerPaths() {
		return playerPaths;
	}
	
	public List<Path> getEnemyPaths() {
		return enemyPaths;
	}

	/**
	 * Recherche les chemins depuis les emplacements spawn du château
	 * fromCastle vers le château toCastle
	 * @param string
	 * @param string2
	 * @return
	 */
	private List<Path> searchPaths(String fromCastle, String toCastleAttackPoints) {
	    MapObject castle = getObject("markers", fromCastle);
	    MapObjects attackPoints = getObjects("markers", RectangleMapObject.class, "attackPoint", toCastleAttackPoints);
	    List<Path> paths = new ArrayList<Path>();
	    for (MapObject attackPoint : attackPoints) {
	    	Path path = findPath(castle, attackPoint);
	    	if (path != null) {
	    		paths.add(path);
	    	}
	    }
		return paths;
	}
}
