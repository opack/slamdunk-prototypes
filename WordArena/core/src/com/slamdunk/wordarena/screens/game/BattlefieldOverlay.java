package com.slamdunk.wordarena.screens.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay;
import com.slamdunk.toolkit.screen.overlays.TiledMapOverlay.TiledMapInputProcessor;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.point.Point;

public class BattlefieldOverlay extends TiledMapOverlay implements TiledMapInputProcessor {

	private List<Path> playerPaths;
	private List<Path> enemyPaths;
	
	/**
	 * Précédente position pendant le drag, pour calculer le déplacement
	 * de la caméra
	 */
	private Vector2 touchDownPos;
	
	/**
	 * Indique si on est en train de faire un drag
	 */
	private boolean dragging;
	
	/**
	 * Indique si on est en train de sélectionner des unités sur le champ de bataille
	 */
	private boolean selectingUnits;
	
	public BattlefieldOverlay() {
		playerPaths = new ArrayList<Path>();
		touchDownPos = new Vector2();
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
		Collections.sort(playerPaths);
		
		enemyPaths = searchPathsToAttackPoints("castle2", "castle1");
		enemyPaths.addAll(searchPathsToAttackPoints("castle3", "castle1"));
		Collections.sort(enemyPaths);
		
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
	 * Recherche les chemins depuis les spawnPoints du château fromCastle
	 * vers les attackPoints du château toCastle
	 * @param fromCastle
	 * @param toCastle
	 * @return
	 */
	private List<Path> searchPathsToAttackPoints(String fromCastle, String toCastle) {
	    MapObjects spawnPoints = getObjects("markers", RectangleMapObject.class, "spawnPoint", fromCastle);
	    MapObjects attackPoints = getObjects("markers", RectangleMapObject.class, "attackPoint", toCastle);
	    List<Path> paths = new ArrayList<Path>();
	    for (MapObject spawnPoint : spawnPoints) {
		    for (MapObject attackPoint : attackPoints) {
		    	Path path = findPath(spawnPoint, attackPoint);
		    	if (path != null) {
		    		paths.add(path);
		    	}
		    }
	    }
		return paths;
	}

	@Override
	public boolean tileTouchDown(Vector3 worldPosition, Point tilePosition) {
		touchDownPos.x = worldPosition.x;
		touchDownPos.y = worldPosition.y;
		
		GameScreen screen = ((GameScreen)getScreen());
		selectingUnits = screen.getUIOverlay().isSelectingUnits();
		return true;
	}
	
	@Override
	public boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition) {
		// Si on est en train de créer un rectangle de sélection d'unités
		if (selectingUnits) {
			// Dessin du rectangle de sélection
			// TODO ...
		} else {
			// Sinon, on déplace la carte, donc la caméra
			getCamera().position.add(
				touchDownPos.x - worldPosition.x,
				touchDownPos.y - worldPosition.y,
				0);
		}
		dragging = true;
		return true;
	}

	@Override
	public boolean tileTouchUp(Vector3 worldPosition, Point tilePosition) {
		// Si on était en train de créer un rectangle de sélection d'unités, c'est fini
		if (dragging) {
			if (selectingUnits) {
				// Détermine le rectangle de sélection
				final float minX;
				final float maxX;
				if (touchDownPos.x < worldPosition.x) {
					minX = touchDownPos.x;
					maxX = worldPosition.x;
				} else {
					minX = worldPosition.x;
					maxX = touchDownPos.x;
				}
				final float minY;
				final float maxY;
				if (touchDownPos.y < worldPosition.y) {
					minY = touchDownPos.y;
					maxY = worldPosition.y;
				} else {
					minY = worldPosition.y;
					maxY = touchDownPos.y;
				}
				Rectangle selectArea = new Rectangle();
				selectArea.x = minX;
				selectArea.y = minY;
				selectArea.width = maxX - minX;
				selectArea.height = maxY - minY;
				((GameScreen)getScreen()).selectUnitsIn(selectArea);
			}
		} else {
			// Si on n'était pas en train de déplacer la caméra, alors on vient
			// d'avoir une touche simple : c'est l'action pour spawner une unité
			((GameScreen)getScreen()).createUnit(tilePosition);
		}
		dragging = false;
		return true;
	}
}
