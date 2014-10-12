package com.slamdunk.toolkit.screen.overlays;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class TiledMapOverlay implements SlamOverlay {
	private class TiledMapInputProcessor implements InputProcessor {
		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
		    // TODO D�terminer la tuile touch�e
			int tileX = 0;
			int tileY = 0;
			return TiledMapOverlay.this.tileTouchDown(worldPosition, tileX, tileY);
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
	    	// TODO D�terminer la tuile touch�e
			int tileX = 0;
			int tileY = 0;
			return TiledMapOverlay.this.tileTouchUp(worldPosition, tileX, tileY);
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		    Vector3 worldPosition = camera.unproject(clickCoordinates);
		    // TODO D�terminer la tuile touch�e
			int tileX = 0;
			int tileY = 0;
			return TiledMapOverlay.this.tileTouchDragged(worldPosition, tileX, tileY);
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
	private TiledMapInputProcessor inputProcessor;
	
	// DBG Params values
	// mapFile="data/maps/tiled/super-koalio/level1.tmx"
	// pixelsByUnit=16
	// fieldOfViewWidth=30
	// fieldOfViewHeight=20
	public void load(String mapFile, float pixelsByUnit, int fieldOfViewWidth, int fieldOfViewHeight) {
		// Charge la carte et d�finit l'�chelle (1 unit� ==  pixelsByUnit pixels)
		map = new TmxMapLoader().load(mapFile);
		renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsByUnit);
		
		// Cr�e une cam�ra qui montre fieldOfViewWidth x fieldOfViewHeight unit�s du monde 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, fieldOfViewWidth, fieldOfViewHeight);
		camera.update();
		
		// Cr�e un objet charg� de g�rer les touches sur la carte
		inputProcessor = new TiledMapInputProcessor();
	}
	
	/**
	 * Appel�e lorsque le joueur d�place sa touche sur la carte. La m�thode re�oit 
	 * les coordonn�es de la touche en pixels du monde ainsi que les coordonn�es 
	 * de la tuile touch�e.
	 * @param worldPosition
	 * @param tileX
	 * @param tileY
	 * @return
	 */
	public boolean tileTouchDragged(Vector3 worldPosition, int tileX, int tileY) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Appel�e lorsque le joueur relache sa touche sur la carte. La m�thode re�oit  
	 * les coordonn�es de la touche en pixels du monde ainsi que les coordonn�es de 
	 * la tuile touch�e.
	 * @param worldPosition
	 * @param tileX
	 * @param tileY
	 * @return
	 */
	public boolean tileTouchUp(Vector3 worldPosition, int tileX, int tileY) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Appel�e lorsque le joueur touche la carte. La m�thode re�oit les coordonn�es 
	 * de la touche en pixels du monde ainsi que les coordonn�es de la tuile touch�e.
	 * @param worldPosition
	 * @param tileX
	 * @param tileY
	 * @return
	 */
	public boolean tileTouchDown(Vector3 worldPosition, int tileX, int tileY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void act(float delta) {
	}

	@Override
	public void draw() {
		if (map == null) {
			return;
		}
		// DBG let the camera follow the koala, x-axis only
		//camera.position.x = koala.position.x;
		
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

}
