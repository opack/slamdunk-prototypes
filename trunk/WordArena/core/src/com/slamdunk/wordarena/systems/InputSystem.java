package com.slamdunk.wordarena.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.wordarena.Arena;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.CellStates;
import com.slamdunk.wordarena.GameStates;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.InputComponent;
import com.slamdunk.wordarena.components.LetterCellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;

public class InputSystem extends IteratingSystem implements InputProcessor {
	private Vector3 tmp;
	private Vector2 lastTouch;
	private long lastEntityId;
	private List<Entity> selectedLetters;
	private boolean isDragging;
	
	private Arena arena;
	private Camera camera;
	
	@SuppressWarnings("unchecked")
	public InputSystem(Camera camera) {
		super(Family.all(InputComponent.class, TextureComponent.class, BoundsComponent.class, TransformComponent.class).get());

		this.camera = camera;
		
		tmp = new Vector3();
		lastTouch = new Vector2();
		selectedLetters = new ArrayList<Entity>();
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	/**
	 * 
	 * @param screenX
	 * @param screenY
	 * @return true si la touche a été gérée
	 */
	private boolean updateLastTouch(int screenX, int screenY) {
		if (screenX < 0
		|| screenX > WordArenaGame.SCREEN_WIDTH
		|| screenY < 0
		|| screenY > WordArenaGame.SCREEN_HEIGHT) {
			return false;
		}
		tmp.set(screenX, screenY, 1);
		camera.unproject(tmp);
		lastTouch.set(tmp.x, tmp.y);
		return true;
	}
	
	private void updateLetterCellState(Entity entity, CellStates state) {
		LetterCellComponent letterCell = ComponentMappers.LETTER_CELL.get(entity);
		letterCell.type = state;
		TextureComponent texture = ComponentMappers.TEXTURE.get(entity);
		texture.region = Assets.letterData.get(letterCell.letter, letterCell.type);
	}
	
	@Override
	public void update(float deltaTime) {
		if (!isDragging) {
			return;
		}
		super.update(deltaTime);
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		BoundsComponent bounds = ComponentMappers.BOUNDS.get(entity);
		
		if (lastEntityId != entity.getId()
		&& bounds.bounds.contains(lastTouch.x, lastTouch.y)) {
			lastEntityId = entity.getId();
			
			updateLetterCellState(entity, CellStates.SELECTED);
			selectedLetters.add(entity);
		}
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
		return arena.state == GameStates.RUNNING && updateLastTouch(screenX, screenY);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (Entity entity : selectedLetters) {
			updateLetterCellState(entity, CellStates.NORMAL);
		}
		selectedLetters.clear();
		isDragging = false;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateLastTouch(screenX, screenY);
		isDragging = true;
		return true;
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
