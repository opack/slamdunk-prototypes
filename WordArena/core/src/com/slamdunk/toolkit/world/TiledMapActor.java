package com.slamdunk.toolkit.world;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.screen.overlays.MapObjectHelper;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.pathfinder.PathFinder;
import com.slamdunk.toolkit.world.point.Point;

public class TiledMapActor extends SlamActor {
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	private int tileWidth;
	private int tileHeight;
	private float pixelsByTile;
	
	private PathFinder pathfinder;
	
	public TiledMap getMap() {
		return map;
	}
	
	public int getMapWidth() {
		if (map == null) {
			return 0;
		}
		return (Integer)map.getProperties().get("width");
	}
	
	public int getMapHeight() {
		if (map == null) {
			return 0;
		}
		return (Integer)map.getProperties().get("height");
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public float getPixelsByTile() {
		return pixelsByTile;
	}
	
	/**
	 * Charge la carte depuis le fichier spécifié en affichant autant de tuiles que possible
	 * à l'écran.
	 * @param mapFile
	 * @param pixelsByUnit
	 */
	public void load(String mapFile) {
		load(mapFile, -1, -1, -1);
	}
	
	/**
	 * Charge la carte depuis le fichier spécifié.
	 * @param mapFile
	 * @param pixelsByUnit Nombre de pixels par unité du monde. Si -1, 1 tuile fait autant de pixels qu'indiqués
	 * dans le fichier de carte.
	 * @param fieldOfViewWidth Nombre de tuiles affichées en largeur. Si -1, affiche autant de tuiles que possible.
	 * @param fieldOfViewHeight Nombre de tuiles affichées en hauteur. Si -1, affiche autant de tuiles que possible.
	 */
	public void load(String mapFile, float pixelsByUnit, int fieldOfViewWidth, int fieldOfViewHeight) {
		// Charge la carte et définit l'échelle (1 unité ==  pixelsByUnit pixels)
		map = new TmxMapLoader().load(mapFile);
		tileWidth = (Integer)map.getProperties().get("tilewidth");
		tileHeight = (Integer)map.getProperties().get("tileheight");
		pixelsByTile = (pixelsByUnit == -1) ? tileWidth : pixelsByUnit;
		
//		renderer = new OrthogonalTiledMapRenderer(map, 1/* / pixelsByTile*/);
		renderer = new OrthogonalTiledMapRenderer(map, 1, getStage().getBatch());
		
		// Ajuste la taille de l'acteur à la carte 
		if (fieldOfViewWidth == -1) {
			fieldOfViewWidth = 800 / tileWidth;
		}
		if (fieldOfViewHeight == -1) {
			fieldOfViewHeight = 480 / tileHeight;
		}
		setSize(getMapWidth() * tileWidth, getMapHeight() * tileHeight);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		super.draw(batch, parentAlpha);
		if (map == null) {
			return;
		}
//		// Met à jour les matrices de la caméra
//		camera.update();
//		// Configure le renderer en fonction de ce que voit la caméra
//		renderer.setView((OrthographicCamera)getStage().getCamera());
		OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
		float viewportWidth = camera.viewportWidth * camera.zoom;
		float viewportHeight = camera.viewportHeight * camera.zoom;
		float viewportX = camera.position.x - viewportWidth / 2;
		float viewportY = camera.position.y - viewportHeight / 2;
		renderer.setView(camera.combined, viewportX, viewportY, viewportWidth, viewportHeight);
		// Procède au rendu de la map
		renderer.render();
		batch.begin();
	}
	
	/**
	 * Retourne l'objet de la map dont getName() vaut objectName et qui se trouve sur la couche
	 * layerName.
	 * @param layerName
	 * @param objectName
	 * @return
	 */
	public MapObject getObject(String layerName, String objectName) {
		MapLayer layer = map.getLayers().get(layerName);
		if (layer == null) {
			return null;
		}
		return layer.getObjects().get(objectName);
	}
	
	/**
	 * Retourne les objets de la classe objectClass sur la couche layerName
	 * @param layerName
	 * @param objectClass
	 * @return
	 */
	public Array<? extends MapObject> getObjects(String layerName, Class<? extends MapObject> objectClass) {
		MapLayer layer = map.getLayers().get(layerName);
		if (layer == null) {
			return null;
		}
		
		return layer.getObjects().getByType(objectClass);
	}
	
	/**
	 * Retourne les objets de la classe objectClass sur la couche layerName, ayant la propriété
	 * property à la valeur value. 
	 * @param layerName
	 * @param objectClass
	 * @param property
	 * @param value
	 * @return
	 */
	public MapObjects getObjects(String layerName, Class<? extends MapObject> objectClass, String property, Object value) {
		MapLayer layer = map.getLayers().get(layerName);
		if (layer == null) {
			return null;
		}
		
		Array<? extends MapObject> objects = layer.getObjects().getByType(objectClass);
		MapObjects mapObjects = new MapObjects();
		for (MapObject object : objects) {
			if (MapObjectHelper.hasValue(object, property, value)) {
				mapObjects.add(object);
			}
		}
		return mapObjects;
	}

	public float convertFromPixelToMapX(float pixelX) {
		return pixelX / tileWidth;
	}
	
	public float convertFromPixelToMapY(float pixelY) {
		return pixelY / tileHeight;
	}

	public void initPathfinder(boolean defaultWalkable) {
		pathfinder = new PathFinder(getMapWidth(), getMapHeight(), defaultWalkable);
	}
	
	/**
	 * Indique au pathfinder de cette carte la position des cases traversables en utilisant
	 * la position des objets de la classe objectClass ayant la propriété property à la
	 * valeur value sur la couche layerName
	 */
	public void setWalkables(String layerName, Class<? extends MapObject> objectClass, String property, Object value) {
		MapObjects paths = getObjects(layerName, objectClass, property, value);
		int tileX;
		int tileY;
		for (MapObject path : paths) {
			tileX = (int)convertFromPixelToMapX(MapObjectHelper.getX(path));
			tileY = (int)convertFromPixelToMapY(MapObjectHelper.getY(path));
			pathfinder.setWalkable(tileX, tileY, true);
		}
	}
	
	/**
	 * Indique si la position donnée en paramètre peut être parcourue
	 * @param position
	 * @return
	 */
	public boolean isWalkable(int x, int y) {
		return pathfinder.isWalkable(x, y);
	}
	
	/**
	 * Indique si la position donnée en paramètre peut être parcourue
	 * @param position
	 * @return
	 */
	public boolean isWalkable(Point position) {
		return pathfinder.isWalkable(position.getX(), position.getY());
	}

	/**
	 * Calcule le chemin entre 2 points de la carte
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return
	 */
	public Path findPath(int fromX, int fromY, int toX, int toY) {
		List<Point> positions = pathfinder.findPath(fromX, fromY, toX, toY, true);
		if (positions != null && !positions.isEmpty()) {
			return new Path(positions);
		}
		return null;
	}

	public Path findPath(MapObject from, MapObject to) {
		return findPath(
			(int)convertFromPixelToMapX(MapObjectHelper.getX(from)), (int)convertFromPixelToMapY(MapObjectHelper.getY(from)),
			(int)convertFromPixelToMapX(MapObjectHelper.getX(to)), (int)convertFromPixelToMapY(MapObjectHelper.getY(to)));
	}
}
