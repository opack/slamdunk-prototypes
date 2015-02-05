package com.slamdunk.wordarena.ecs.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.wordarena.ecs.Assets;
import com.slamdunk.wordarena.ecs.CellStates;
import com.slamdunk.wordarena.ecs.GameStates;
import com.slamdunk.wordarena.ecs.arena.Arena;
import com.slamdunk.wordarena.ecs.components.ColliderComponent;
import com.slamdunk.wordarena.ecs.components.LetterCellComponent;
import com.slamdunk.wordarena.ecs.components.TextureComponent;
import com.slamdunk.wordarena.ecs.components.TransformComponent;

public class WordSelectionHandler implements InputProcessor {
	private Vector3 tmp;
	
	private Entity lastEntity;
	private List<Entity> selectedEntities;
	
	private Arena arena;
	private Camera camera;
	
	private Rectangle screenBounds;
	private Rectangle arenaBounds;
	
	public WordSelectionHandler(Camera camera) {
		this.camera = camera;
		
		tmp = new Vector3();
		
		selectedEntities = new ArrayList<Entity>();
		
		screenBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		arenaBounds = new Rectangle(0, 0, 0, 0);
	}
	
	public List<Entity> getSelectedEntities() {
		return Collections.unmodifiableList(selectedEntities);
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
		arenaBounds.width = arena.getWidth() * (1 + Arena.CELL_GAP);
		arenaBounds.height = arena.getHeight() * (1 + Arena.CELL_GAP);
	}

	/**
	 * 
	 * @param screenX
	 * @param screenY
	 * @return true si la touche a été gérée
	 */
	private boolean updateLastTouch(int screenX, int screenY) {
		// Vérifie que les coordonnées sont bien sur la zone d'affichage
		// Ce test sert notamment en desktop car la fenêtre peut être plus petite
		// que l'écran. On ignore donc les touches hors de la fenêtre.
		if (!screenBounds.contains(screenX, screenY)) {
			return false;
		}
		
		// Convertit les coordonnées "écran" en coordonnées "monde"
		tmp.set(screenX, screenY, 1);
		camera.unproject(tmp);
		
		// Vérifie que les coordonnées sont bien au-dessus de l'arène
		if (!arenaBounds.contains(tmp.x, tmp.y)) {
			return false;
		}
		
		// Vérifier si la nouvelle case est bien autour de la dernière case sélectionnée
		int arenaX = (int)tmp.x;
		int arenaY = (int)tmp.y;
		if (lastEntity != null) {
			TransformComponent lastTransform = ComponentMappers.TRANSFORM.get(lastEntity);
			if (Math.abs(arenaX - lastTransform.pos.x) > 1
			|| Math.abs(arenaY - lastTransform.pos.y) > 1) {
				return true;
			}
		}
		
		// Vérifie si la nouvelle case n'est pas déjà sélectionnée
		Entity entity = arena.getEntityAt(arenaX, arenaY);
		if (selectedEntities.contains(entity)) {
			return true;
		}
		
		// Sélectionne la case
		ColliderComponent collider = ComponentMappers.COLLIDER.get(entity);

		if ((lastEntity == null	|| lastEntity.getId() != entity.getId())
		&& collider.bounds.contains(tmp.x, tmp.y)) {
			// Conserve l'entité de la dernière lettre sélectionnée
			lastEntity = entity;
			
			// Marque la lettre comme sélectionnée
			updateLetterCellState(entity, CellStates.SELECTED);
			
			// Ajoute la lettre aux lettres sélectionnées pour constituer un mot
			selectedEntities.add(entity);
		}
		return true;
	}
	
	private void updateLetterCellState(Entity entity, CellStates state) {
		LetterCellComponent letterCell = ComponentMappers.LETTER_CELL.get(entity);
		letterCell.type = state;
		
		TextureComponent texture = ComponentMappers.TEXTURE.get(entity);
		texture.region = Assets.letterData.get(letterCell.letter, letterCell.type);
	}

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
		if (arena.state == GameStates.RUNNING
		&& updateLastTouch(screenX, screenY)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateLastTouch(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void resetSelection() {
		for (Entity entity : selectedEntities) {
			updateLetterCellState(entity, CellStates.NORMAL);
		}
		selectedEntities.clear();
		lastEntity = null;
	}
}
