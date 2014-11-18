package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay.TiledMapInputProcessor;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;

public class BattlefieldOverlay extends TiledMapOverlay implements TiledMapInputProcessor {

	private List<Path> playerPaths;
	private List<Path> enemyPaths;
	
	public BattlefieldOverlay() {
		playerPaths = new ArrayList<Path>();
		// Définit le gestionnaire des entrées utilisateur
		setTileInputProcessor(this);
	}
	
	public void init(String mapFile) {
		// Chargement d'une tiledmap
		load(mapFile);
		// Initialise le pathfinder
		initPathfinder(false);
		setWalkables("markers", RectangleMapObject.class, "type", "path");
		
		// Recherche les chemins depuis le château vers les attackPoints du château adverse
		playerPaths = searchPathsToAttackPoints("castle1", "castle2");
		playerPaths.addAll(searchPathsToAttackPoints("castle1", "castle3"));
		
		enemyPaths = searchPathsToAttackPoints("castle2", "castle1");
		enemyPaths.addAll(searchPathsToAttackPoints("castle3", "castle1"));
		
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
	 * Recherche les chemins depuis le château fromCastle vers les
	 * attackPoints du château toCastle
	 * @param string
	 * @param string2
	 * @return
	 */
	private List<Path> searchPathsToAttackPoints(String fromCastle, String toCastle) {
	    MapObject castle = getObject("markers", fromCastle);
	    MapObjects attackPoints = getObjects("markers", RectangleMapObject.class, "attackPoint", toCastle);
	    List<Path> paths = new ArrayList<Path>();
	    for (MapObject attackPoint : attackPoints) {
	    	Path path = findPath(castle, attackPoint);
	    	if (path != null) {
	    		paths.add(path);
	    	}
	    }
		return paths;
	}
	
	@Override
	public boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition) {
		return false;
	}


	@Override
	public boolean tileTouchDown(Vector3 worldPosition, Point tilePosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean tileTouchUp(Vector3 worldPosition, Point tilePosition) {
		((GameScreen)getScreen()).tileTouched(worldPosition, tilePosition);
		return true;
	}
}
