package com.slamdunk.toolkit.screen.overlays;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.graphics.tiled.OrthogonalTiledMapRendererWithSprites;
import com.slamdunk.toolkit.graphics.tiled.SpriteMapObject;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.pathfinder.PathFinder;
import com.slamdunk.toolkit.world.point.Point;

public class TiledMapOverlay implements SlamOverlay {
	private static final String SPRITES_LAYER = "sprites";
	
	public interface TiledMapInputProcessor {
		/**
		 * Appelée lorsque le joueur déplace sa touche sur la carte. La méthode reçoit 
		 * les coordonnées de la touche en pixels du monde ainsi que les coordonnées 
		 * de la tuile touchée.
		 * @param worldPosition
		 * @param tilePosition
		 * @return
		 */
		boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition);

		/**
		 * Appelée lorsque le joueur relache sa touche sur la carte. La méthode reçoit  
		 * les coordonnées de la touche en pixels du monde ainsi que les coordonnées de 
		 * la tuile touchée.
		 * @param worldPosition
		 * @param tilePosition
		 * @param offset 
		 * @return
		 */
		boolean tileTouchUp(Vector3 worldPosition, Point tilePosition);

		/**
		 * Appelée lorsque le joueur touche la carte. La méthode reçoit les coordonnées 
		 * de la touche en pixels du monde ainsi que les coordonnées de la tuile touchée.
		 * @param worldPosition
		 * @param tilePosition
		 * @return
		 */
		boolean tileTouchDown(Vector3 worldPosition, Point tilePosition);
	}
	
	private class TiledMapOverlayInputProcessor implements InputProcessor {
		private TiledMapInputProcessor tileInputProcessor;
		
		public TiledMapOverlayInputProcessor(TiledMapInputProcessor tileInputProcessor) {
			this.tileInputProcessor = tileInputProcessor;
		}
		
		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
			case Keys.UP:
				camera.position.y++;
				break;
			case Keys.DOWN:
				camera.position.y--;
				break;
			case Keys.LEFT:
				camera.position.x--;
				break;
			case Keys.RIGHT:
				camera.position.x++;
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
		    Point tilePosition = new Point(
		    	(int)(worldPosition.x * pixelsByTile / tileWidth),
		    	(int)(worldPosition.y * pixelsByTile / tileHeight));
		    
			return tileInputProcessor.tileTouchDown(worldPosition, tilePosition);
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
		    Point tilePosition = new Point(
		    	(int)(worldPosition.x * pixelsByTile / tileWidth),
		    	(int)(worldPosition.y * pixelsByTile / tileHeight));
		    
			return tileInputProcessor.tileTouchUp(worldPosition, tilePosition);
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
		    Point tilePosition = new Point(
		    	(int)(worldPosition.x * pixelsByTile / tileWidth),
		    	(int)(worldPosition.y * pixelsByTile / tileHeight));
			return tileInputProcessor.tileTouchDragged(worldPosition, tilePosition);
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}
	}
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMapOverlayInputProcessor inputProcessor;
	
	private int tileWidth;
	private int tileHeight;
	private float pixelsByTile;
	
	private PathFinder pathfinder;
	
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
		
		renderer = new OrthogonalTiledMapRendererWithSprites(map, 1 / pixelsByTile/*DBG, SPRITES_LAYER*/);
		
		// Crée une caméra qui montre fieldOfViewWidth x fieldOfViewHeight unités du monde 
		if (fieldOfViewWidth == -1) {
			fieldOfViewWidth = Gdx.graphics.getWidth() / tileWidth;
		}
		if (fieldOfViewHeight == -1) {
			fieldOfViewHeight = Gdx.graphics.getHeight() / tileHeight;
		}
		camera = new OrthographicCamera();
		camera.setToOrtho(false, fieldOfViewWidth, fieldOfViewHeight);
		camera.update();
	}
	
	/**
	 * Définit l'objet qui recevra les actions effectuées sur la map
	 * @param tileInputProcessor
	 */
	public void setTileInputProcessor(TiledMapInputProcessor tileInputProcessor) {
		// Crée un objet chargé de gérer les touches sur la carte
		inputProcessor = new TiledMapOverlayInputProcessor(tileInputProcessor);
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
	
	@Override
	public void act(float delta) {
	}

	@Override
	public void draw() {
		if (map == null) {
			return;
		}
		// Met à jour les matrices de la caméra
		camera.update();
		// Configure le renderer en fonction de ce que voit la caméra
		renderer.setView(camera);
		// Procède au rendu de la map
		renderer.render();
	}

	@Override
	public void dispose() {
		if (map != null) {
			map.dispose();
		}
	}

	@Override
	public boolean isProcessInputs() {
		return true;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return inputProcessor;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}

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
	 * Déplace la caméra pour la centrer sur la case où se trouve l'objet
	 * indiqué sur la couche indiquée
	 * @param layerName
	 * @param objectName
	 */
	public void setCameraOnObject(String layerName, String objectName) {
		MapObject object = getObject(layerName, objectName);
		if (object != null) {
			setCameraOnObject(object);
		}
	}
	
	/**
	 * Déplace la caméra pour la centrer sur la case où se trouve l'objet
	 * indiqué
	 * @param layerName
	 * @param objectName
	 */
	public void setCameraOnObject(MapObject object) {
		camera.position.x = convertFromPixelToMapX(MapObjectHelper.getX(object));
		camera.position.y = convertFromPixelToMapY(MapObjectHelper.getY(object));
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

	/**
	 * Ajoute le sprite indiqué à la map, en adaptant sa taille (en pixels) pour qu'elle
	 * soit cohérente avec le reste de la map (en unités).
	 * @param sprite
	 * @return 
	 */
	public SpriteMapObject addSprite(Sprite sprite, String name, String layerName) {
		// Adapte la taille du sprite (exprimée en pixels) à celle du monde
		sprite.setSize(sprite.getWidth() / pixelsByTile, sprite.getHeight() / pixelsByTile);
		
		// Ajoute le sprite à la couche adéquate
		MapLayer layer = map.getLayers().get(layerName);
		if (layer == null) {
			return null;
		}
		SpriteMapObject spriteMapObject = new SpriteMapObject(sprite, name, tileWidth);
		layer.getObjects().add(spriteMapObject);
		return spriteMapObject;
	}
	
	/**
	 * Ajoute le sprite indiqué à la map, en adaptant sa taille (en pixels) pour qu'elle
	 * soit cohérente avec le reste de la map (en unités).
	 * @param sprite
	 */
	public SpriteMapObject addSprite(Sprite sprite, String name) {
		return addSprite(sprite, name, SPRITES_LAYER);
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
