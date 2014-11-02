package com.slamdunk.toolkit.screen.overlays;

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
import com.slamdunk.toolkit.world.pathfinder.PathFinder;
import com.slamdunk.toolkit.world.point.Point;

public class TiledMapOverlay implements SlamOverlay {
	private static final String SPRITES_LAYER = "sprites";
	
	public interface TiledMapInputProcessor {
		/**
		 * Appel�e lorsque le joueur d�place sa touche sur la carte. La m�thode re�oit 
		 * les coordonn�es de la touche en pixels du monde ainsi que les coordonn�es 
		 * de la tuile touch�e.
		 * @param worldPosition
		 * @param tilePosition
		 * @return
		 */
		boolean tileTouchDragged(Vector3 worldPosition, Point tilePosition);

		/**
		 * Appel�e lorsque le joueur relache sa touche sur la carte. La m�thode re�oit  
		 * les coordonn�es de la touche en pixels du monde ainsi que les coordonn�es de 
		 * la tuile touch�e.
		 * @param worldPosition
		 * @param tilePosition
		 * @return
		 */
		boolean tileTouchUp(Vector3 worldPosition, Point tilePosition);

		/**
		 * Appel�e lorsque le joueur touche la carte. La m�thode re�oit les coordonn�es 
		 * de la touche en pixels du monde ainsi que les coordonn�es de la tuile touch�e.
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
	 * Charge la carte depuis le fichier sp�cifi�.
	 * @param mapFile
	 * @param pixelsByUnit Nombre de pixels par unit� du monde. Si -1, 1 tuile fait autant de pixels qu'indiqu�s
	 * dans le fichier de carte.
	 * @param fieldOfViewWidth Nombre de tuiles affich�es en largeur. Si -1, affiche autant de tuiles que possible.
	 * @param fieldOfViewHeight Nombre de tuiles affich�es en hauteur. Si -1, affiche autant de tuiles que possible.
	 */
	public void load(String mapFile, float pixelsByUnit, int fieldOfViewWidth, int fieldOfViewHeight) {
		// Charge la carte et d�finit l'�chelle (1 unit� ==  pixelsByUnit pixels)
		map = new TmxMapLoader().load(mapFile);
		tileWidth = (Integer)map.getProperties().get("tilewidth");
		tileHeight = (Integer)map.getProperties().get("tileheight");
		pixelsByTile = (pixelsByUnit == -1) ? tileWidth : pixelsByUnit;
		
		renderer = new OrthogonalTiledMapRendererWithSprites(map, 1 / pixelsByTile/*DBG, SPRITES_LAYER*/);
		
		// Cr�e une cam�ra qui montre fieldOfViewWidth x fieldOfViewHeight unit�s du monde 
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
	 * D�finit l'objet qui recevra les actions effectu�es sur la map
	 * @param tileInputProcessor
	 */
	public void setTileInputProcessor(TiledMapInputProcessor tileInputProcessor) {
		// Cr�e un objet charg� de g�rer les touches sur la carte
		inputProcessor = new TiledMapOverlayInputProcessor(tileInputProcessor);
	}
	
	/**
	 * Charge la carte depuis le fichier sp�cifi� en affichant autant de tuiles que possible
	 * � l'�cran.
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
		// Met � jour les matrices de la cam�ra
		camera.update();
		// Configure le renderer en fonction de ce que voit la cam�ra
		renderer.setView(camera);
		// Proc�de au rendu de la map
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

	/**
	 * D�place la cam�ra pour la centrer sur la case o� se trouve l'objet
	 * indiqu� sur la couche indiqu�e
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
	 * D�place la cam�ra pour la centrer sur la case o� se trouve l'objet
	 * indiqu�
	 * @param layerName
	 * @param objectName
	 */
	public void setCameraOnObject(MapObject object) {
		camera.position.x = convertFromPixelToMapX((Float)object.getProperties().get("x"));
		camera.position.y = convertFromPixelToMapY((Float)object.getProperties().get("y"));
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
	 * Retourne les objets de la classe objectClass sur la couche layerName, ayant la propri�t�
	 * property � la valeur value. 
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
		Object readValue;
		MapObjects mapObjects = new MapObjects();
		for (MapObject object : objects) {
			readValue = object.getProperties().get(property);
			if (readValue != null && readValue.equals(value)) {
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
	 * Ajoute le sprite indiqu� � la map, en adaptant sa taille (en pixels) pour qu'elle
	 * soit coh�rente avec le reste de la map (en unit�s).
	 * @param sprite
	 * @return 
	 */
	public SpriteMapObject addSprite(Sprite sprite, String name, String layerName) {
		// Adapte la taille du sprite (exprim�e en pixels) � celle du monde
		sprite.setSize(sprite.getWidth() / pixelsByTile, sprite.getHeight() / pixelsByTile);
		
		// Ajoute le sprite � la couche ad�quate
		MapLayer layer = map.getLayers().get(layerName);
		if (layer == null) {
			return null;
		}
		SpriteMapObject spriteMapObject = new SpriteMapObject(sprite, name, tileWidth);
		layer.getObjects().add(spriteMapObject);
		return spriteMapObject;
	}
	
	/**
	 * Ajoute le sprite indiqu� � la map, en adaptant sa taille (en pixels) pour qu'elle
	 * soit coh�rente avec le reste de la map (en unit�s).
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
	 * la position des objets de la classe objectClass ayant la propri�t� property � la
	 * valeur value sur la couche layerName
	 */
	public void setWalkables(String layerName, Class<? extends MapObject> objectClass, String property, Object value) {
		MapObjects paths = getObjects(layerName, objectClass, property, value);
		int tileX;
		int tileY;
		for (MapObject path : paths) {
			tileX = (int)convertFromPixelToMapX((Float)path.getProperties().get("x"));
			tileY = (int)convertFromPixelToMapY((Float)path.getProperties().get("y"));
			pathfinder.setWalkable(tileX, tileY, true);
		}
	}
}
